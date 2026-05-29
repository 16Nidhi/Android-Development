package com.example.cse225android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cse225android.ui.theme.CSE225AndroidTheme
import kotlinx.coroutines.launch

class ACTIVITY10 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CSE225AndroidTheme {
                MainScreen()
            }
        }
    }
}


data class NavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    var selectedRoute by remember { mutableStateOf("Home") }

    val navItems = listOf(
        NavItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
        NavItem("Analytics", Icons.AutoMirrored.Filled.TrendingUp, Icons.AutoMirrored.Outlined.TrendingUp),
        NavItem("Tasks", Icons.AutoMirrored.Filled.List, Icons.AutoMirrored.Outlined.List, 4),
        NavItem("Messages", Icons.Filled.Email, Icons.Outlined.Email, 12),
        NavItem("Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(320.dp),
                drawerShape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp)
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Column {
                        Surface(
                            shape = CircleShape,
                            modifier = Modifier.size(72.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                            shadowElevation = 8.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "CSE 225 Android",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "dev.android@cse225.edu",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                navItems.forEach { item ->
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                if (selectedRoute == item.label) item.selectedIcon else item.unselectedIcon,
                                contentDescription = null
                            )
                        },
                        label = { Text(text = item.label, fontWeight = FontWeight.SemiBold) },
                        selected = selectedRoute == item.label,
                        onClick = {
                            selectedRoute = item.label
                            scope.launch { drawerState.close() }
                        },
                        badge = {
                            if (item.badgeCount > 0) {
                                Badge { Text(item.badgeCount.toString()) }
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                LargeTopAppBar(
                    title = {
                        Text(
                            selectedRoute,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-1).sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                        IconButton(onClick = { }) {
                            BadgedBox(badge = { Badge { Text("5") } }) {
                                Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                            }
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            floatingActionButton = {
                if (selectedRoute == "Home" || selectedRoute == "Tasks") {
                    ExtendedFloatingActionButton(
                        onClick = { },
                        icon = { Icon(Icons.Default.Add, contentDescription = null) },
                        text = { Text("New Action") },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                AnimatedContent(
                    targetState = selectedRoute,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "ContentTransition"
                ) { targetState ->
                    when (targetState) {
                        "Home" -> HomePage()
                        "Analytics" -> AnalyticsPage()
                        "Tasks" -> TasksPage()
                        "Messages" -> MessagesPage()
                        "Settings" -> SettingsPage()
                        else -> PlaceholderPage(targetState)
                    }
                }
            }
        }
    }
}

@Composable
fun HomePage() {
    val tabs = listOf("Overview", "Recent", "Updates")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = { tabPositions ->
                if (pagerState.currentPage < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(title, fontWeight = FontWeight.Bold) }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { _ ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        "Dashboard Summary",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(5) { i ->
                            Card(
                                modifier = Modifier.width(150.dp).height(100.dp),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (i % 2 == 0) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
                                )
                            ) {
                                Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                                    Text("Metric $i", fontWeight = FontWeight.Black)
                                }
                            }
                        }
                    }
                }

                items(10) { index ->
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.tertiaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.onTertiaryContainer)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Feed Item $index", fontWeight = FontWeight.Bold)
                                Text("This is a description for the item.", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnalyticsPage() {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Text("Analytics", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
        }
        items(5) { i ->
            ListItem(
                headlineContent = { Text("Data Point $i", fontWeight = FontWeight.Bold) },
                supportingContent = { Text("Analysis group $i details.") },
                trailingContent = { Text("+${(i+1)*5}%", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold) },
                leadingContent = { Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null) }
            )
            HorizontalDivider()
        }
    }
}

@Composable
fun TasksPage() {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(10) { i ->
            OutlinedCard(shape = RoundedCornerShape(12.dp)) {
                ListItem(
                    headlineContent = { Text("Task #$i", fontWeight = FontWeight.Bold) },
                    supportingContent = { Text("Due Tomorrow") },
                    leadingContent = { Checkbox(checked = i % 2 == 0, onCheckedChange = {}) }
                )
            }
        }
    }
}

@Composable
fun MessagesPage() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(15) { i ->
            ListItem(
                headlineContent = { Text("Contact $i", fontWeight = FontWeight.Bold) },
                supportingContent = { Text("Hey! How is Activity 10 going?", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                leadingContent = { 
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(48.dp)) {
                        Box(contentAlignment = Alignment.Center) { Text("${(65+i).toChar()}") }
                    }
                },
                modifier = Modifier.clickable { }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

@Composable
fun SettingsPage() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        val settings = listOf(
            Triple(Icons.Default.Palette, "Theme", "Light/Dark mode"),
            Triple(Icons.Default.Notifications, "Notifications", "Sounds and alerts"),
            Triple(Icons.Default.Security, "Privacy", "Manage your data"),
            Triple(Icons.Default.Language, "Language", "English"),
            Triple(Icons.Default.Info, "About", "Version 1.0.0")
        )
        items(settings) { (icon, title, desc) ->
            ListItem(
                headlineContent = { Text(title, fontWeight = FontWeight.Bold) },
                supportingContent = { Text(desc) },
                leadingContent = { Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                modifier = Modifier.clickable { }
            )
        }
    }
}

@Composable
fun PlaceholderPage(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Construction, contentDescription = null, modifier = Modifier.size(80.dp), tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            Text(name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Text("Coming Soon", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
