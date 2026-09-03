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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
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
  var isSending by remember { mutableStateOf(false) }
  var isLoadingIncoming by remember { mutableStateOf(false) }
  var incomingInvitationId by remember { mutableStateOf<String?>(null) }
  var incomingFromUid by remember { mutableStateOf<String?>(null) }
  var incomingFromName by remember { mutableStateOf("Your sibling") }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()
  val auth = remember { FirebaseAuth.getInstance() }
  val firestore = remember { FirebaseFirestore.getInstance() }
  val currentUser = auth.currentUser

  val inviteLink = "https://brotherandsister.app/connect/shashank-k928"

  fun loadIncomingInvitation() {
    val email = currentUser?.email?.trim()?.lowercase() ?: return
    isLoadingIncoming = true
    errorMessage = null

    firestore.collection("invitations")
      .whereEqualTo("toEmail", email)
      .whereEqualTo("status", "pending")
      .limit(1)
      .get()
      .addOnSuccessListener { snapshot ->
        val document = snapshot.documents.firstOrNull()
        if (document != null) {
          incomingInvitationId = document.id
          incomingFromUid = document.getString("fromUid")
          incomingFromName = document.getString("fromName") ?: "Your sibling"
        } else {
          incomingInvitationId = null
          incomingFromUid = null
        }
        isLoadingIncoming = false
      }
      .addOnFailureListener { exception ->
        isLoadingIncoming = false
        errorMessage = exception.localizedMessage ?: "Could not load invitations."
      }
  }

  fun sendInvitation() {
    val email = inviteEmail.trim().lowercase()
    val sender = currentUser

    if (sender == null) {
      errorMessage = "Please sign in before sending an invitation."
      return
    }
    if (email.isBlank() || !email.contains("@")) {
      errorMessage = "Enter a valid sibling email address."
      return
    }
    if (email == sender.email?.trim()?.lowercase()) {
      errorMessage = "You cannot invite your own account."
      return
    }

    isSending = true
    errorMessage = null

    firestore.collection("users")
      .whereEqualTo("email", email)
      .limit(1)
      .get()
      .addOnSuccessListener { userSnapshot ->
        val recipient = userSnapshot.documents.firstOrNull()
        if (recipient == null) {
          isSending = false
          errorMessage = "No Brother & Sister account was found for this email. Ask your sibling to register first."
          return@addOnSuccessListener
        }

        val recipientUid = recipient.id
        val senderName = sender.displayName?.takeIf { it.isNotBlank() }
          ?: "Your sibling"

        val invitation = hashMapOf<String, Any>(
          "fromUid" to sender.uid,
          "fromName" to senderName,
          "toUid" to recipientUid,
          "toEmail" to email,
          "status" to "pending",
          "createdAt" to FieldValue.serverTimestamp()
        )

        firestore.collection("invitations")
          .add(invitation)
          .addOnSuccessListener {
            isSending = false
            inviteEmail = ""
            coroutineScope.launch {
              snackbarHostState.showSnackbar("Invitation sent successfully!")
            }
          }
          .addOnFailureListener { exception ->
            isSending = false
            errorMessage = exception.localizedMessage ?: "Could not send invitation."
          }
      }
      .addOnFailureListener { exception ->
        isSending = false
        errorMessage = exception.localizedMessage ?: "Could not find that account."
      }
  }

  fun acceptInvitation() {
    val invitationId = incomingInvitationId
    val fromUid = incomingFromUid
    val toUid = currentUser?.uid

    if (invitationId == null || fromUid == null || toUid == null) {
      errorMessage = "This invitation is no longer available."
      return
    }

    errorMessage = null
    val connection = hashMapOf<String, Any>(
      "user1Uid" to fromUid,
      "user2Uid" to toUid,
      "createdAt" to FieldValue.serverTimestamp()
    )

    firestore.collection("siblingConnections")
      .add(connection)
      .addOnSuccessListener {
        firestore.collection("invitations")
          .document(invitationId)
          .update(
            mapOf(
              "status" to "accepted",
              "acceptedAt" to FieldValue.serverTimestamp()
            )
          )
          .addOnSuccessListener {
            isAccepted = true
            onConnectedSuccess()
          }
          .addOnFailureListener { exception ->
            errorMessage = exception.localizedMessage ?: "Connected, but the invitation status could not be updated."
          }
      }
      .addOnFailureListener { exception ->
        errorMessage = exception.localizedMessage ?: "Could not create the sibling connection."
      }
  }

  if (selectedTab == 1 && incomingInvitationId == null && !isLoadingIncoming) {
    loadIncomingInvitation()
  }

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
          onClick = {
            selectedTab = 1
            loadIncomingInvitation()
          },
          text = { Text("Incoming Request") }
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      errorMessage?.let { message ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
          shape = RoundedCornerShape(14.dp)
        ) {
          Text(
            text = message,
            color = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier.padding(14.dp),
            style = MaterialTheme.typography.bodyMedium
          )
        }
        Spacer(modifier = Modifier.height(12.dp))
      }

      if (selectedTab == 0) {
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
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Only connected siblings will get access to the private shared space.",
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
          )

          Spacer(modifier = Modifier.height(24.dp))

          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
          ) {
            Column(modifier = Modifier.padding(18.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Send via Email", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
              }
              Spacer(modifier = Modifier.height(12.dp))
              OutlinedTextField(
                value = inviteEmail,
                onValueChange = {
                  inviteEmail = it
                  errorMessage = null
                },
                label = { Text("Sibling's Email Address") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                singleLine = true
              )
              Spacer(modifier = Modifier.height(12.dp))
              Button(
                onClick = ::sendInvitation,
                enabled = !isSending,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Text(if (isSending) "Sending..." else "Send Private Invitation")
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
          ) {
            Column(modifier = Modifier.padding(18.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Link, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Invitation Link", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
              }
              Spacer(modifier = Modifier.height(8.dp))
              Text(inviteLink, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
              Spacer(modifier = Modifier.height(14.dp))
              Button(
                onClick = {
                  isCopied = true
                  coroutineScope.launch { snackbarHostState.showSnackbar("Invitation link copied to clipboard!") }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Icon(if (isCopied) Icons.Default.CheckCircle else Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isCopied) "Copied!" else "Copy Link")
              }
            }
          }
        }
      } else {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          if (isLoadingIncoming) {
            Text("Checking for invitations...", style = MaterialTheme.typography.bodyMedium)
          } else if (incomingInvitationId != null && !isAccepted) {
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(24.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
            ) {
              Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = HeartRed, modifier = Modifier.size(54.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                  text = "$incomingFromName wants to create a private Brother/Sister memory space with you.",
                  style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                  textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                  text = "Accepting will connect both accounts so the private shared space can be used by both siblings.",
                  style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                  textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                  OutlinedButton(
                    onClick = {
                      incomingInvitationId?.let { id ->
                        firestore.collection("invitations").document(id)
                          .update("status", "declined")
                          .addOnSuccessListener {
                            incomingInvitationId = null
                            coroutineScope.launch { snackbarHostState.showSnackbar("Invitation declined.") }
                          }
                          .addOnFailureListener { exception ->
                            errorMessage = exception.localizedMessage ?: "Could not decline invitation."
                          }
                      }
                    },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(14.dp)
                  ) { Text("Decline") }

                  Button(
                    onClick = ::acceptInvitation,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                  ) { Text("Accept ❤️", fontWeight = FontWeight.Bold) }
                }
              }
            }
          } else {
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
              Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = HeartRed, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text("No pending invitations", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  "When your sibling sends an invitation, it will appear here.",
                  textAlign = TextAlign.Center,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }

      SnackbarHost(hostState = snackbarHostState)
    }
  }
}
