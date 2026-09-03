package com.example.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.ui.theme.WarmGold

@Composable
fun ProfileScreen(
  onLogout: () -> Unit,
  onNavigateToConnect: () -> Unit
) {
  val auth = remember { FirebaseAuth.getInstance() }
  val db = remember { FirebaseFirestore.getInstance() }

  var firstName by remember { mutableStateOf("Loading...") }
  var email by remember { mutableStateOf(auth.currentUser?.email ?: "") }
  var bio by remember { mutableStateOf("") }
  var siblingName by remember { mutableStateOf<String?>(null) }
  var siblingRole by remember { mutableStateOf<String?>(null) }
  var isLoadingConnection by remember { mutableStateOf(true) }
  var showConnectionDialog by remember { mutableStateOf(false) }
  var showPremiumDialog by remember { mutableStateOf(false) }
  var notificationsEnabled by remember { mutableStateOf(true) }

  LaunchedEffect(Unit) {
    val currentUser = auth.currentUser
    val uid = currentUser?.uid
    email = currentUser?.email ?: ""

    if (uid == null) {
      firstName = "Guest"
      isLoadingConnection = false
      return@LaunchedEffect
    }

    db.collection("users").document(uid).get().addOnSuccessListener { document ->
      if (document.exists()) {
        firstName = document.getString("firstName") ?: "User"
        bio = document.getString("bio") ?: ""
        email = document.getString("email") ?: currentUser.email.orEmpty()
      } else {
        firstName = "User"
      }
    }

    fun loadOtherUser(connectionDocument: com.google.firebase.firestore.DocumentSnapshot) {
      val user1 = connectionDocument.getString("user1Uid")
      val user2 = connectionDocument.getString("user2Uid")
      val otherUid = if (user1 == uid) user2 else user1

      if (otherUid.isNullOrBlank()) {
        isLoadingConnection = false
        return
      }

      db.collection("users").document(otherUid).get().addOnSuccessListener { siblingDocument ->
        if (siblingDocument.exists()) {
          siblingName = siblingDocument.getString("firstName") ?: "Sibling"
          val gender = siblingDocument.getString("gender")
          siblingRole = when (gender?.lowercase()) {
            "sister" -> "Sister"
            "brother" -> "Brother"
            else -> "Sibling"
          }
        }
        isLoadingConnection = false
      }.addOnFailureListener {
        isLoadingConnection = false
      }
    }

    db.collection("siblingConnections")
      .whereEqualTo("user1Uid", uid)
      .limit(1)
      .get()
      .addOnSuccessListener { snapshot ->
        if (snapshot.documents.isNotEmpty()) {
          loadOtherUser(snapshot.documents.first())
        } else {
          db.collection("siblingConnections")
            .whereEqualTo("user2Uid", uid)
            .limit(1)
            .get()
            .addOnSuccessListener { secondSnapshot ->
              if (secondSnapshot.documents.isNotEmpty()) {
                loadOtherUser(secondSnapshot.documents.first())
              } else {
                isLoadingConnection = false
              }
            }
            .addOnFailureListener { isLoadingConnection = false }
        }
      }
      .addOnFailureListener { isLoadingConnection = false }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(bottom = 100.dp)
    ) {
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(90.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = firstName.take(1).uppercase(),
              style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.primary
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = firstName,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
          )
          Text(
            text = email,
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
          )

          if (bio.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "“$bio”",
              style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
              ),
              modifier = Modifier.padding(horizontal = 24.dp)
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
            )
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                  modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  when {
                    isLoadingConnection -> {
                      Text("Checking sibling connection...", fontWeight = FontWeight.Bold)
                    }
                    siblingName != null -> {
                      Text("Connected with $siblingName", fontWeight = FontWeight.Bold)
                      Text(
                        "${siblingRole ?: "Sibling"} • Private Space Active",
                        style = MaterialTheme.typography.bodySmall.copy(
                          color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                      )
                    }
                    else -> {
                      Text("No sibling connected", fontWeight = FontWeight.Bold)
                      Text(
                        "Connect your brother or sister to create a private space",
                        style = MaterialTheme.typography.bodySmall.copy(
                          color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                      )
                    }
                  }
                }
              }

              if (!isLoadingConnection) {
                TextButton(onClick = {
                  if (siblingName != null) showConnectionDialog = true else onNavigateToConnect()
                }) {
                  Text(if (siblingName != null) "Manage" else "Connect")
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            onClick = { showPremiumDialog = true }
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.Star, contentDescription = null, tint = WarmGold, modifier = Modifier.size(30.dp))
              Spacer(modifier = Modifier.width(14.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text("Brother & Sister Premium", fontWeight = FontWeight.Bold)
                Text(
                  "Unlimited HD photos, videos & backup for ₹19/mo",
                  style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
              }
              Icon(Icons.Default.CardMembership, contentDescription = null, tint = WarmGold)
            }
          }
        }
      }

      item {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
          Text(
            "PREFERENCES & SECURITY",
            style = MaterialTheme.typography.labelSmall.copy(
              color = MaterialTheme.colorScheme.outline,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            ),
            modifier = Modifier.padding(vertical = 8.dp)
          )

          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
          ) {
            Column {
              Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                  Spacer(modifier = Modifier.width(14.dp))
                  Text("Memory Notifications", fontWeight = FontWeight.Medium)
                }
                Switch(
                  checked = notificationsEnabled,
                  onCheckedChange = { notificationsEnabled = it },
                  colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
                )
              }

              HorizontalDivider()
              ProfileMenuItem(Icons.Default.Lock, "Privacy & Encryption", "Only you and your sibling can view memories")
              HorizontalDivider()
              ProfileMenuItem(Icons.Default.Security, "Security & Passcode Lock", "Protect the app with biometric unlock")
              HorizontalDivider()
              ProfileMenuItem(Icons.Default.CloudDone, "Cloud Backup & Export", "Download all photos and memories safely")
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(20.dp))
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
          Text(
            "SUPPORT & LEGAL",
            style = MaterialTheme.typography.labelSmall.copy(
              color = MaterialTheme.colorScheme.outline,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            ),
            modifier = Modifier.padding(vertical = 8.dp)
          )
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
          ) {
            Column {
              ProfileMenuItem(Icons.Default.HelpOutline, "Help & Family Guides")
              HorizontalDivider()
              ProfileMenuItem(Icons.Default.ReportProblem, "Report a Problem")
              HorizontalDivider()
              ProfileMenuItem(Icons.Default.VerifiedUser, "Terms of Service & Privacy Policy")
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(20.dp))
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
          OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(16.dp)
          ) {
            Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Log Out", fontWeight = FontWeight.SemiBold)
          }
        }
      }
    }

    if (showConnectionDialog && siblingName != null) {
      AlertDialog(
        onDismissRequest = { showConnectionDialog = false },
        title = { Text("Sibling Connection") },
        text = { Text("You are connected with $siblingName in a private sibling space.") },
        confirmButton = {
          Button(onClick = {
            showConnectionDialog = false
            onNavigateToConnect()
          }) { Text("Connection Settings") }
        },
        dismissButton = { TextButton(onClick = { showConnectionDialog = false }) { Text("Close") } }
      )
    }

    if (showPremiumDialog) {
      AlertDialog(
        onDismissRequest = { showPremiumDialog = false },
        title = { Text("Brother & Sister Premium ❤️") },
        text = {
          Text("Unlimited HD photos, videos and cloud backup. Launch pricing: ₹19/month or ₹199/year.")
        },
        confirmButton = { TextButton(onClick = { showPremiumDialog = false }) { Text("OK") } }
      )
    }
  }
}

@Composable
private fun ProfileMenuItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  subtitle: String? = null
) {
  Row(
    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
    Spacer(modifier = Modifier.width(14.dp))
    Column(modifier = Modifier.weight(1f)) {
      Text(title, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium))
      if (!subtitle.isNullOrBlank()) {
        Text(
          subtitle,
          style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
      }
    }
    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
  }
}
