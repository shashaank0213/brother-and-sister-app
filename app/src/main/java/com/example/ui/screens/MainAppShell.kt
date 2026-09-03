package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.data.SampleData
import com.example.model.MemoryItem
import com.example.model.SiblingConnection
import com.example.model.UserProfile
import com.example.ui.components.AddMemoryDialog
import com.example.ui.screens.albums.AlbumsScreen
import com.example.ui.screens.chat.ChatScreen
import com.example.ui.screens.connection.ConnectSiblingScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.memories.MemoriesScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.theme.EditorialBorder
import com.example.ui.theme.EditorialEspresso
import com.example.ui.theme.EditorialMauve
import com.example.ui.theme.EditorialPlum
import com.example.ui.theme.EditorialRoseBlush
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

sealed class BottomNavItem(
  val title: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector
) {
  object Home : BottomNavItem("Home", Icons.Filled.Home, Icons.Outlined.Home)
  object Memories : BottomNavItem("Memories", Icons.Filled.PhotoLibrary, Icons.Outlined.PhotoLibrary)
  object Chat : BottomNavItem("Chat", Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline)
  object Albums : BottomNavItem("Albums", Icons.Filled.CollectionsBookmark, Icons.Outlined.CollectionsBookmark)
  object Profile : BottomNavItem("Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun MainAppShell(
  onLogout: () -> Unit
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  var showAddMemoryDialog by remember { mutableStateOf(false) }
  var showConnectScreen by remember { mutableStateOf(false) }
  var connectInitialTab by remember { mutableIntStateOf(0) }
  var connectionRefreshKey by remember { mutableIntStateOf(0) }
  var currentUserProfile by remember { mutableStateOf(UserProfile()) }
  var currentConnection by remember {
    mutableStateOf(
      SiblingConnection(
        siblingName = "Sibling",
        siblingRole = "Brother/Sister",
        sharedMemoryCount = 0,
        photoCount = 0,
        videoCount = 0
      )
    )
  }

  val memoriesList = remember { mutableStateListOf<MemoryItem>().apply { addAll(SampleData.sampleMemories) } }
  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()
  val auth = remember { FirebaseAuth.getInstance() }
  val db = remember { FirebaseFirestore.getInstance() }

  LaunchedEffect(connectionRefreshKey, auth.currentUser?.uid) {
    val uid = auth.currentUser?.uid ?: return@LaunchedEffect

    db.collection("users").document(uid).get().addOnSuccessListener { document ->
      if (document.exists()) {
        currentUserProfile = UserProfile(
          id = uid,
          firstName = document.getString("firstName") ?: "User",
          email = document.getString("email") ?: auth.currentUser?.email.orEmpty(),
          dateOfBirth = document.getString("dateOfBirth") ?: "",
          gender = document.getString("gender") ?: "",
          country = document.getString("country") ?: "",
          city = document.getString("city") ?: "",
          bio = document.getString("bio") ?: "",
          interests = (document.get("interests") as? List<*>)?.filterIsInstance<String>() ?: emptyList()
        )
      }
    }

    currentConnection = SiblingConnection(
      siblingName = "Sibling",
      siblingRole = "Brother/Sister",
      sharedMemoryCount = 0,
      photoCount = 0,
      videoCount = 0
    )

    fun loadConnection(document: com.google.firebase.firestore.DocumentSnapshot) {
      val user1 = document.getString("user1Uid")
      val user2 = document.getString("user2Uid")
      val otherUid = if (user1 == uid) user2 else user1
      if (otherUid.isNullOrBlank()) return

      db.collection("users").document(otherUid).get().addOnSuccessListener { sibling ->
        if (sibling.exists()) {
          val gender = sibling.getString("gender")?.lowercase()
          val role = when (gender) {
            "sister" -> "Sister"
            "brother" -> "Brother"
            else -> "Sibling"
          }
          val name = sibling.getString("firstName") ?: "Sibling"
          currentConnection = SiblingConnection(
            connectionId = document.id,
            siblingName = name,
            siblingRole = role,
            sharedMemoryCount = 0,
            photoCount = 0,
            videoCount = 0
          )
        }
      }
    }

    db.collection("siblingConnections")
      .whereEqualTo("user1Uid", uid)
      .limit(1)
      .get()
      .addOnSuccessListener { first ->
        if (first.documents.isNotEmpty()) {
          loadConnection(first.documents.first())
        } else {
          db.collection("siblingConnections")
            .whereEqualTo("user2Uid", uid)
            .limit(1)
            .get()
            .addOnSuccessListener { second ->
              if (second.documents.isNotEmpty()) loadConnection(second.documents.first())
            }
        }
      }
  }

  val navItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Memories,
    BottomNavItem.Chat,
    BottomNavItem.Albums,
    BottomNavItem.Profile
  )

  if (showConnectScreen) {
    ConnectSiblingScreen(
      initialTab = connectInitialTab,
      onBack = { showConnectScreen = false },
      onConnectedSuccess = {
        showConnectScreen = false
        connectionRefreshKey++
        coroutineScope.launch {
          snackbarHostState.showSnackbar("Brother/Sister connection activated ❤️")
        }
      }
    )
    return
  }

  Scaffold(
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    bottomBar = {
      Column {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(EditorialBorder)
        )
        NavigationBar(
          containerColor = androidx.compose.ui.graphics.Color.White,
          tonalElevation = 0.dp
        ) {
          navItems.forEachIndexed { index, item ->
            val isSelected = selectedTab == index
            NavigationBarItem(
              selected = isSelected,
              onClick = { selectedTab = index },
              icon = {
                Icon(
                  imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                  contentDescription = item.title
                )
              },
              label = {
                Text(
                  item.title,
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Medium
                  )
                )
              },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = EditorialPlum,
                selectedTextColor = EditorialPlum,
                unselectedIconColor = EditorialMauve.copy(alpha = 0.65f),
                unselectedTextColor = EditorialMauve.copy(alpha = 0.7f),
                indicatorColor = EditorialRoseBlush
              )
            )
          }
        }
      }
    },
    floatingActionButton = {
      if (selectedTab in listOf(0, 1, 3)) {
        FloatingActionButton(
          onClick = { showAddMemoryDialog = true },
          containerColor = EditorialEspresso,
          contentColor = androidx.compose.ui.graphics.Color.White,
          shape = CircleShape
        ) {
          Icon(Icons.Default.Add, contentDescription = "Add Memory", modifier = Modifier.size(26.dp))
        }
      }
    }
  ) { innerPadding ->
    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
      when (selectedTab) {
        0 -> HomeScreen(
          user = currentUserProfile,
          connection = currentConnection,
          memories = memoriesList,
          onAddMemoryClick = { showAddMemoryDialog = true },
          onMessageClick = { selectedTab = 2 },
          onConnectSiblingClick = {
            connectInitialTab = 0
            showConnectScreen = true
          },
          onViewAllMemories = { selectedTab = 1 },
          onNotificationsClick = {
            connectInitialTab = 1
            showConnectScreen = true
          }
        )
        1 -> MemoriesScreen(memoriesList = memoriesList, onAddMemoryClick = { showAddMemoryDialog = true })
        2 -> ChatScreen(siblingName = currentConnection.siblingName)
        3 -> AlbumsScreen()
        4 -> ProfileScreen(
          onLogout = onLogout,
          onNavigateToConnect = {
            connectInitialTab = 0
            showConnectScreen = true
          }
        )
      }
    }

    if (showAddMemoryDialog) {
      AddMemoryDialog(
        onDismiss = { showAddMemoryDialog = false },
        onSaveMemory = { newMemory ->
          memoriesList.add(0, newMemory)
          coroutineScope.launch { snackbarHostState.showSnackbar("Memory saved to private space ❤️") }
        }
      )
    }
  }
}
