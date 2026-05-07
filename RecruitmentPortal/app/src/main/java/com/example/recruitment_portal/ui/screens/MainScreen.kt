package com.example.recruitment_portal.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recruitment_portal.Job
import com.example.recruitment_portal.ui.components.JobItem
import com.example.recruitment_portal.ui.theme.Saffron
import com.example.recruitment_portal.ui.theme.Green
import kotlinx.coroutines.delay

@Composable
fun MainScreen() {
    val jobs = remember {
        mutableStateListOf(
            Job("Software Developer", "Develop and maintain web applications.", "21-30", "IT Department", "01 Aug 2026", "30 Aug 2026"),
            Job("System Administrator", "Manage servers and network infrastructure.", "22-35", "Infrastructure", "05 Aug 2026", "25 Aug 2026"),
            Job("Data Analyst", "Analyze recruitment data and reports.", "20-28", "Analytics", "10 Aug 2026", "28 Aug 2026"),
            Job("HR Specialist", "Manage recruitment processes and interviews.", "24-40", "Human Resources", "15 Aug 2026", "05 Sep 2026"),
            Job("Project Manager", "Oversee government portal projects.", "30-45", "Management", "20 Aug 2026", "15 Sep 2026"),
            Job("Security Officer", "Ensure security of government premises.", "21-35", "Security", "25 Aug 2026", "20 Sep 2026")
        )
    }

    Scaffold(
        bottomBar = { BottomNavigationBar() }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item { HeaderSection() }
            item { SearchSection() }
            item { FeatureHighlightSection() }
            item { QuickAccessSection() }
            item {
                Text(
                    text = "Latest Recruitments",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 12.dp)
                )
            }
            items(jobs) { job ->
                JobItem(job = job)
            }
        }
    }
}

@Composable
fun HeaderSection() {
    var titleText by remember { mutableStateOf("") }
    val fullText = "Recruitment Portal"

    LaunchedEffect(Unit) {
        for (i in 1..fullText.length) {
            titleText = fullText.substring(0, i)
            delay(120)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp, start = 24.dp, end = 24.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val annotatedTitle = buildAnnotatedString {
            titleText.forEachIndexed { index, char ->
                val color = if (index < 11) Saffron else if (index > 11) Green else MaterialTheme.colorScheme.onBackground
                withStyle(style = SpanStyle(color = color)) {
                    append(char)
                }
            }
        }

        Text(
            text = annotatedTitle,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = { /* Login */ },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
        ) {
            Text("Login", color = Color.White)
        }
    }
}

@Composable
fun SearchSection() {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder().copy(width = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Search job opportunities...",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun FeatureHighlightSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(24.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.padding(24.dp)) {
            Column {
                Text(
                    text = "Unlock Your Future",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Apply to 500+ Government job openings across India.",
                    color = Color(0xFFE0E7FF),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.width(200.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Explore Now", color = MaterialTheme.colorScheme.primary)
                }
            }
            Icon(
                imageVector = Icons.Default.Work,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.25f),
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterEnd)
            )
        }
    }
}

@Composable
fun QuickAccessSection() {
    Column {
        Text(
            text = "Quick Access",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(start = 24.dp, bottom = 16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            QuickServiceItem("Jobs", Icons.Default.Work)
            QuickServiceItem("Apply", Icons.Default.Home)
            QuickServiceItem("Admit", Icons.Default.Person)
            QuickServiceItem("Results", Icons.Default.Settings)
        }
    }
}

@Composable
fun QuickServiceItem(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { }
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = true,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Work, contentDescription = "Jobs") },
            label = { Text("Jobs") },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = false,
            onClick = { }
        )
    }
}
