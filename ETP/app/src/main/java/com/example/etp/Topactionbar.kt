package com.example.etp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.etp.ui.theme.ETPTheme
import kotlinx.coroutines.launch

class Topactionbar : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ETPTheme {
                MyScaffoldScreen()
                }
            }
        }
    }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScaffoldScreen() {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show()
                    })
                    {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,

                        )
                    }
                },
                title = {
                    Text(
                        text = "ScaffoldDemo"
                    )
                },
                actions = {
                    IconButton(onClick = {
                        Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show()
                    }){
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue
                )
            )
        }
    ){innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).padding(16.dp)
        ){
            Text("back icon")
            Text("home icon")
            
        }
    }

}

@Composable
fun SnackbarScaffold(){
    val snackbarHostState= remember { SnackbarHostState() }
    val scope= rememberCoroutineScope()
    val context=LocalContext.current
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {padding->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                scope.launch {
                    val result= snackbarHostState.showSnackbar(
                        message = "item deleted",
                        actionLabel = "undo",
                        duration = SnackbarDuration.Short
                    )
                    if(result== SnackbarResult.ActionPerformed){
                        Toast.makeText(context,"Item restored",Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
                Text(text = "Show Snackbar")
            }
        }
    }

}
@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ETPTheme {
        MyScaffoldScreen()
    }
}
