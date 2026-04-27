package com.example.cse225android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

// Custom Color Palette
private val AppLightColors = lightColorScheme(
    primary = Color(0xFF006782),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBBE9FF),
    onPrimaryContainer = Color(0xFF001F29),
    secondary = Color(0xFF4C626B),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCFE6F1),
    onSecondaryContainer = Color(0xFF071E26),
    background = Color(0xFFF1F4F9),
    surface = Color(0xFFF1F4F9),
    onBackground = Color(0xFF191C1E),
    onSurface = Color(0xFF191C1E),
    surfaceVariant = Color(0xFFDCE4E9),
    onSurfaceVariant = Color(0xFF40484C)
)

class NavigationDemoApp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = AppLightColors) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScreen() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedScreen by remember { mutableStateOf("Home") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Learning App",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(modifier = Modifier.height(12.dp))

                NavigationDrawerItem(
                    label = { Text("Home") },
                    selected = selectedScreen == "Home",
                    onClick = {
                        selectedScreen = "Home"
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text("Courses") },
                    selected = selectedScreen == "Courses",
                    onClick = {
                        selectedScreen = "Courses"
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.Book, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text("Profile") },
                    selected = selectedScreen == "Profile",
                    onClick = {
                        selectedScreen = "Profile"
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(selectedScreen, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                when (selectedScreen) {
                    "Home" -> HomeScreen()
                    "Courses" -> CoursePager()
                    "Profile" -> ProfileScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier.size(80.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Dashboard, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(40.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Welcome Back!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Your dashboard is ready.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatusCard("Courses", "4", Icons.Default.Book, Modifier.weight(1f))
            StatusCard("Tasks", "12", Icons.AutoMirrored.Filled.List, Modifier.weight(1f))
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Overall Progress", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { 0.65f },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                )
                Text("65% of monthly goal", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}

@Composable
fun StatusCard(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CoursePager() {
    val courses = listOf(
        CourseData("Android Development", "Learn Kotlin and Compose", Color(0xFF006782)),
        CourseData("Web Development", "Master HTML, CSS, and JS", Color(0xFF8B4100)),
        CourseData("AI & ML", "Explore Data Science", Color(0xFF4B6200))
    )
    val pagerState = rememberPagerState(pageCount = { courses.size })

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 48.dp),
            pageSpacing = 16.dp,
            modifier = Modifier.weight(1f)
        ) { page ->
            val course = courses[page]
            Card(
                modifier = Modifier.fillMaxWidth().height(380.dp).padding(vertical = 32.dp),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.School, null, modifier = Modifier.size(64.dp), tint = course.color)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = course.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = course.color)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = course.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = course.color)) {
                        Text("View Course")
                    }
                }
            }
        }
    }
}

data class CourseData(val title: String, val description: String, val color: Color)

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(100.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("John Doe", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text("john.doe@example.com", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        ProfileOption(Icons.Default.Settings, "Account Settings")
        ProfileOption(Icons.AutoMirrored.Filled.Help, "Help & Support")
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(onClick = { }, modifier = Modifier.fillMaxWidth()) {
            Text("Edit Profile")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
            Icon(Icons.AutoMirrored.Filled.Logout, null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout")
        }
    }
}

@Composable
fun ProfileOption(icon: ImageVector, label: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun NavigationScreenPreview() {
    MaterialTheme(colorScheme = AppLightColors) {
        NavigationScreen()
    }
}
