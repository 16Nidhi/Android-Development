package com.example.cse225android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cse225android.ui.theme.CSE225AndroidTheme

class ButtonReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Toast.makeText(context,
            "Broadcast Received via PendingIntent",
            Toast.LENGTH_LONG).show()

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview7() {
    CSE225AndroidTheme {
    }
}