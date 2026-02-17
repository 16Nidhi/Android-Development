package com.example.cse225android

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cse225android.ui.theme.CSE225AndroidTheme

class PendingIntentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CSE225AndroidTheme {
                BroadcastButtonScreen()
            }
        }
    }
}
@Composable
fun BroadcastButtonScreen() {
    val context = LocalContext.current

    Surface {
        Button(
            onClick = {
                triggerBroadcast(context) },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Send Broadcast using Pending Intent")
        }
    }
}
fun triggerBroadcast(context: Context) {
    val intent = Intent(context, ButtonReceiver::class.java)

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    try {
        pendingIntent.send()
    } catch (e: PendingIntent.CanceledException) {
        e.printStackTrace()
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    CSE225AndroidTheme {
        BroadcastButtonScreen()
    }
}
