package com.example.cse225android

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cse225android.ui.theme.CSE225AndroidTheme

class ProjectAlarm : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CSE225AndroidTheme {
                AlarmApp()
            }
        }
    }
}

@Composable
fun AlarmApp() {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize()
                  .padding(16.dp),
                   verticalArrangement = Arrangement.Center
    ) {
        Text("Alarm Clock")
    }
      Button(
            onClick = { setAlarm(context) },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(text = "Set Alarm (10 sec)")
        }
    }

fun setAlarm(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val triggerTime = System.currentTimeMillis() + 10000  // 10 seconds

    // 'set' is inexact and doesn't require complex permission handling in code
    alarmManager.set(
        AlarmManager.RTC_WAKEUP,
        triggerTime,
        pendingIntent
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview8() {
    CSE225AndroidTheme {
        AlarmApp()
    }
}
