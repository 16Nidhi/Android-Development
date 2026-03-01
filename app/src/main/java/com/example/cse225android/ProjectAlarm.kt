package com.example.cse225android

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cse225android.ui.theme.CSE225AndroidTheme
import java.util.*

class ProjectAlarm : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContent {
            CSE225AndroidTheme {
                AlarmApp()
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alarm Channel"
            val descriptionText = "Channel for Alarm Notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("ALARM_CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

@Composable
fun AlarmApp() {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    var selectedTime by remember { mutableStateOf("No alarm set") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Advanced Alarm", fontSize = 32.sp, modifier = Modifier.padding(bottom = 32.dp))
        
        Text(text = selectedTime, fontSize = 20.sp, modifier = Modifier.padding(bottom = 16.dp))

        Button(
            onClick = {
                val currentTime = Calendar.getInstance()
                val timePickerDialog = TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                        calendar.set(Calendar.SECOND, 0)
                        
                        if (calendar.before(Calendar.getInstance())) {
                            calendar.add(Calendar.DATE, 1)
                        }

                        selectedTime = String.format(Locale.getDefault(), "Alarm set for %02d:%02d", hourOfDay, minute)
                        setAlarm(context, calendar.timeInMillis)
                    },
                    currentTime.get(Calendar.HOUR_OF_DAY),
                    currentTime.get(Calendar.MINUTE),
                    false
                )
                timePickerDialog.show()
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(text = "Pick Time & Set Alarm")
        }

        OutlinedButton(
            onClick = {
                cancelAlarm(context)
                selectedTime = "Alarm cancelled"
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(text = "Cancel Alarm")
        }
    }
}

fun setAlarm(context: Context, triggerTime: Long) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            } else {
                // Request permission or use inexact alarm
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                context.startActivity(Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }
        Toast.makeText(context, "Alarm set!", Toast.LENGTH_SHORT).show()
    } catch (e: SecurityException) {
        Toast.makeText(context, "Permission error", Toast.LENGTH_SHORT).show()
    }
}

fun cancelAlarm(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    alarmManager.cancel(pendingIntent)
    Toast.makeText(context, "Alarm cancelled", Toast.LENGTH_SHORT).show()
}
