package com.example.cse225android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cse225android.ui.theme.CSE225AndroidTheme
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class DrawerNavigationExample : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CSE225AndroidTheme {
                DrawerAppContent()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerAppContent() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val menuItems = listOf(
        Pair("Home", Icons.Default.Home),
        Pair("Trending", Icons.Default.Star),
        Pair("Notifications", Icons.Default.Notifications),
        Pair("Settings", Icons.Default.Settings)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerTonalElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                            )
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Column {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp).clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f))
                                .padding(8.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "John Doe",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "john.doe@example.com",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                menuItems.forEach { (title, icon) ->
                    NavigationDrawerItem(
                        label = { Text(title, fontWeight = FontWeight.Medium) },
                        selected = title == "Home",
                        onClick = { scope.launch { drawerState.close() } },
                        icon = { Icon(icon, contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                Text(
                    "Advanced Navigation v1.0",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { 
                        Text(
                            "DISCOVER", 
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
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
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        ) { padding ->
            val pagerState = rememberPagerState(pageCount = { 5 })

            Box(
                modifier = Modifier.fillMaxSize().padding(padding)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                VerticalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    pageSpacing = 16.dp,
                    beyondViewportPageCount = 1
                ) { page ->
                    val pageOffset = (pagerState.currentPage - page).absoluteValue
                    val scale by animateFloatAsState(if (pageOffset == 0) 1f else 0.85f, label = "")
                    
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                alpha = scale
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        ModernPageContent(page)
                    }
                }
            }
        }
    }
}

@Composable
fun ModernPageContent(page: Int) {
    val colors = listOf(
        Color(0xFF6750A4), Color(0xFF006A60), Color(0xFFB3261E),
        Color(0xFF3F51B5), Color(0xFFE91E63)
    )
    val color = colors[page % colors.size]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1.2f)
                    .background(
                        brush = Brush.linearGradient(
                            listOf(color, color.copy(alpha = 0.7f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(120.dp),
                    tint = Color.White
                )
            }

            Column(
                modifier = Modifier.weight(1f).padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "FEATURED ITEM #$page",
                    style = MaterialTheme.typography.labelLarge,
                    color = color,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Explore Modern UI Components",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "This VerticalPager demonstrates advanced navigation concepts from your syllabus with smooth animations and professional styling.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { i ->
                        Box(
                            modifier = Modifier.size(8.dp).clip(CircleShape)
                                .background(if (i == page) color else Color.LightGray)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DrawerPreview() {
    CSE225AndroidTheme {
        DrawerAppContent()
    }
}
