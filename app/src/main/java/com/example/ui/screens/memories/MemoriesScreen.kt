package com.example.ui.screens.memories

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.data.SampleData
import com.example.model.MemoryItem
import com.example.ui.theme.HeartRed

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MemoriesScreen(
  memoriesList: List<MemoryItem> = SampleData.sampleMemories,
  onAddMemoryClick: () -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("All") }
  var selectedYear by remember { mutableStateOf("All") }
  val reactedMap = remember { mutableStateMapOf<String, Boolean>() }

  val categories = listOf("All", "Photos", "Videos", "Childhood", "Birthdays", "Trips", "Festivals", "Family")
  val timelineYears = listOf("All", "2026", "2025", "2022", "2018", "2015", "Childhood")

  val filteredMemories = memoriesList.filter { memory ->
    val matchesSearch = searchQuery.isBlank() ||
      memory.title.contains(searchQuery, ignoreCase = true) ||
      memory.description.contains(searchQuery, ignoreCase = true) ||
      memory.location.contains(searchQuery, ignoreCase = true) ||
      memory.tags.any { it.contains(searchQuery, ignoreCase = true) }

    val matchesCategory = when (selectedCategory) {
      "All" -> true
      "Photos" -> memory.drawableRes != null && !memory.isVideo
      "Videos" -> memory.isVideo
      else -> memory.category.equals(selectedCategory, ignoreCase = true) ||
        memory.tags.any { it.equals(selectedCategory, ignoreCase = true) }
    }

    val matchesYear = when (selectedYear) {
      "All" -> true
      else -> memory.year.equals(selectedYear, ignoreCase = true)
    }

    matchesSearch && matchesCategory && matchesYear
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(bottom = 96.dp)
    ) {
      // Header
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Memory Timeline",
                style = MaterialTheme.typography.headlineLarge.copy(
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              )
              Text(
                text = "Chronological archive of our journey together",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              )
            }

            Surface(
              color = MaterialTheme.colorScheme.primaryContainer,
              shape = RoundedCornerShape(12.dp)
            ) {
              Text(
                text = "${filteredMemories.size} Memories",
                style = MaterialTheme.typography.labelMedium.copy(
                  color = MaterialTheme.colorScheme.onPrimaryContainer,
                  fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Search field
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search memories, trips, tags, locations...") },
            leadingIcon = {
              Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary)
            },
            trailingIcon = {
              if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { searchQuery = "" }) {
                  Icon(Icons.Default.Clear, contentDescription = "Clear")
                }
              }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = MaterialTheme.colorScheme.surface,
              unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Year timeline quick selector
          Text(
            text = "TIMELINE YEAR",
            style = MaterialTheme.typography.labelSmall.copy(
              color = MaterialTheme.colorScheme.outline,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )
          )
          Spacer(modifier = Modifier.height(6.dp))
          LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(timelineYears) { yr ->
              val isSelected = selectedYear == yr
              Surface(
                onClick = { selectedYear = yr },
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.height(34.dp)
              ) {
                Box(
                  contentAlignment = Alignment.Center,
                  modifier = Modifier.padding(horizontal = 14.dp)
                ) {
                  Text(
                    text = yr,
                    style = MaterialTheme.typography.labelMedium.copy(
                      fontWeight = FontWeight.SemiBold,
                      color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Category filter chips
          LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories) { cat ->
              val isSelected = selectedCategory == cat
              FilterChip(
                selected = isSelected,
                onClick = { selectedCategory = cat },
                label = { Text(cat) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                  selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
              )
            }
          }
        }
      }

      // Memory Items with timeline connector
      if (filteredMemories.isEmpty()) {
        item {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Box(
              modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(36.dp)
              )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
              text = "No memories yet ❤️",
              style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Start creating your first brother/sister memory.",
              style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
              ),
              textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextButton(onClick = onAddMemoryClick) {
              Text("Add First Memory", fontWeight = FontWeight.Bold)
            }
          }
        }
      } else {
        items(filteredMemories) { memory ->
          TimelineMemoryCard(
            memory = memory,
            isReacted = reactedMap[memory.id] ?: false,
            onToggleReaction = {
              reactedMap[memory.id] = !(reactedMap[memory.id] ?: false)
            }
          )
        }
      }
    }
  }
}

@Composable
fun TimelineMemoryCard(
  memory: MemoryItem,
  isReacted: Boolean,
  onToggleReaction: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
  ) {
    // Left timeline axis
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.width(36.dp)
    ) {
      Box(
        modifier = Modifier
          .size(14.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primary)
      )
      Box(
        modifier = Modifier
          .width(2.dp)
          .height(160.dp)
          .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
      )
    }

    Spacer(modifier = Modifier.width(8.dp))

    // Main Memory Card
    Card(
      modifier = Modifier.weight(1f),
      shape = RoundedCornerShape(22.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      border = androidx.compose.foundation.BorderStroke(
        1.dp,
        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
      )
    ) {
      Column {
        if (memory.drawableRes != null) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(180.dp)
          ) {
            Image(
              painter = painterResource(id = memory.drawableRes),
              contentDescription = memory.title,
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )

            // Year & Media Badge
            Surface(
              color = Color.Black.copy(alpha = 0.6f),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier
                .align(Alignment.TopStart)
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

            // Category badge
            Surface(
              color = MaterialTheme.colorScheme.primaryContainer,
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
            ) {
              Text(
                text = memory.category,
                style = MaterialTheme.typography.labelSmall.copy(
                  color = MaterialTheme.colorScheme.onPrimaryContainer,
                  fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }
        }

        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = memory.dateString,
            style = MaterialTheme.typography.labelSmall.copy(
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.SemiBold
            )
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = memory.title,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = memory.description,
            style = MaterialTheme.typography.bodyMedium.copy(
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              lineHeight = 22.sp
            )
          )

          if (memory.location.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = memory.location,
                style = MaterialTheme.typography.labelSmall.copy(
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Footer with author and reaction toggles
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "By ${memory.authorName}",
              style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.outline
              )
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
              // Heart reaction
              Surface(
                onClick = onToggleReaction,
                shape = RoundedCornerShape(12.dp),
                color = if (isReacted) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.height(32.dp)
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 8.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = if (isReacted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (isReacted) HeartRed else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "${memory.reactionsCount + if (isReacted) 1 else 0}",
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold
                    )
                  )
                }
              }

              Spacer(modifier = Modifier.width(8.dp))

              // Comments count
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.height(32.dp)
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 8.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = Icons.Default.ChatBubbleOutline,
                    contentDescription = "Comments",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "${memory.commentsCount}",
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold
                    )
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
