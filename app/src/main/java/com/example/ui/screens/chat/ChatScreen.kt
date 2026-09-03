package com.example.ui.screens.chat

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.SampleData
import com.example.model.ChatMessage
import com.example.ui.theme.HeartRed
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
  siblingName: String = "Ananya"
) {
  val messages = remember { mutableStateListOf<ChatMessage>().apply { addAll(SampleData.sampleChatMessages) } }
  var messageText by remember { mutableStateOf("") }
  val listState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()

  LaunchedEffect(messages.size) {
    listState.animateScrollToItem(messages.size - 1)
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Chat Header
    Surface(
      color = MaterialTheme.colorScheme.surface,
      shadowElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Image(
              painter = painterResource(id = R.drawable.ic_app_logo),
              contentDescription = siblingName,
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = siblingName,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = HeartRed,
                modifier = Modifier.size(14.dp)
              )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Encrypted",
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Private Brother/Sister Space",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = MaterialTheme.colorScheme.outline
                )
              )
            }
          }
        }

        IconButton(onClick = { /* Menu */ }) {
          Icon(Icons.Default.MoreVert, contentDescription = "More")
        }
      }
    }

    // Messages list
    LazyColumn(
      state = listState,
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // Date chip
      item {
        Box(
          modifier = Modifier.fillMaxWidth(),
          contentAlignment = Alignment.Center
        ) {
          Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text(
              text = "Today",
              style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
              ),
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
          }
        }
      }

      items(messages) { message ->
        ChatBubble(message = message)
      }
    }

    // Input Bar
    Surface(
      color = MaterialTheme.colorScheme.surface,
      shadowElevation = 6.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = {
            // Attach demo photo message
            messages.add(
              ChatMessage(
                id = "msg_${UUID.randomUUID()}",
                senderId = "user_shashank",
                senderName = "Shashank",
                text = "Sharing this sweet memory with you! ❤️",
                timestamp = "Just now",
                isFromMe = true,
                hasPhoto = true,
                photoDrawableRes = R.drawable.hero_family,
                isRead = true
              )
            )
          }
        ) {
          Icon(
            imageVector = Icons.Default.AddPhotoAlternate,
            contentDescription = "Send Photo",
            tint = MaterialTheme.colorScheme.primary
          )
        }

        OutlinedTextField(
          value = messageText,
          onValueChange = { messageText = it },
          placeholder = { Text("Write to $siblingName...") },
          modifier = Modifier
            .weight(1f)
            .padding(horizontal = 4.dp),
          shape = RoundedCornerShape(24.dp),
          maxLines = 3,
          colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = MaterialTheme.colorScheme.primary
          )
        )

        IconButton(
          onClick = {
            if (messageText.isNotBlank()) {
              messages.add(
                ChatMessage(
                  id = "msg_${UUID.randomUUID()}",
                  senderId = "user_shashank",
                  senderName = "Shashank",
                  text = messageText.trim(),
                  timestamp = "Just now",
                  isFromMe = true,
                  isRead = false
                )
              )
              messageText = ""
            }
          },
          enabled = messageText.isNotBlank()
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(
                if (messageText.isNotBlank()) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceVariant
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.Send,
              contentDescription = "Send",
              tint = if (messageText.isNotBlank()) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
fun ChatBubble(message: ChatMessage) {
  val isMe = message.isFromMe

  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
  ) {
    Box(
      modifier = Modifier
        .widthIn(max = 280.dp)
        .clip(
          RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp,
            bottomStart = if (isMe) 20.dp else 4.dp,
            bottomEnd = if (isMe) 4.dp else 20.dp
          )
        )
        .background(
          if (isMe) MaterialTheme.colorScheme.primaryContainer
          else MaterialTheme.colorScheme.surfaceVariant
        )
        .padding(12.dp)
    ) {
      Column {
        if (message.hasPhoto && message.photoDrawableRes != null) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(150.dp)
              .clip(RoundedCornerShape(14.dp))
              .padding(bottom = 8.dp)
          ) {
            Image(
              painter = painterResource(id = message.photoDrawableRes),
              contentDescription = "Shared photo",
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )
          }
        }

        Text(
          text = message.text,
          style = MaterialTheme.typography.bodyMedium.copy(
            color = if (isMe) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
          )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
          modifier = Modifier.align(Alignment.End),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = message.timestamp,
            style = MaterialTheme.typography.labelSmall.copy(
              color = MaterialTheme.colorScheme.outline,
              fontSize = 10.sp
            )
          )

          if (isMe) {
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = if (message.isRead) Icons.Default.DoneAll else Icons.Default.Check,
              contentDescription = if (message.isRead) "Read" else "Sent",
              tint = if (message.isRead) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
              modifier = Modifier.size(14.dp)
            )
          }
        }
      }
    }

    if (message.reaction != null) {
      Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = CircleShape,
        shadowElevation = 2.dp,
        modifier = Modifier
          .padding(top = 2.dp, start = if (isMe) 0.dp else 8.dp, end = if (isMe) 8.dp else 0.dp)
      ) {
        Text(
          text = message.reaction,
          style = MaterialTheme.typography.labelSmall,
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
      }
    }
  }
}
