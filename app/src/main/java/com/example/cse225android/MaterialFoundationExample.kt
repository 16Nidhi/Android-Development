package com.example.cse225android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cse225android.ui.theme.CSE225AndroidTheme

class MaterialFoundationExample : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CSE225AndroidTheme {
                ExampleScreen()
                }
            }
        }
    }

@Composable
fun ExampleScreen(){
    Column(
        modifier = Modifier.fillMaxSize().background(Color.LightGray).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        //FOUNDATION COMPOSABLE
        BasicText("Foundation Text")
        Spacer(modifier = Modifier.height(28.dp))
        
        //MATERIAL COMPOSABLE
        Text(
            text = "Material Text",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(28.dp))
        
        Button(onClick = { }) {
            Text("Material Button")
        }
        Spacer(modifier = Modifier.height(28.dp))
        
        //FOUNDATION IMAGE
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Sample Image",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(28.dp))
        
//MATERIAL CARD
      Card(
          modifier = Modifier.fillMaxWidth()
      ){
          Text(
              text = "Material Card Example",
              modifier = Modifier.padding(16.dp)
          )
      }
    }
}

/*@Preview(showBackground = true)
@Composable
fun GreetingPreview6() {
    CSE225AndroidTheme {
        Greeting("Android")
    }
}*/
