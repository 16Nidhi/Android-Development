package com.example.cse225android

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat
import java.util.*

class Activity6AlarmReceiver : BroadcastReceiver() {
    
    companion object {
        private var currentRingtone: Ringtone? = null
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val title = intent?.getStringExtra("REMINDER_TITLE") ?: "Smart Reminder"
        val message = "It's time for your scheduled task!"

        // 6. Custom Notification Sounds (Logic based on title)
        val alarmSound: Uri = when {
            title.contains("Work", true) || title.contains("Study", true) -> 
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            title.contains("Exercise", true) -> 
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            else -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        }

        try {
            currentRingtone?.stop()
            currentRingtone = RingtoneManager.getRingtone(context, alarmSound)
            currentRingtone?.play()

            // 7. Auto-Silence (Stop after 60 seconds)
            Handler(Looper.getMainLooper()).postDelayed({
                currentRingtone?.stop()
            }, 60000) 
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 5. Smart History Log (Save to SharedPreferences)
        saveToHistory(context, title)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationIntent = Intent(context, ACTIVITY6::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, notificationIntent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "ALARM_CHANNEL_ID")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun saveToHistory(context: Context, task: String) {
        val prefs = context.getSharedPreferences("SmartReminderPrefs", Context.MODE_PRIVATE)
        val history = prefs.getStringSet("history_logs", mutableSetOf())?.toMutableList() ?: mutableListOf()
        
        val timeStamp = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date())
        history.add(0, "$task|$timeStamp") // Add at top
        
        // Keep only last 20 entries
        val limitedHistory = if (history.size > 20) history.take(20) else history
        
        prefs.edit().putStringSet("history_logs", limitedHistory.toSet()).apply()
    }
}
