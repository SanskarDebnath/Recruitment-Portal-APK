package com.example.recruitment_portal.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.roundToInt
import androidx.compose.ui.unit.IntOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recruitment_portal.*
import com.example.recruitment_portal.ui.components.JobItem
import com.example.recruitment_portal.ui.theme.Saffron
import com.example.recruitment_portal.ui.theme.Green
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val jobs = remember {
        mutableStateListOf(
            Job("Software Developer", "Develop and maintain web applications.", "21-30", "IT Department", "01 Aug 2026", "30 Aug 2026"),
            Job("System Administrator", "Manage servers and network infrastructure.", "22-35", "Infrastructure", "05 Aug 2026", "25 Aug 2026"),
            Job("Data Analyst", "Analyze recruitment data and reports.", "20-28", "Analytics", "10 Aug 2026", "28 Aug 2026"),
            Job("HR Specialist", "Manage recruitment processes and interviews.", "24-40", "Human Resources", "15 Aug 2026", "05 Sep 2026"),
            Job("Project Manager", "Oversee government portal projects.", "30-45", "Management", "20 Aug 2026", "15 Sep 2026"),
            Job("Security Officer", "Ensure security of government premises.", "21-35", "Security", "25 Aug 2026", "20 Sep 2026"),
            Job("Medical Officer", "Provide medical care at government hospitals.", "25-45", "Health", "01 Sep 2026", "25 Sep 2026"),
            Job("Clerk", "Manage administrative tasks.", "18-27", "Administration", "10 Sep 2026", "30 Sep 2026"),
            Job("Junior Engineer", "Assist in civil engineering projects.", "21-30", "Engineering", "15 Sep 2026", "05 Oct 2026"),
            Job("Accountant", "Manage financial records.", "22-35", "Finance", "20 Sep 2026", "10 Oct 2026")
        )
    }

    var isLoading by remember { mutableStateOf(true) }
    var showLoginDialog by remember { mutableStateOf(false) }
    var isLoggedIn by remember { mutableStateOf(SessionManager.isLoggedIn) }
    var selectedTab by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    var jobForDialog by remember { mutableStateOf<Job?>(null) }

    LaunchedEffect(Unit) {
        delay(2000)
        isLoading = false
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = { 
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { index -> 
                    context.vibrate(30)
                    selectedTab = index
                    if (index == 1) { // Jobs tab
                        scope.launch { listState.animateScrollToItem(4) }
                    }
                    when (index) {
                        2 -> { // Settings
                            context.startActivity(Intent(context, SettingsActivity::class.java))
                        }
                        3 -> { // Profile
                            context.startActivity(Intent(context, ProfileActivity::class.java))
                        }
                    }
                }
            ) 
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                item { 
                    HeaderSection(
                        isLoggedIn = isLoggedIn,
                        onLoginClick = { showLoginDialog = true }
                    ) 
                }
                if (isLoggedIn) {
                    item { UserGreetingSection() }
                }
                item { SearchSection() }
                if (!isLoggedIn) {
                    item { WhyRegisterSection() }
                }
                item { 
                    FeatureHighlightSection(
                        onExploreClick = {
                            scope.launch { listState.animateScrollToItem(4) }
                        }
                    ) 
                }
                item { NewsSection() }
                item { 
                    QuickAccessSection(
                        onItemClick = { label ->
                            when (label) {
                                "Apply" -> {
                                    if (isLoggedIn) {
                                        context.startActivity(Intent(context, ApplyOnlineActivity::class.java))
                                    } else {
                                        scope.launch { snackbarHostState.showSnackbar("Login required to access portal") }
                                        showLoginDialog = true
                                    }
                                }
                                "Admit" -> context.startActivity(Intent(context, AdmitCardActivity::class.java))
                                "Results" -> context.startActivity(Intent(context, ResultsActivity::class.java))
                                "Jobs" -> { /* Already on home/jobs */ }
                            }
                        }
                    ) 
                }
                if (isLoggedIn) {
                    item { RecommendedJobsSection() }
                }
                item { RecruitersSection() }
                item { SuccessStoriesSection() }
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
                    JobItem(
                        job = job,
                        onApplyClick = {
                            if (isLoggedIn) {
                                context.startActivity(Intent(context, ApplyOnlineActivity::class.java))
                            } else {
                                scope.launch { snackbarHostState.showSnackbar("Login required") }
                                showLoginDialog = true
                            }
                        },
                        onDetailsClick = {
                            scope.launch { snackbarHostState.showSnackbar("Opening details for ${job.title}") }
                        },
                        onInfoClick = {
                            jobForDialog = job
                        }
                    )
                }
            }

            if (jobForDialog != null) {
                val jobInfo = jobForDialog!!
                AlertDialog(
                    onDismissRequest = { jobForDialog = null },
                    title = { Text(jobInfo.title, fontWeight = FontWeight.Bold) },
                    text = { 
                        Column {
                            Text("Department: ${jobInfo.department}")
                            Text("Age Limit: ${jobInfo.age}")
                            Text("Application Period: ${jobInfo.startDate} to ${jobInfo.endDate}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Description:\n${jobInfo.description}")
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { jobForDialog = null }) {
                            Text("Close")
                        }
                    }
                )
            }

            if (isLoading) {
                LoadingOverlay()
            }

            if (showLoginDialog) {
                LoginDialog(
                    onDismiss = { showLoginDialog = false },
                    onLoginSuccess = {
                        isLoggedIn = true
                        showLoginDialog = false
                        isLoading = true
                        // Simulate post-login loading
                        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                            isLoading = false
                            context.startActivity(Intent(context, DashboardActivity::class.java))
                        }, 3000)
                    }
                )
            }

            // Floating Action Button for scrolling up
            if (listState.firstVisibleItemIndex > 0) {
                FloatingActionButton(
                    onClick = {
                        scope.launch { listState.animateScrollToItem(0) }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 110.dp, end = 24.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.ArrowUpward, contentDescription = "Scroll to Top")
                }
            }
        }
    }
}

@Composable
fun LoadingOverlay() {
    var progress by remember { mutableFloatStateOf(0f) }
    
    LaunchedEffect(Unit) {
        val duration = 2000L
        val steps = 100
        for (i in 1..steps) {
            progress = i / steps.toFloat()
            delay(duration / steps)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .width(200.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Saffron,
                trackColor = Color.LightGray,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginDialog(onDismiss: () -> Unit, onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sign In",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { 
                    context.vibrate(30)
                    if (email == "student@gmail.com" && password == "1234") {
                        SessionManager.isLoggedIn = true
                        onLoginSuccess()
                    } else {
                        Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = { }) {
                Text("Don't have an account? Sign up")
            }
        }
    }
}

@Composable
fun HeaderSection(isLoggedIn: Boolean, onLoginClick: () -> Unit) {
    val context = LocalContext.current
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

        if (!isLoggedIn) {
            Button(
                onClick = {
                    context.vibrate(30)
                    onLoginClick()
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
            ) {
                Text("Login", color = Color.White)
            }
        } else {
            IconButton(onClick = {
                context.vibrate(30)
                context.startActivity(Intent(context, DashboardActivity::class.java))
            }) {
                Icon(
                    imageVector = Icons.Default.Dashboard,
                    contentDescription = "Dashboard",
                    tint = Color(0xFFFF5722)
                )
            }
        }
    }
}

@Composable
fun SearchSection() {
    var searchText by remember { mutableStateOf("") }
    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        placeholder = { Text("Search job opportunities...", fontSize = 15.sp) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFE2E8F0),
            focusedBorderColor = MaterialTheme.colorScheme.primary
        ),
        singleLine = true
    )
}

@Composable
fun FeatureHighlightSection(onExploreClick: () -> Unit) {
    val context = LocalContext.current
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
                    onClick = { 
                        context.vibrate(30)
                        onExploreClick()
                    },
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
fun QuickAccessSection(onItemClick: (String) -> Unit) {
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
            val ctx = LocalContext.current
            QuickServiceItem("Jobs", Icons.Default.Work) { 
                ctx.vibrate(30)
                onItemClick("Jobs") 
            }
            QuickServiceItem("Apply", Icons.Default.Home) { 
                ctx.vibrate(30)
                onItemClick("Apply") 
            }
            QuickServiceItem("Admit", Icons.Default.Person) { 
                ctx.vibrate(30)
                onItemClick("Admit") 
            }
            QuickServiceItem("Results", Icons.Default.Settings) { 
                ctx.vibrate(30)
                onItemClick("Results") 
            }
        }
    }
}

@Composable
fun NewsSection() {
    val news = listOf(
        "Admit Card for Civil Services Exam 2026 released.",
        "New 10,000+ vacancies announced in Railway Dept.",
        "Last date for Banking recruitment extended to 15th Aug.",
        "Results for State Engineering Services out now."
    )

    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Notifications, contentDescription = null, tint = Saffron, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Featured News",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                news.forEach { item ->
                    Row(modifier = Modifier.padding(vertical = 6.dp)) {
                        Text(text = "•", color = Saffron, modifier = Modifier.padding(end = 8.dp))
                        Text(text = item, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
fun UserGreetingSection() {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
        Text(
            text = "Welcome back, Student!",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "You have 2 pending applications.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
fun WhyRegisterSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Saffron.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Why Register?",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Saffron
            )
            Spacer(modifier = Modifier.height(8.dp))
            val benefits = listOf(
                "Personalized Job Alerts",
                "One-click Application",
                "Real-time Tracking",
                "Free Mock Tests"
            )
            benefits.forEach { benefit ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Saffron, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = benefit, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun RecommendedJobsSection() {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(
            text = "Recommended for You",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
        )
        androidx.compose.foundation.lazy.LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val recommendations = listOf("Web Dev - NIC", "Analyst - RBI", "Engineer - ISRO")
            items(recommendations) { job ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.width(160.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Icon(Icons.Default.Recommend, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = job, style = MaterialTheme.typography.labelLarge)
                        Text(text = "Matches your profile", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun RecruitersSection() {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(
            text = "Top Recruiters",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
        )
        androidx.compose.foundation.lazy.LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val departments = listOf("UPSC", "SSC", "IBPS", "RRB", "DRDO")
            items(departments) { dept ->
                ElevatedCard(
                    modifier = Modifier.width(100.dp).height(100.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Saffron.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = dept[0].toString(), fontWeight = FontWeight.Bold, color = Saffron)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = dept, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
fun SuccessStoriesSection() {
    val stories = remember { mutableStateListOf(
        Pair("Rahul Sharma", "Placed in Indian Railways"),
        Pair("Anjali Gupta", "Cleared UPSC 2025"),
        Pair("Amit Patel", "Joined SBI as PO")
    ) }

    val swipeAnimatable = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(
            text = "Success Stories",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            stories.forEachIndexed { index, story ->
                val reversedIndex = stories.size - 1 - index
                val scale by animateFloatAsState(targetValue = 1f - (reversedIndex * 0.05f), animationSpec = spring(stiffness = Spring.StiffnessMediumLow))
                val baseOffsetY by animateDpAsState(targetValue = (reversedIndex * 14).dp, animationSpec = spring(stiffness = Spring.StiffnessMediumLow))
                val alpha by animateFloatAsState(targetValue = 1f - (reversedIndex * 0.3f), animationSpec = spring(stiffness = Spring.StiffnessMediumLow))
                
                val offsetX = if (reversedIndex == 0) swipeAnimatable.value else 0f
                val rotationZ = if (reversedIndex == 0) swipeAnimatable.value / 20f else 0f

                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(100.dp)
                        .offset { IntOffset(offsetX.roundToInt(), baseOffsetY.roundToPx()) }
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                            this.rotationZ = rotationZ
                        }
                        .pointerInput(Unit) {
                            if (reversedIndex == 0) {
                                detectHorizontalDragGestures(
                                    onDragEnd = {
                                        scope.launch {
                                            if (swipeAnimatable.value > 250f || swipeAnimatable.value < -250f) {
                                                val target = if (swipeAnimatable.value > 0) 1000f else -1000f
                                                swipeAnimatable.animateTo(target, tween(200))
                                                val topStory = stories.removeAt(0)
                                                stories.add(topStory)
                                                swipeAnimatable.snapTo(0f)
                                            } else {
                                                swipeAnimatable.animateTo(0f, spring(stiffness = Spring.StiffnessMediumLow))
                                            }
                                        }
                                    },
                                    onDragCancel = {
                                        scope.launch {
                                            swipeAnimatable.animateTo(0f, spring(stiffness = Spring.StiffnessMediumLow))
                                        }
                                    },
                                    onHorizontalDrag = { change, dragAmount ->
                                        change.consume()
                                        scope.launch {
                                            swipeAnimatable.snapTo(swipeAnimatable.value + dragAmount)
                                        }
                                    }
                                )
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFDCF2DA)),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (reversedIndex == 0) 8.dp else 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Green, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = story.first, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = story.second, style = MaterialTheme.typography.bodyMedium, color = Color.Gray, modifier = Modifier.padding(start = 32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun QuickServiceItem(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
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
fun BottomNavigationBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Work, contentDescription = "Jobs") },
            label = { Text("Jobs") },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) }
        )
    }
}
