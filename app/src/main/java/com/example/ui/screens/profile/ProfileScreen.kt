package com.example.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.DeleteForever
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.SampleData
import com.example.model.UserProfile
import com.example.ui.theme.HeartRed
import com.example.ui.theme.WarmGold

@Composable
fun ProfileScreen(
  user: UserProfile = SampleData.currentUser,
  onLogout: () -> Unit,
  onNavigateToConnect: () -> Unit
) {
  var showDisconnectDialog by remember { mutableStateOf(false) }
  var showDeleteDialog by remember { mutableStateOf(false) }
  var showPremiumDialog by remember { mutableStateOf(false) }
  var notificationsEnabled by remember { mutableStateOf(true) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(bottom = 100.dp)
    ) {
      // Profile Header
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
              .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Image(
              painter = painterResource(id = R.drawable.ic_app_logo),
              contentDescription = "Profile Photo",
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = user.firstName,
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          )

          Text(
            text = user.email,
            style = MaterialTheme.typography.bodySmall.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "“${user.bio}”",
            style = MaterialTheme.typography.bodyMedium.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              lineHeight = 20.sp
            ),
            modifier = Modifier.padding(horizontal = 24.dp)
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Sibling Connection Card
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
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                  )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                  Text(
                    text = "Connected with Ananya",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                  )
                  Text(
                    text = "Sister • Private Space Active",
                    style = MaterialTheme.typography.bodySmall.copy(
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  )
                }
              }

              TextButton(onClick = { showDisconnectDialog = true }) {
                Text(
                  text = "Manage",
                  style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                  )
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Premium Tier Card (₹19/month)
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { showPremiumDialog = true },
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, WarmGold.copy(alpha = 0.7f))
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(48.dp)
                  .clip(CircleShape)
                  .background(WarmGold.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Star,
                  contentDescription = null,
                  tint = WarmGold,
                  modifier = Modifier.size(28.dp)
                )
              }

              Spacer(modifier = Modifier.width(14.dp))

              Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = "Brother & Sister Premium",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                  )
                }
                Text(
                  text = "Unlimited HD photos, videos & backup for ₹19/mo",
                  style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                )
              }

              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.outline
              )
            }
          }
        }
      }

      // Settings Group
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
        ) {
          Text(
            text = "PREFERENCES & SECURITY",
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
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
          ) {
            Column {
              // Notification toggle
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    Icons.Default.Notifications,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                  )
                  Spacer(modifier = Modifier.width(14.dp))
                  Text(
                    text = "Memory Notifications",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                  )
                }
                Switch(
                  checked = notificationsEnabled,
                  onCheckedChange = { notificationsEnabled = it },
                  colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
                )
              }

              HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

              ProfileMenuItem(
                icon = Icons.Default.Lock,
                title = "Privacy & Encryption",
                subtitle = "Only you and your sibling can view memories",
                onClick = {}
              )

              HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

              ProfileMenuItem(
                icon = Icons.Default.Security,
                title = "Security & Passcode Lock",
                subtitle = "Protect memory app with biometric unlock",
                onClick = {}
              )

              HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

              ProfileMenuItem(
                icon = Icons.Default.CloudDone,
                title = "Cloud Backup & Export",
                subtitle = "Download all photos and memories safely",
                onClick = {}
              )
            }
          }
        }
      }

      // Support & Legal Group
      item {
        Spacer(modifier = Modifier.height(20.dp))
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
        ) {
          Text(
            text = "SUPPORT & LEGAL",
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
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
          ) {
            Column {
              ProfileMenuItem(
                icon = Icons.Default.HelpOutline,
                title = "Help & Family Guides",
                onClick = {}
              )

              HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

              ProfileMenuItem(
                icon = Icons.Default.ReportProblem,
                title = "Report a Problem",
                onClick = {}
              )

              HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

              ProfileMenuItem(
                icon = Icons.Default.VerifiedUser,
                title = "Terms of Service & Privacy Policy",
                onClick = {}
              )
            }
          }
        }
      }

      // Actions Group: Logout & Delete
      item {
        Spacer(modifier = Modifier.height(20.dp))
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
        ) {
          OutlinedButton(
            onClick = onLogout,
            modifier = Modifier
              .fillMaxWidth()
              .height(52.dp),
            shape = RoundedCornerShape(16.dp)
          ) {
            Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Log Out", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
          }

          Spacer(modifier = Modifier.height(10.dp))

          TextButton(
            onClick = { showDeleteDialog = true },
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "Delete Account & Wipe Space",
              style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Medium
              )
            )
          }
        }
      }
    }

    // Disconnect Sibling Dialog
    if (showDisconnectDialog) {
      AlertDialog(
        onDismissRequest = { showDisconnectDialog = false },
        title = { Text("Sibling Connection") },
        text = {
          Text(
            "You are currently connected to Ananya in a private space. You can disconnect or invite another sibling if needed."
          )
        },
        confirmButton = {
          Button(
            onClick = {
              showDisconnectDialog = false
              onNavigateToConnect()
            }
          ) {
            Text("Connection Settings")
          }
        },
        dismissButton = {
          TextButton(onClick = { showDisconnectDialog = false }) {
            Text("Cancel")
          }
        }
      )
    }

    // Delete Account Dialog
    if (showDeleteDialog) {
      AlertDialog(
        onDismissRequest = { showDeleteDialog = false },
        title = { Text("Delete Account?", color = MaterialTheme.colorScheme.error) },
        text = {
          Text("This action cannot be undone. All shared memories and chat messages in your space will be permanently erased.")
        },
        confirmButton = {
          Button(
            onClick = {
              showDeleteDialog = false
              onLogout()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
          ) {
            Text("Delete Forever")
          }
        },
        dismissButton = {
          TextButton(onClick = { showDeleteDialog = false }) {
            Text("Cancel")
          }
        }
      )
    }

    // Premium Upgrade Dialog
    if (showPremiumDialog) {
      AlertDialog(
        onDismissRequest = { showPremiumDialog = false },
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = null, tint = WarmGold)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Brother & Sister Premium ❤️")
          }
        },
        text = {
          Column {
            Text(
              text = "Preserve a lifetime of high definition photos, videos, and unlimited albums with cloud backup.",
              style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
              color = MaterialTheme.colorScheme.primaryContainer,
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Text(
                  text = "Special Launch Pricing:",
                  style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onPrimaryContainer)
                )
                Text(
                  text = "₹19 / Month  or  ₹199 / Year",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                  )
                )
              }
            }
          }
        },
        confirmButton = {
          Button(
            onClick = { showPremiumDialog = false },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
          ) {
            Text("Activate Plan")
          }
        },
        dismissButton = {
          TextButton(onClick = { showPremiumDialog = false }) {
            Text("Later")
          }
        }
      )
    }
  }
}

@Composable
fun ProfileMenuItem(
  icon: ImageVector,
  title: String,
  subtitle: String? = null,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(horizontal = 16.dp, vertical = 14.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.primary,
      modifier = Modifier.size(22.dp)
    )

    Spacer(modifier = Modifier.width(14.dp))

    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge.copy(
          fontWeight = FontWeight.Medium,
          color = MaterialTheme.colorScheme.onSurface
        )
      )
      if (subtitle != null) {
        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        )
      }
    }

    Icon(
      imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.outline,
      modifier = Modifier.size(14.dp)
    )
  }
}
