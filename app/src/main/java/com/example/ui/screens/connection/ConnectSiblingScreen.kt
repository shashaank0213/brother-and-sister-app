package com.example.ui.screens.connection

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.theme.HeartRed
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun ConnectSiblingScreen(
  onBack: () -> Unit,
  onConnectedSuccess: () -> Unit,
  initialTab: Int = 0
) {
  var selectedTab by remember { mutableIntStateOf(initialTab.coerceIn(0, 1)) }
  var inviteEmail by remember { mutableStateOf("") }
  var incomingInvitationId by remember { mutableStateOf<String?>(null) }
  var incomingFromUid by remember { mutableStateOf<String?>(null) }
  var incomingFromName by remember { mutableStateOf("Your sibling") }
  var loadingIncoming by remember { mutableStateOf(false) }
  var sending by remember { mutableStateOf(false) }
  var accepting by remember { mutableStateOf(false) }
  var message by remember { mutableStateOf<String?>(null) }
  var isError by remember { mutableStateOf(false) }

  val auth = remember { FirebaseAuth.getInstance() }
  val db = remember { FirebaseFirestore.getInstance() }
  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()
  val currentUser = auth.currentUser

  fun showMessage(text: String, error: Boolean = false) {
    message = text
    isError = error
  }

  fun loadIncomingInvitation() {
    val uid = currentUser?.uid
    val email = currentUser?.email?.trim()?.lowercase()
    if (uid.isNullOrBlank()) return

    loadingIncoming = true
    message = null
    db.collection("invitations")
      .whereEqualTo("toUid", uid)
      .whereEqualTo("status", "pending")
      .limit(1)
      .get()
      .addOnSuccessListener { snapshot ->
        val doc = snapshot.documents.firstOrNull()
        if (doc != null) {
          incomingInvitationId = doc.id
          incomingFromUid = doc.getString("fromUid")
          incomingFromName = doc.getString("fromName") ?: "Your sibling"
          loadingIncoming = false
        } else if (!email.isNullOrBlank()) {
          // Fallback to check pending invitation by email
          db.collection("invitations")
            .whereEqualTo("toEmail", email)
            .whereEqualTo("status", "pending")
            .limit(1)
            .get()
            .addOnSuccessListener { emailSnapshot ->
              val emailDoc = emailSnapshot.documents.firstOrNull()
              if (emailDoc != null) {
                incomingInvitationId = emailDoc.id
                incomingFromUid = emailDoc.getString("fromUid")
                incomingFromName = emailDoc.getString("fromName") ?: "Your sibling"
              } else {
                incomingInvitationId = null
                incomingFromUid = null
                incomingFromName = "Your sibling"
              }
              loadingIncoming = false
            }
            .addOnFailureListener {
              loadingIncoming = false
            }
        } else {
          incomingInvitationId = null
          incomingFromUid = null
          incomingFromName = "Your sibling"
          loadingIncoming = false
        }
      }
      .addOnFailureListener { e ->
        loadingIncoming = false
        showMessage(e.localizedMessage ?: "Could not load invitations.", true)
      }
  }

  fun sendInvitation() {
    val sender = currentUser
    val email = inviteEmail.trim().lowercase()

    if (sender == null) {
      showMessage("Please sign in before sending an invitation.", true)
      return
    }
    if (!email.contains("@")) {
      showMessage("Enter a valid sibling email address.", true)
      return
    }
    if (email == sender.email?.trim()?.lowercase()) {
      showMessage("You cannot invite your own account.", true)
      return
    }

    sending = true
    message = null

    db.collection("users")
      .whereEqualTo("email", email)
      .limit(1)
      .get()
      .addOnSuccessListener { users ->
        val recipient = users.documents.firstOrNull()
        if (recipient == null) {
          sending = false
          showMessage("No Brother & Sister account was found for this email. Ask your sibling to register first.", true)
          return@addOnSuccessListener
        }

        val recipientUid = recipient.id
        val senderName = db.collection("users").document(sender.uid).get()
          .continueWith { task ->
            task.result?.getString("firstName")?.takeIf { it.isNotBlank() } ?: "Your sibling"
          }

        senderName.addOnSuccessListener { name ->
          val invitation = hashMapOf<String, Any>(
            "fromUid" to sender.uid,
            "fromName" to name,
            "toUid" to recipientUid,
            "toEmail" to email,
            "status" to "pending",
            "createdAt" to FieldValue.serverTimestamp()
          )

          db.collection("invitations")
            .whereEqualTo("fromUid", sender.uid)
            .whereEqualTo("toUid", recipientUid)
            .whereEqualTo("status", "pending")
            .limit(1)
            .get()
            .addOnSuccessListener { existing ->
              if (existing.documents.isNotEmpty()) {
                sending = false
                showMessage("An invitation is already pending for this sibling.")
                return@addOnSuccessListener
              }

              db.collection("siblingConnections")
                .whereEqualTo("user1Uid", sender.uid)
                .whereEqualTo("user2Uid", recipientUid)
                .limit(1)
                .get()
                .addOnSuccessListener { connections ->
                  if (connections.documents.isNotEmpty()) {
                    sending = false
                    showMessage("You are already connected with this sibling.")
                    return@addOnSuccessListener
                  }

                  db.collection("invitations").add(invitation)
                    .addOnSuccessListener {
                      sending = false
                      inviteEmail = ""
                      showMessage("Invitation sent successfully!")
                      scope.launch { snackbarHostState.showSnackbar("Invitation sent successfully!") }
                    }
                    .addOnFailureListener { e ->
                      sending = false
                      showMessage(e.localizedMessage ?: "Could not send invitation.", true)
                    }
                }
                .addOnFailureListener { e ->
                  sending = false
                  showMessage(e.localizedMessage ?: "Could not check the existing connection.", true)
                }
            }
            .addOnFailureListener { e ->
              sending = false
              showMessage(e.localizedMessage ?: "Could not check pending invitations.", true)
            }
        }.addOnFailureListener { e ->
          sending = false
          showMessage(e.localizedMessage ?: "Could not load your profile.", true)
        }
      }
      .addOnFailureListener { e ->
        sending = false
        showMessage(e.localizedMessage ?: "Could not find that account.", true)
      }
  }

  fun acceptInvitation() {
    val invitationId = incomingInvitationId
    val fromUid = incomingFromUid
    val toUid = currentUser?.uid

    if (invitationId.isNullOrBlank() || fromUid.isNullOrBlank() || toUid.isNullOrBlank()) {
      showMessage("This invitation is no longer available.", true)
      return
    }

    accepting = true
    message = null

    db.collection("siblingConnections")
      .whereEqualTo("user1Uid", fromUid)
      .whereEqualTo("user2Uid", toUid)
      .limit(1)
      .get()
      .addOnSuccessListener { existing ->
        if (existing.documents.isNotEmpty()) {
          accepting = false
          db.collection("invitations").document(invitationId).update("status", "accepted")
          onConnectedSuccess()
          return@addOnSuccessListener
        }

        val connectionRef = db.collection("siblingConnections").document()
        val connection = hashMapOf<String, Any>(
          "user1Uid" to fromUid,
          "user2Uid" to toUid,
          "createdAt" to FieldValue.serverTimestamp()
        )

        val batch = db.batch()
        batch.set(connectionRef, connection)
        batch.update(
          db.collection("invitations").document(invitationId),
          mapOf(
            "status" to "accepted",
            "acceptedAt" to FieldValue.serverTimestamp()
          )
        )

        batch.commit()
          .addOnSuccessListener {
            accepting = false
            incomingInvitationId = null
            showMessage("Connected successfully ❤️")
            onConnectedSuccess()
          }
          .addOnFailureListener { e ->
            accepting = false
            showMessage(e.localizedMessage ?: "Could not create the sibling connection.", true)
          }
      }
      .addOnFailureListener { e ->
        accepting = false
        showMessage(e.localizedMessage ?: "Could not check the existing connection.", true)
      }
  }

  LaunchedEffect(selectedTab, currentUser?.uid) {
    if (selectedTab == 1) loadIncomingInvitation()
  }

  Box(modifier = Modifier.fillMaxSize()) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(20.dp)
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Spacer(Modifier.width(8.dp))
        Text("Brother & Sister Connection", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
      }

      Spacer(Modifier.height(16.dp))
      TabRow(selectedTabIndex = selectedTab) {
        Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Invite Sibling") })
        Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Incoming Request") })
      }
      Spacer(Modifier.height(24.dp))

      message?.let {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(
            containerColor = if (isError) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer
          )
        ) {
          Text(it, modifier = Modifier.padding(14.dp))
        }
        Spacer(Modifier.height(12.dp))
      }

      if (selectedTab == 0) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(Icons.Default.Favorite, contentDescription = null, tint = HeartRed, modifier = Modifier.size(56.dp))
          Spacer(Modifier.height(12.dp))
          Text("Invite Your Brother or Sister", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), textAlign = TextAlign.Center)
          Spacer(Modifier.height(8.dp))
          Text("Only connected siblings can access the private shared space.", textAlign = TextAlign.Center)
          Spacer(Modifier.height(24.dp))

          Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)) {
            Column(modifier = Modifier.padding(18.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Email, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Send via Email", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
              }
              Spacer(Modifier.height(12.dp))
              OutlinedTextField(
                value = inviteEmail,
                onValueChange = { inviteEmail = it; message = null },
                label = { Text("Sibling's Email Address") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
              )
              Spacer(Modifier.height(12.dp))
              Button(onClick = ::sendInvitation, enabled = !sending, modifier = Modifier.fillMaxWidth()) {
                Text(if (sending) "Sending..." else "Send Private Invitation")
              }
            }
          }
        }
      } else {
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          if (loadingIncoming) {
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
              Column(
                modifier = Modifier.padding(28.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text("Checking for invitations...", style = MaterialTheme.typography.bodyMedium)
              }
            }
          } else if (incomingInvitationId != null) {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp)) {
              Column(modifier = Modifier.padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = HeartRed, modifier = Modifier.size(52.dp))
                Spacer(Modifier.height(14.dp))
                Text(
                  "$incomingFromName wants to connect with you as a sibling.",
                  style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                  textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(10.dp))
                Text("Accept to create the private shared sibling connection.", textAlign = TextAlign.Center)
                Spacer(Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                  OutlinedButton(
                    onClick = {
                      val id = incomingInvitationId ?: return@OutlinedButton
                      db.collection("invitations").document(id).update("status", "declined")
                        .addOnSuccessListener {
                          incomingInvitationId = null
                          showMessage("Invitation declined.")
                        }
                        .addOnFailureListener { e -> showMessage(e.localizedMessage ?: "Could not decline invitation.", true) }
                    },
                    modifier = Modifier.weight(1f)
                  ) { Text("Decline") }
                  Button(onClick = ::acceptInvitation, enabled = !accepting, modifier = Modifier.weight(1f)) {
                    Text(if (accepting) "Accepting..." else "Accept ❤️")
                  }
                }
              }
            }
          } else {
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
              Column(
                modifier = Modifier.padding(28.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = HeartRed.copy(alpha = 0.6f), modifier = Modifier.size(48.dp))
                Spacer(Modifier.height(14.dp))
                Text("No pending invitations", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                Spacer(Modifier.height(8.dp))
                Text(
                  "When your sibling sends an invitation to your email, it will appear here.",
                  textAlign = TextAlign.Center,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }

    SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
  }
}
