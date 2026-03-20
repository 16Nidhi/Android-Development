package com.example.cse225android

import android.app.*
import android.content.*
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cse225android.ui.theme.CSE225AndroidTheme
import java.util.*

class ACTIVITY6 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContent {
            CSE225AndroidTheme {
                var step by remember { mutableIntStateOf(0) }
                var selectedTask by remember { mutableStateOf("Wake me up") }
                var otherTask by remember { mutableStateOf("") }
                
                val predefinedTimes = remember {
                    (0..23).flatMap { hour ->
                        listOf(0, 30).map { minute ->
                            val amPm = if (hour < 12) "AM" else "PM"
                            val displayHour = when {
                                hour == 0 -> 12
                                hour > 12 -> hour - 12
                                else -> hour
                            }
                            String.format(Locale.getDefault(), "%02d:%02d %s", displayHour, minute, amPm)
                        }
                    }
                }
                
                val selectedTimes = remember { mutableStateListOf<String>() }
                val context = LocalContext.current

                Scaffold(modifier = Modifier.fillMaxSize()) { p ->
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .padding(p).background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF0D1B2A), 
                                        Color(0xFF1B263B),
                                        Color(0xFF415A77)
                                    )
                                )
                            )
                    ) {
                        AnimatedContent(
                            targetState = step,
                            transitionSpec = {
                                if (targetState > initialState) {
                                    (slideInHorizontally { it } + fadeIn()).togetherWith(slideOutHorizontally { -it } + fadeOut())
                                } else {
                                    (slideInHorizontally { -it } + fadeIn()).togetherWith(slideOutHorizontally { it } + fadeOut())
                                }.using(SizeTransform(clip = false))
                            },
                            label = "ScreenTransition"
                        ) { targetStep ->
                            when (targetStep) {
                                0 -> TaskAndTimeSelection(
                                    selectedTask, { selectedTask = it },
                                    otherTask, { otherTask = it },
                                    predefinedTimes, selectedTimes,
                                    onNext = { step = 1 },
                                    onViewHistory = { step = 4 }
                                )
                                1 -> ConfirmationScreen(
                                    task = if (selectedTask == "Other") otherTask else selectedTask,
                                    times = selectedTimes,
                                    onAddCustom = { step = 2 },
                                    onFinish = { 
                                        scheduleMultipleAlarms(context, selectedTimes, if (selectedTask == "Other") otherTask else selectedTask)
                                        step = 3
                                    }
                                )
                                2 -> CustomTimePickerScreen(
                                    onTimePicked = { time ->
                                        if (!selectedTimes.contains(time)) selectedTimes.add(time)
                                        step = 1
                                    },
                                    onBack = { step = 1 }
                                )
                                3 -> FinalSuccessScreen(
                                    task = if (selectedTask == "Other") otherTask else selectedTask,
                                    times = selectedTimes,
                                    onDone = {
                                        step = 0
                                        selectedTimes.clear()
                                        otherTask = ""
                                        selectedTask = "Wake me up"
                                    }
                                )
                                4 -> HistoryLogScreen(onBack = { step = 0 })
                            }
                        }
                    }
                }
                BackHandler(step > 0 && step != 3) { 
                    if (step == 4) step = 0 else step-- 
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("ALARM_CHANNEL_ID", "Reminders", NotificationManager.IMPORTANCE_HIGH)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    private fun scheduleMultipleAlarms(context: Context, times: List<String>, title: String) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        times.forEach { timeStr ->
            val calendar = Calendar.getInstance()
            val parts = timeStr.split(":", " ")
            var hour = parts[0].toInt()
            val minute = parts[1].toInt()
            val amPm = parts[2]
            
            if (amPm == "PM" && hour < 12) hour += 12
            if (amPm == "AM" && hour == 12) hour = 0
            
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            
            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1)
            }

            val intent = Intent(context, Activity6AlarmReceiver::class.java).apply {
                putExtra("REMINDER_TITLE", title)
            }
            val pi = PendingIntent.getBroadcast(
                context, 
                calendar.timeInMillis.toInt(), 
                intent, 
                PendingIntent.FLAG_IMMUTABLE
            )
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
        }
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .shadow(10.dp, RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(24.dp))
            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
            .padding(16.dp),
        content = content
    )
}

@Composable
fun TaskAndTimeSelection(
    selectedTask: String, onTaskChange: (String) -> Unit,
    otherTask: String, onOtherChange: (String) -> Unit,
    times: List<String>, selectedTimes: MutableList<String>,
    onNext: () -> Unit,
    onViewHistory: () -> Unit
) {
    val tasks = listOf(
        "Wake me up" to Icons.Default.Alarm,
        "Medicine" to Icons.Default.MedicalServices,
        "Study" to Icons.Default.MenuBook,
        "Work" to Icons.Default.Work,
        "Exercise" to Icons.Default.FitnessCenter,
        "Other" to Icons.Default.MoreHoriz
    )

    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Set Your Routine", 
                fontSize = 30.sp, 
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = (-1).sp
            )
            IconButton(onClick = onViewHistory) {
                Icon(Icons.Default.History, "History", tint = Color.White)
            }
        }
        Spacer(Modifier.height(24.dp))
        
        Text("Select Category", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White.copy(alpha = 0.7f))
        Spacer(Modifier.height(12.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(tasks) { (name, icon) ->
                val isSelected = selectedTask == name
                Column(
                    modifier = Modifier
                        .width(90.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) Color.White else Color.White.copy(alpha = 0.1f))
                        .clickable { onTaskChange(name) }
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        icon, 
                        contentDescription = null, 
                        tint = if (isSelected) Color(0xFF1B263B) else Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        name, 
                        fontSize = 12.sp, 
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color(0xFF1B263B) else Color.White,
                        maxLines = 1,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if (selectedTask == "Other") {
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = otherTask, onValueChange = onOtherChange,
                label = { Text("Custom Task Name", color = Color.White.copy(alpha = 0.6f)) }, 
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                )
            )
        }

        Spacer(Modifier.height(32.dp))
        Text("Choose Timings", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        Spacer(Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(times) { index, time ->
                val isSelected = selectedTimes.contains(time)
                
                // Entrance animation
                val animatedAlpha by animateFloatAsState(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 500, delayMillis = index * 20),
                    label = "alpha"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer { alpha = animatedAlpha }
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) Color.White.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.1f))
                        .border(1.dp, if (isSelected) Color.White else Color.Transparent, RoundedCornerShape(16.dp))
                        .clickable { 
                            if (isSelected) selectedTimes.remove(time) 
                            else selectedTimes.add(time) 
                        }
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f)
                        )
                        Spacer(Modifier.width(16.dp))
                        Text(
                            time, 
                            Modifier.weight(1f),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        if (isSelected) {
                            Icon(Icons.Default.CheckCircle, null, tint = Color.White)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onNext, 
            modifier = Modifier.fillMaxWidth().height(64.dp), 
            enabled = selectedTimes.isNotEmpty(),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF1B263B),
                disabledContainerColor = Color.White.copy(alpha = 0.3f)
            )
        ) {
            Text("Review Selection", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ConfirmationScreen(task: String, times: List<String>, onAddCustom: () -> Unit, onFinish: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(24.dp), 
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                .border(2.dp, Color.White.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.NotificationsActive, 
                null, 
                Modifier.size(48.dp), 
                Color.White
            )
        }
        
        Spacer(Modifier.height(24.dp))
        Text(task, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Your Schedule", color = Color.White.copy(alpha = 0.7f), fontSize = 16.sp)
        
        Spacer(Modifier.height(32.dp))
        
        GlassCard(modifier = Modifier.weight(1f)) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(times) { time ->
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.AccessTime, null, tint = Color.White.copy(alpha = 0.6f))
                        Spacer(Modifier.width(16.dp))
                        Text(time, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(
                onClick = onAddCustom, 
                Modifier.weight(1f).height(60.dp),
                shape = RoundedCornerShape(20.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(Color.White, Color.White))),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) { 
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.width(8.dp))
                Text("Custom") 
            }
            Button(
                onClick = onFinish, 
                Modifier.weight(1f).height(60.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF1B263B))
            ) { 
                Text("Set Alarms") 
            }
        }
    }
}

@Composable
fun CustomTimePickerScreen(onTimePicked: (String) -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    
    Column(
        Modifier.fillMaxSize().padding(24.dp), 
        verticalArrangement = Arrangement.Center, 
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlassCard(modifier = Modifier.padding(16.dp)) {
            Column(
                Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Timer, 
                    null, 
                    Modifier.size(80.dp), 
                    Color.White
                )
                Spacer(Modifier.height(16.dp))
                Text("Pick a Time", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Select your preferred slot", color = Color.White.copy(alpha = 0.7f))
                Spacer(Modifier.height(32.dp))
                
                Button(
                    onClick = {
                        TimePickerDialog(context, { _, h, m ->
                            val time = String.format(Locale.getDefault(), "%02d:%02d %s", if (h % 12 == 0) 12 else h % 12, m, if (h < 12) "AM" else "PM")
                            onTimePicked(time)
                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF1B263B))
                ) {
                    Text("Open Time Picker")
                }
                
                TextButton(onClick = onBack, modifier = Modifier.padding(top = 12.dp)) { 
                    Text("Go Back", color = Color.White.copy(alpha = 0.6f)) 
                }
            }
        }
    }
}

@Composable
fun FinalSuccessScreen(task: String, times: List<String>, onDone: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Glow effect
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .scale(scale)
                    .background(Color.White.copy(alpha = 0.1f), CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White, CircleShape)
                    .shadow(20.dp, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Check, 
                    null, 
                    Modifier.size(50.dp), 
                    Color(0xFF1B263B)
                )
            }
        }
        
        Spacer(Modifier.height(40.dp))
        Text(
            "All Set!", 
            fontSize = 36.sp, 
            fontWeight = FontWeight.ExtraBold, 
            color = Color.White
        )
        Text(
            "Alarms scheduled for \"$task\"", 
            fontSize = 18.sp, 
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        
        Spacer(Modifier.height(40.dp))
        
        GlassCard(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            Text("Active Times", fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(Modifier.height(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(times) { time ->
                    Text(
                        text = "• $time",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }
        
        Spacer(Modifier.height(40.dp))
        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth().height(64.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF1B263B))
        ) {
            Text("Done", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun HistoryLogScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val history = remember {
        val prefs = context.getSharedPreferences("SmartReminderPrefs", Context.MODE_PRIVATE)
        prefs.getStringSet("history_logs", emptySet())?.toList()?.sortedByDescending { it.split("|").last() } ?: emptyList()
    }

    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
            }
            Text("Activity History", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        
        Spacer(Modifier.height(24.dp))
        
        if (history.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No history yet", color = Color.White.copy(alpha = 0.5f))
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(history) { entry ->
                    val parts = entry.split("|")
                    val task = parts.getOrNull(0) ?: "Unknown"
                    val time = parts.getOrNull(1) ?: ""
                    
                    GlassCard {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(task, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                                Text(time, fontSize = 14.sp, color = Color.White.copy(alpha = 0.6f))
                            }
                            Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50))
                        }
                    }
                }
            }
        }
    }
}
