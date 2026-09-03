package com.example.ui.screens.connection

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.HeartRed
import kotlinx.coroutines.launch

@Composable
fun ConnectSiblingScreen(
  onBack: () -> Unit,
  onConnectedSuccess: () -> Unit
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  var inviteEmail by remember { mutableStateOf("") }
  var isAccepted by remember { mutableStateOf(false) }
  var isCopied by remember { mutableStateOf(false) }
  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()

  val inviteLink = "https://brotherandsister.app/connect/shashank-k928"

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Brother & Sister Connection",
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      TabRow(selectedTabIndex = selectedTab) {
        Tab(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          text = { Text("Invite Sibling") }
        )
        Tab(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          text = { Text("Incoming Request") }
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      if (selectedTab == 0) {
        // Invite Brother/Sister
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Box(
            modifier = Modifier
              .size(64.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Favorite, contentDescription = null, tint = HeartRed, modifier = Modifier.size(32.dp))
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = "Invite Your Brother or Sister",
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            ),
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "Only one connected sibling gets access to your private shared sanctuary.",
            style = MaterialTheme.typography.bodyMedium.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
          )

          Spacer(modifier = Modifier.height(24.dp))

          // Method 1: Invitation Link
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
          ) {
            Column(modifier = Modifier.padding(18.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Link, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Private Invitation Link",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
              }
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = inviteLink,
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
              )
              Spacer(modifier = Modifier.height(14.dp))
              Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                  onClick = {
                    isCopied = true
                    coroutineScope.launch { snackbarHostState.showSnackbar("Invitation link copied to clipboard!") }
                  },
                  shape = RoundedCornerShape(12.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                  modifier = Modifier.weight(1f)
                ) {
                  Icon(
                    if (isCopied) Icons.Default.CheckCircle else Icons.Default.ContentCopy,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(if (isCopied) "Copied!" else "Copy Link")
                }

                OutlinedButton(
                  onClick = {
                    coroutineScope.launch { snackbarHostState.showSnackbar("Opening system share sheet...") }
                  },
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier.weight(1f)
                ) {
                  Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Share")
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Method 2: QR Code Visual
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
          ) {
            Column(
              modifier = Modifier.padding(18.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.QrCode, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Scan QR Code in Person",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
              }

              Spacer(modifier = Modifier.height(14.dp))

              Box(
                modifier = Modifier
                  .size(160.dp)
                  .clip(RoundedCornerShape(16.dp))
                  .background(Color.White)
                  .padding(16.dp),
                contentAlignment = Alignment.Center
              ) {
                Image(
                  painter = painterResource(id = R.drawable.ic_app_logo),
                  contentDescription = "QR Code Placeholder",
                  contentScale = ContentScale.Fit,
                  modifier = Modifier.size(128.dp)
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = "Hold sibling's phone camera over this code to connect instantly",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  textAlign = TextAlign.Center
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Method 3: Direct Email Invite
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
          ) {
            Column(modifier = Modifier.padding(18.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Send via Email",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
              }
              Spacer(modifier = Modifier.height(12.dp))
              OutlinedTextField(
                value = inviteEmail,
                onValueChange = { inviteEmail = it },
                label = { Text("Sibling's Email Address") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                singleLine = true
              )
              Spacer(modifier = Modifier.height(12.dp))
              Button(
                onClick = {
                  if (inviteEmail.isNotBlank()) {
                    coroutineScope.launch {
                      snackbarHostState.showSnackbar("Invitation sent to $inviteEmail!")
                    }
                  }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Text("Send Private Invitation")
              }
            }
          }
        }
      } else {
        // Incoming Request View (User B side experience)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          if (!isAccepted) {
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(24.dp),
              colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
              ),
              border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
            ) {
              Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Box(
                  modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                  contentAlignment = Alignment.Center
                ) {
                  Image(
                    painter = painterResource(id = R.drawable.ic_app_logo),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                  )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                  text = "Ananya wants to create a private Brother/Sister memory space with you.",
                  style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    lineHeight = 26.sp
                  ),
                  textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                  text = "By accepting, you two will share a locked, private space for childhood photos, videos, and memories.",
                  style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  ),
                  textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                  OutlinedButton(
                    onClick = {
                      coroutineScope.launch {
                        snackbarHostState.showSnackbar("Invitation declined.")
                      }
                    },
                    modifier = Modifier
                      .weight(1f)
                      .height(48.dp),
                    shape = RoundedCornerShape(14.dp)
                  ) {
                    Text("Decline")
                  }

                  Button(
                    onClick = {
                      isAccepted = true
                      onConnectedSuccess()
                    },
                    modifier = Modifier
                      .weight(1f)
                      .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                  ) {
                    Text("Accept ❤️", fontWeight = FontWeight.Bold)
                  }
                }
              }
            }
          } else {
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(24.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
              Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Icon(
                  Icons.Default.Favorite,
                  contentDescription = null,
                  tint = HeartRed,
                  modifier = Modifier.size(54.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                  text = "Your Brother/Sister Space is Ready ❤️",
                  style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                  ),
                  textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = "Connected with Ananya! You can now preserve memories, share photos, and chat in complete privacy.",
                  style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  ),
                  textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                  onClick = onBack,
                  shape = RoundedCornerShape(14.dp)
                ) {
                  Text("Enter Your Space")
                }
              }
            }
          }
        }
      }

      SnackbarHost(hostState = snackbarHostState)
    }
  }
}
