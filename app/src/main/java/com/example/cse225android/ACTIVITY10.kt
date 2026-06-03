package com.example.cse225android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.automirrored.outlined.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.cse225android.ui.theme.CSE225AndroidTheme
import kotlinx.coroutines.launch

class ACTIVITY10 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CSE225AndroidTheme { MainScreen() } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var route by remember { mutableStateOf("Home") }

    val navItems = listOf(
        "Home" to (Icons.Default.Home to Icons.Outlined.Home),
        "Analytics" to (Icons.AutoMirrored.Filled.TrendingUp to Icons.AutoMirrored.Outlined.TrendingUp),
        "Tasks" to (Icons.AutoMirrored.Filled.List to Icons.AutoMirrored.Outlined.List),
        "Messages" to (Icons.Default.Email to Icons.Outlined.Email),
        "Settings" to (Icons.Default.Settings to Icons.Outlined.Settings)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(Modifier.width(300.dp)) {
                Box(Modifier.fillMaxWidth().height(200.dp).background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer))).padding(24.dp), contentAlignment = Alignment.BottomStart) {
                    Column {
                        Surface(Modifier.size(64.dp), CircleShape, MaterialTheme.colorScheme.surface.copy(0.9f)) {
                            Icon(Icons.Default.Person, null, Modifier.padding(12.dp), MaterialTheme.colorScheme.primary)
                        }
                        Text("CSE 225 Android", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("dev.android@cse225.edu", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(0.8f))
                    }
                }
                navItems.forEach { (label, icons) ->
                    NavigationDrawerItem(
                        icon = { Icon(if (route == label) icons.first else icons.second, null) },
                        label = { Text(label, fontWeight = FontWeight.Bold) },
                        selected = route == label,
                        onClick = { route = label; scope.launch { drawerState.close() } },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                LargeTopAppBar(
                    title = { Text(route, fontWeight = FontWeight.Black, letterSpacing = (-1).sp) },
                    navigationIcon = { IconButton({ scope.launch { drawerState.open() } }) { Icon(Icons.Default.Menu, null) } },
                    actions = {
                        IconButton({}) { Icon(Icons.Default.Search, null) }
                        IconButton({}) { BadgedBox({ Badge { Text("5") } }) { Icon(Icons.Default.Notifications, null) } }
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            floatingActionButton = {
                if (route == "Home" || route == "Tasks") {
                    ExtendedFloatingActionButton(onClick = { }, icon = { Icon(Icons.Default.Add, null) }, text = { Text("New Action") })
                }
            }
        ) { p ->
            Box(Modifier.padding(p)) {
                AnimatedContent(route, label = "") { r ->
                    when (r) {
                        "Home" -> HomePage()
                        "Analytics" -> GenericListPage("Analytics")
                        "Tasks" -> GenericListPage("Tasks")
                        "Messages" -> GenericListPage("Messages")
                        "Settings" -> GenericListPage("Settings")
                        else -> PlaceholderPage(r)
                    }
                }
            }
        }
    }
}

@Composable
fun HomePage() {
    val tabs = listOf("Overview", "Recent", "Updates")
    val pagerState = rememberPagerState { tabs.size }
    val scope = rememberCoroutineScope()

    Column {
        TabRow(pagerState.currentPage) {
            tabs.forEachIndexed { i, t ->
                Tab(pagerState.currentPage == i, { scope.launch { pagerState.animateScrollToPage(i) } }, text = { Text(t, fontWeight = FontWeight.Bold) })
            }
        }
        HorizontalPager(pagerState) { _ ->
            LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item { Text("Dashboard Summary", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(4) { i ->
                            Card(Modifier.size(140.dp, 80.dp), RoundedCornerShape(16.dp), CardDefaults.cardColors(if (i % 2 == 0) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer)) {
                                Box(Modifier.fillMaxSize(), Alignment.Center) { Text("Metric $i", fontWeight = FontWeight.Black) }
                            }
                        }
                    }
                }
                items(10) { i ->
                    ElevatedCard(Modifier.fillMaxWidth(), RoundedCornerShape(16.dp)) {
                        ListItem(
                            headlineContent = { Text("Feed Item $i", fontWeight = FontWeight.Bold) },
                            supportingContent = { Text("Description for item $i") },
                            leadingContent = { Surface(Modifier.size(40.dp), CircleShape, MaterialTheme.colorScheme.tertiaryContainer) { Icon(Icons.Default.Star, null, Modifier.padding(8.dp)) } }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GenericListPage(title: String) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item { Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black) }
        items(12) { i ->
            ListItem(
                headlineContent = { Text("$title Item $i", fontWeight = FontWeight.Bold) },
                supportingContent = { Text("Details for $title entry $i") },
                leadingContent = { Icon(Icons.Default.Info, null) },
                modifier = Modifier.clickable { }
            )
            HorizontalDivider()
        }
    }
}

@Composable
fun PlaceholderPage(n: String) = Box(Modifier.fillMaxSize(), Alignment.Center) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.Construction, null, Modifier.size(80.dp), MaterialTheme.colorScheme.primary.copy(0.5f))
        Text(n, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
        Text("Coming Soon")
    }
}
