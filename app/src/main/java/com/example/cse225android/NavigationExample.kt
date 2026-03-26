package com.example.cse225android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cse225android.ui.theme.CSE225AndroidTheme
import kotlinx.coroutines.launch

class NavigationExample : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CSE225AndroidTheme {
                MainContent()
            }
        }
    }
}

data class PageData(val title: String, val icon: ImageVector, val color: Color)

@Composable
fun MainContent() {
    val pages = listOf(
        PageData("Home", Icons.Default.Home, Color(0xFF6750A4)),
        PageData("Explore", Icons.Default.Explore, Color(0xFF006A60)),
        PageData("Profile", Icons.Default.Person, Color(0xFFB3261E))
    )
    
    // rememberPagerState is the key for swiping
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            // TabRow synced with pagerState
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                pages.forEachIndexed { index, page ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                // This makes the pager move when you click a tab
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(text = page.title) },
                        icon = { Icon(page.icon, contentDescription = null) }
                    )
                }
            }

            // HorizontalPager MUST have weight(1f) or fillMaxSize to be swipeable
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize() // Use fillMaxSize here
            ) { pageIndex ->
                // This is the content that appears on each page
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    PageItem(pages[pageIndex])
                }
            }
        }
    }
}

@Composable
fun PageItem(page: PageData) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.size(240.dp),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = page.color.copy(alpha = 0.12f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = page.icon,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                    tint = page.color
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = page.title,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = page.color
        )
        
        Text(
            text = "<- Swipe to change screens ->",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    CSE225AndroidTheme {
        MainContent()
    }
}
