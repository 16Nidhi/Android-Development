package com.example.cse225android

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        // Play ringtone
        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtone = RingtoneManager.getRingtone(context, alarmSound)
        ringtone.play()

        // Show Notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        val notificationIntent = Intent(context, ProjectAlarm::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 
            0, 
            notificationIntent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Add Snooze action
        val snoozeIntent = Intent(context, SnoozeReceiver::class.java)
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "ALARM_CHANNEL_ID")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("Alarm Triggered!")
            .setContentText("Your scheduled alarm is ringing.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(android.R.drawable.ic_menu_recent_history, "Snooze (5m)", snoozePendingIntent)
            .build()

        notificationManager.notify(1, notification)
    }
}
