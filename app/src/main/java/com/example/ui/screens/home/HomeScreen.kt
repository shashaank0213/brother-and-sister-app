package com.example.ui.screens.home

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.SampleData
import com.example.model.ImportantDateItem
import com.example.model.MemoryItem
import com.example.model.SiblingConnection
import com.example.model.UserProfile
import com.example.ui.theme.EditorialBorder
import com.example.ui.theme.EditorialDustyRose
import com.example.ui.theme.EditorialEspresso
import com.example.ui.theme.EditorialMauve
import com.example.ui.theme.EditorialOnPeriwinkle
import com.example.ui.theme.EditorialPeriwinkle
import com.example.ui.theme.EditorialPlum
import com.example.ui.theme.EditorialQuoteStyle
import com.example.ui.theme.EditorialRoseBlush
import com.example.ui.theme.EditorialWarmIvory
import com.example.ui.theme.HeartRed
import com.example.ui.theme.WarmGold

@Composable
fun HomeScreen(
  user: UserProfile = SampleData.currentUser,
  connection: SiblingConnection = SampleData.siblingConnection,
  memories: List<MemoryItem> = SampleData.sampleMemories,
  pendingNotificationCount: Int = 0,
  onAddMemoryClick: () -> Unit,
  onMessageClick: () -> Unit,
  onConnectSiblingClick: () -> Unit,
  onViewAllMemories: () -> Unit,
  onNotificationsClick: () -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
    contentPadding = PaddingValues(bottom = 96.dp)
  ) {
    // Header
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Good Morning",
            style = MaterialTheme.typography.bodySmall.copy(
              fontWeight = FontWeight.Medium,
              color = EditorialMauve,
              fontSize = 14.sp
            )
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Hello, ${user.firstName} 👋",
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 24.sp,
              letterSpacing = (-0.5).sp,
              color = EditorialEspresso
            )
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = onNotificationsClick,
            modifier = Modifier.size(44.dp)
          ) {
            BadgedBox(
              badge = {
                if (pendingNotificationCount > 0) {
                  Badge(
                    containerColor = HeartRed,
                    contentColor = Color.White
                  ) {
                    Text(
                      text = if (pendingNotificationCount > 9) "9+" else "$pendingNotificationCount",
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                      )
                    )
                  }
                }
              }
            ) {
              Icon(
                imageVector = Icons.Default.NotificationsNone,
                contentDescription = "Notifications",
                tint = EditorialMauve
              )
            }
          }

          Box(
            modifier = Modifier
              .size(48.dp)
              .clip(CircleShape)
              .border(2.dp, Color.White, CircleShape)
              .background(EditorialPeriwinkle),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = user.firstName.take(1).uppercase(),
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = EditorialOnPeriwinkle,
                fontSize = 18.sp
              )
            )
          }
        }
      }
    }

    // Main Card: "Sister Space" / "Brother Space"
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
          containerColor = EditorialRoseBlush
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(22.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          // Top row of main card
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(56.dp)
                  .clip(RoundedCornerShape(20.dp))
                  .background(Color.White.copy(alpha = 0.65f))
                  .clickable { onConnectSiblingClick() },
                contentAlignment = Alignment.Center
              ) {
                Text(text = "👩👧", fontSize = 26.sp)
              }

              Column {
                Text(
                  text = "${connection.siblingRole.replaceFirstChar { it.uppercase() }} Space",
                  style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = EditorialPlum
                  )
                )
                Text(
                  text = "CONNECTED WITH ${connection.siblingName.uppercase()}",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = EditorialMauve,
                    letterSpacing = 1.sp
                  )
                )
              }
            }

            Surface(
              color = Color.White.copy(alpha = 0.7f),
              shape = RoundedCornerShape(12.dp)
            ) {
              Text(
                text = "Connected ❤️",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = EditorialPlum,
                  fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          // Statistics counters row
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(20.dp))
              .background(Color.White.copy(alpha = 0.45f))
              .padding(vertical = 12.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(
              modifier = Modifier.weight(1f),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "${connection.sharedMemoryCount}",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 18.sp,
                  color = EditorialEspresso
                )
              )
              Text(
                text = "MEMORIES",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 10.sp,
                  color = EditorialMauve,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 0.5.sp
                )
              )
            }

            Box(
              modifier = Modifier
                .width(1.dp)
                .height(28.dp)
                .background(EditorialMauve.copy(alpha = 0.15f))
            )

            Column(
              modifier = Modifier.weight(1f),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "${connection.photoCount}",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 18.sp,
                  color = EditorialEspresso
                )
              )
              Text(
                text = "PHOTOS",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 10.sp,
                  color = EditorialMauve,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 0.5.sp
                )
              )
            }

            Box(
              modifier = Modifier
                .width(1.dp)
                .height(28.dp)
                .background(EditorialMauve.copy(alpha = 0.15f))
            )

            Column(
              modifier = Modifier.weight(1f),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "${connection.videoCount}",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 18.sp,
                  color = EditorialEspresso
                )
              )
              Text(
                text = "VIDEOS",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = 10.sp,
                  color = EditorialMauve,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 0.5.sp
                )
              )
            }
          }

          // Primary action buttons: + Add Memory and Message
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Button(
              onClick = onAddMemoryClick,
              modifier = Modifier
                .weight(1f)
                .height(48.dp),
              shape = RoundedCornerShape(16.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = EditorialEspresso,
                contentColor = Color.White
              )
            ) {
              Text(
                text = "+ Add Memory",
                style = MaterialTheme.typography.labelLarge.copy(
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 14.sp
                )
              )
            }

            Button(
              onClick = onMessageClick,
              modifier = Modifier
                .height(48.dp)
                .padding(horizontal = 4.dp),
              shape = RoundedCornerShape(16.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = EditorialEspresso
              )
            ) {
              Text(
                text = "Chat",
                style = MaterialTheme.typography.labelLarge.copy(
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 14.sp
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
              )
            }
          }
        }
      }
    }

    // Section: On This Day ❤️
    item {
      Spacer(modifier = Modifier.height(22.dp))
      val onThisDay = SampleData.onThisDayMemory
      Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "On this day",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
              color = EditorialEspresso
            )
          )

          Text(
            text = "2018 • View All",
            style = MaterialTheme.typography.labelMedium.copy(
              color = EditorialMauve,
              fontWeight = FontWeight.Medium,
              fontSize = 12.sp
            ),
            modifier = Modifier.clickable { onViewAllMemories() }
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clickable { onViewAllMemories() },
          shape = RoundedCornerShape(32.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFE5E1E6)),
          elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
          Box(modifier = Modifier.fillMaxSize()) {
            Image(
              painter = painterResource(id = R.drawable.hero_family),
              contentDescription = onThisDay.title,
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )

            // Dark gradient overlay for editorial legibility
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(
                  Brush.verticalGradient(
                    colors = listOf(
                      Color.Black.copy(alpha = 0.15f),
                      Color.Black.copy(alpha = 0.78f)
                    )
                  )
                )
            )

            // Centered nostalgic quote tag
            Box(
              modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 20.dp)
            ) {
              Surface(
                color = Color.Black.copy(alpha = 0.35f),
                shape = RoundedCornerShape(16.dp)
              ) {
                Text(
                  text = "“Childhood blanket fortress under the stars...”",
                  style = EditorialQuoteStyle.copy(
                    color = Color.White.copy(alpha = 0.95f),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                  ),
                  modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
              }
            }

            // Bottom editorial metadata and title
            Column(
              modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
            ) {
              Text(
                text = "MAY 24, 2018",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = Color.White.copy(alpha = 0.85f),
                  fontWeight = FontWeight.Medium,
                  letterSpacing = 1.5.sp,
                  fontSize = 11.sp
                )
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = onThisDay.title,
                style = MaterialTheme.typography.titleLarge.copy(
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 20.sp,
                  lineHeight = 26.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
              )
            }
          }
        }
      }
    }


    // Section: Upcoming Dates & Birthdays
    item {
      Spacer(modifier = Modifier.height(24.dp))
      Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
          text = "Upcoming",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = EditorialEspresso
          ),
          modifier = Modifier.padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          SampleData.sampleImportantDates.forEach { dateItem ->
            UpcomingDateCard(item = dateItem)
          }
        }
      }
    }

    // Section: Your Memories (Recent timeline highlights)
    item {
      Spacer(modifier = Modifier.height(26.dp))
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Your Memories",
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = EditorialEspresso
          )
        )

        TextButton(onClick = onViewAllMemories) {
          Text(
            text = "View Timeline",
            style = MaterialTheme.typography.labelLarge.copy(
              color = EditorialMauve,
              fontWeight = FontWeight.SemiBold
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(4.dp))
    }

    // List of recent memories
    items(memories.take(3)) { memory ->
      Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
        HomeMemoryCard(memory = memory, onCardClick = onViewAllMemories)
      }
    }
  }
}

@Composable
fun CounterItem(count: String, label: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text = count,
      style = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Bold,
        color = EditorialEspresso
      )
    )
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall.copy(
        color = EditorialMauve,
        letterSpacing = 0.5.sp
      )
    )
  }
}

@Composable
fun UpcomingDateCard(item: ImportantDateItem) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(26.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBorder)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Box(
        modifier = Modifier
          .size(48.dp)
          .clip(CircleShape)
          .background(EditorialDustyRose),
        contentAlignment = Alignment.Center
      ) {
        Text(text = "🎂", fontSize = 22.sp)
      }

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = item.title,
          style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = EditorialEspresso
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = "Coming up in ${item.daysRemaining} days • ${item.dateString}",
          style = MaterialTheme.typography.bodySmall.copy(
            color = EditorialMauve,
            fontSize = 12.sp
          )
        )
      }

      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(Color(0xFFFCF8F7))
          .border(1.dp, EditorialBorder, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Text(text = "🔔", fontSize = 16.sp)
      }
    }
  }
}

@Composable
fun HomeMemoryCard(memory: MemoryItem, onCardClick: () -> Unit) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onCardClick() },
    shape = RoundedCornerShape(28.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBorder)
  ) {
    Column {
      if (memory.drawableRes != null) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(22.dp))
        ) {
          Image(
            painter = painterResource(id = memory.drawableRes),
            contentDescription = memory.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )

          Surface(
            color = Color.Black.copy(alpha = 0.55f),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(10.dp)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = if (memory.isVideo) Icons.Default.Videocam else Icons.Default.Image,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = memory.year,
                style = MaterialTheme.typography.labelSmall.copy(
                  color = Color.White,
                  fontWeight = FontWeight.Bold
                )
              )
            }
          }
        }
      }

      Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = memory.title,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = EditorialEspresso
            ),
            modifier = Modifier.weight(1f)
          )
          Text(
            text = memory.dateString,
            style = MaterialTheme.typography.labelSmall.copy(
              color = EditorialMauve,
              fontWeight = FontWeight.Medium
            )
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = memory.description,
          style = MaterialTheme.typography.bodyMedium.copy(
            color = EditorialMauve,
            lineHeight = 20.sp
          ),
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )

        if (memory.location.isNotBlank()) {
          Spacer(modifier = Modifier.height(8.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = null,
              tint = EditorialMauve,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = memory.location,
              style = MaterialTheme.typography.labelSmall.copy(
                color = EditorialMauve
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Added by ${memory.authorName}",
            style = MaterialTheme.typography.labelSmall.copy(
              color = EditorialMauve.copy(alpha = 0.8f)
            )
          )

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Text(
              text = "❤️ ${memory.reactionsCount}",
              style = MaterialTheme.typography.labelSmall.copy(
                color = EditorialMauve,
                fontWeight = FontWeight.Medium
              )
            )
            Text(
              text = "💬 ${memory.commentsCount}",
              style = MaterialTheme.typography.labelSmall.copy(
                color = EditorialMauve,
                fontWeight = FontWeight.Medium
              )
            )
          }
        }
      }
    }
  }
}

