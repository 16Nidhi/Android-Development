package com.example.cse225android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cse225android.ui.theme.CSE225AndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CSE225AndroidTheme {
                //ListExample()
                /*HorizontalList()*/
                //SimpleScrollExample()//
                //HorizontalScrollExample()//
                //NestedScrollExample()//
                //SpinnerExample()//
                CircularProgressExample()
                LinearProgressExample()

                }
            }
        }
    }

/*@Composable
fun ListExample(){

    val items = listOf("Android","Kotlin","Jetpack compose","Firebase","AI",
        "Android","Kotlin","Jetpack compose","Firebase","AI",
        "Android","Kotlin","Jetpack compose","Firebase","AI",)

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color.Blue).padding(16.dp)){
        items(items){item->
            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray)
            ){
                Text(
                    text = item,
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)

                )
            }
        }
    }
}
*/
/*@Composable
fun HorizontalList() {
    val items = listOf(
        "Android", "Kotlin", "Jetpack compose", "Firebase", "AI",
        "Android", "Kotlin", "Jetpack compose", "Firebase", "AI",
        "Android", "Kotlin", "Jetpack compose", "Firebase", "AI",
    )

    LazyRow(
        modifier = Modifier.fillMaxSize().background(Color.Blue).padding(16.dp)
    ) {
        items(items) { item ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray)
            ) {
                Box(contentAlignment = Alignment.Center) {

                    Text(
                        text = item,
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

*/
/*@Composable
fun SimpleScrollExample(){

    Column(
        modifier = Modifier.fillMaxSize()
                      .verticalScroll(rememberScrollState())
                      .background(Color.LightGray)
                      .padding(16.dp)
    ){
            repeat(25){
                Text(
                    text = "Scrollable Item $it",
                    fontSize = 25.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

}
*/

/*@Composable
fun HorizontalScrollExample() {

    Row(
        modifier = Modifier.fillMaxSize()
            .horizontalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        repeat(10) { index ->

            Card(
                modifier = Modifier.padding(8.dp)
                    .size(120.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Item ${index}")

                }

            }
        }
    }
}*/

/*@Composable
fun NestedScrollExample() {
    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = Color.Black)
    ) {
        Text(
            text = "HEADER SECTION",
            fontSize = 22.sp,
            modifier = Modifier.padding(16.dp),
            color = Color.White
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .height(200.dp)   //IMPORTANT FOR NESTED SCROLL

        ) {
            items(10) { index ->

                Card(
                    modifier = Modifier.fillMaxWidth().padding(4.dp)
                ) {
                    Text(
                        text = "Lazy Item ${index + 1}",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
            }
        }

            Text(
                text = "FOOTER SECTION",
                fontSize = 22.sp,
                modifier = Modifier.padding(16.dp),
                color = Color.White
            )
        }
    }*/
/*@Composable
fun SpinnerExample(){
    val items = listOf("CSE", "ECE", "ME", "CE")
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(items[0])}

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp)

    ){
        OutlinedTextField(
            value = selectedItem,
            onValueChange = { },
            readOnly = true,
            label = { Text("Select Department") },
            trailingIcon = {
                Icon(
                      imageVector = Icons.Default.ArrowDropDown,
                      contentDescription = "Dropdown",
                      modifier = Modifier.clickable { expanded = true }
                )
            },
            modifier = Modifier.fillMaxWidth().clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ){
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        selectedItem = item
                        expanded = false
                    }
                )
            }
        }

    }
}*/

@Composable
fun CircularProgressExample(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(
            modifier = Modifier.size(60.dp),
            color = Color.Blue,
            strokeWidth = 6.dp
        )


    }
}

@Composable
fun LinearProgressExample(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center

    ){
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth().height(6.dp),
            color = Color.Black,
        )
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CSE225AndroidTheme {
      // ListExample()
       // HorizontalList()//
        //SimpleScrollExample()//
       // HorizontalScrollExample()//
        //NestedScrollExample()//
        //SpinnerExample()//
        CircularProgressExample()
        LinearProgressExample()
    }
}
