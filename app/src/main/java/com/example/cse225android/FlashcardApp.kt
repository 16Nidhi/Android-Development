package com.example.cse225android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cse225android.ui.theme.CSE225AndroidTheme
import kotlinx.coroutines.launch

class FlashcardApp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CSE225AndroidTheme {
                FlashcardMainScreen()
            }
        }
    }
}
data class Subject(val name: String, val info: List<String>, val quiz: List<String>)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardMainScreen() {
    val subjects = listOf(
        Subject("Android", listOf("Activity", "Intent", "Compose"), listOf("What is Composable?", "What is Context?")),
        Subject("Kotlin", listOf("Val vs Var", "Null Safety", "Coroutines"), listOf("Explain Suspend", "Explain Data Class"))
    )

    var currentSubject by remember { mutableStateOf(subjects[0]) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Study Subjects", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                subjects.forEach { subject ->
                    NavigationDrawerItem(
                        label = { Text(subject.name) },
                        selected = subject == currentSubject,
                        onClick = {
                            currentSubject = subject
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(currentSubject.name, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = null)
                        }
                    }
                )
            }
        ) { padding ->
            val horizontalPagerState = rememberPagerState(pageCount = { 2 })
            
            Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                TabRow(selectedTabIndex = horizontalPagerState.currentPage) {
                    Tab(
                        selected = horizontalPagerState.currentPage == 0,
                        onClick = { scope.launch { horizontalPagerState.animateScrollToPage(0) } },
                        text = { Text("Learn") }
                    )
                    Tab(
                        selected = horizontalPagerState.currentPage == 1,
                        onClick = { scope.launch { horizontalPagerState.animateScrollToPage(1) } },
                        text = { Text("Test") }
                    )
                }

                HorizontalPager(state = horizontalPagerState, modifier = Modifier.weight(1f)) { page ->
                    if (page == 0) {
                        Column(Modifier.fillMaxSize().padding(16.dp)) {
                            currentSubject.info.forEach { item ->
                                Card(Modifier.fillMaxWidth().padding(vertical = 8.dp), elevation = CardDefaults.cardElevation(4.dp)) {
                                    Text(item, Modifier.padding(24.dp), fontSize = 18.sp)
                                }
                            }
                        }
                    } else {
                        val verticalPagerState = rememberPagerState(pageCount = { currentSubject.quiz.size })
                        VerticalPager(state = verticalPagerState, modifier = Modifier.fillMaxSize()) { qIdx ->
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Card(Modifier.padding(24.dp).fillMaxWidth().height(250.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        Text(currentSubject.quiz[qIdx], fontSize = 22.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
