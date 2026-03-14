package com.example.cse225android

import android.app.*
import android.content.*
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
                                        Color(0xFF1A237E), 
                                        MaterialTheme.colorScheme.surface
                                    )
                                )
                            )
                    ) {
                        AnimatedContent(
                            targetState = step,
                            transitionSpec = {
                                fadeIn() togetherWith fadeOut()
                            },
                            label = "ScreenTransition"
                        ) { targetStep ->
                            when (targetStep) {
                                0 -> TaskAndTimeSelection(
                                    selectedTask, { selectedTask = it },
                                    otherTask, { otherTask = it },
                                    predefinedTimes, selectedTimes,
                                    onNext = { step = 1 }
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
                            }
                        }
                    }
                }
                BackHandler(step > 0 && step != 3) { step-- }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskAndTimeSelection(
    selectedTask: String, onTaskChange: (String) -> Unit,
    otherTask: String, onOtherChange: (String) -> Unit,
    times: List<String>, selectedTimes: MutableList<String>,
    onNext: () -> Unit
) {
    val tasks = listOf("Wake me up", "Medicine", "Study", "Work", "Exercise", "Other")
    var expanded by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Text(
            "Set Your Routine", 
            fontSize = 32.sp, 
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Spacer(Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Select Task", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                    OutlinedTextField(
                        value = selectedTask, onValueChange = {}, readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true).fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        tasks.forEach { task ->
                            DropdownMenuItem(
                                text = { Text(task) }, 
                                onClick = {
                                    onTaskChange(task)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                if (selectedTask == "Other") {
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = otherTask, onValueChange = onOtherChange,
                        label = { Text("Custom Task Name") }, 
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Text("Choose Timings", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        Spacer(Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(times) { time ->
                val isSelected = selectedTimes.contains(time)
                Surface(
                    onClick = { 
                        if (isSelected) selectedTimes.remove(time) 
                        else selectedTimes.add(time) 
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                    tonalElevation = if (isSelected) 4.dp else 0.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.width(16.dp))
                        Text(
                            time, 
                            Modifier.weight(1f),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 18.sp,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                        )
                        Switch(
                            checked = isSelected, 
                            onCheckedChange = { 
                                if (it) selectedTimes.add(time) 
                                else selectedTimes.remove(time) 
                            },
                            modifier = Modifier.scale(0.9f)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onNext, 
            modifier = Modifier.fillMaxWidth().height(60.dp), 
            enabled = selectedTimes.isNotEmpty(),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
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
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(30.dp)),
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
        Text(task, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        Text("Your chosen timings", color = Color.White.copy(alpha = 0.7f), fontSize = 16.sp)
        
        Spacer(Modifier.height(24.dp))
        LazyColumn(Modifier.weight(1f)) {
            items(times) { time ->
                Card(
                    Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
                ) {
                    Row(
                        Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Schedule, null, tint = Color(0xFF1A237E))
                        Spacer(Modifier.width(16.dp))
                        Text(time, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = onAddCustom, 
                Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(Color.White, Color.White)))
            ) { 
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.width(8.dp))
                Text("Custom") 
            }
            Button(
                onClick = onFinish, 
                Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
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
        ElevatedCard(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Timer, 
                    null, 
                    Modifier.size(80.dp), 
                    MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(16.dp))
                Text("Pick a Time", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Choose a specific time", color = MaterialTheme.colorScheme.secondary)
                Spacer(Modifier.height(32.dp))
                
                Button(
                    onClick = {
                        TimePickerDialog(context, { _, h, m ->
                            val time = String.format(Locale.getDefault(), "%02d:%02d %s", if (h % 12 == 0) 12 else h % 12, m, if (h < 12) "AM" else "PM")
                            onTimePicked(time)
                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Select Time")
                }
                
                TextButton(onClick = onBack, modifier = Modifier.padding(top = 8.dp)) { 
                    Text("Cancel", color = MaterialTheme.colorScheme.error) 
                }
            }
        }
    }
}

@Composable
fun FinalSuccessScreen(task: String, times: List<String>, onDone: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(40.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.CheckCircle, 
                null, 
                Modifier.size(64.dp), 
                Color(0xFF4CAF50)
            )
        }
        
        Spacer(Modifier.height(32.dp))
        Text(
            "Reminder Set!", 
            fontSize = 32.sp, 
            fontWeight = FontWeight.ExtraBold, 
            color = Color.White
        )
        Text(
            "You'll be alerted for \"$task\"", 
            fontSize = 18.sp, 
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        
        Spacer(Modifier.height(32.dp))
        Text(
            "Scheduled Times:", 
            fontSize = 16.sp, 
            fontWeight = FontWeight.SemiBold, 
            color = Color.White,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(Modifier.height(8.dp))
        
        LazyColumn(
            modifier = Modifier.weight(0.5f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(times) { time ->
                Surface(
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = time,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        
        Spacer(Modifier.height(40.dp))
        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Great, Thanks!", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}
