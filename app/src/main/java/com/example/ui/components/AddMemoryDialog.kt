package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.model.MemoryItem
import java.util.UUID

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddMemoryDialog(
  onDismiss: () -> Unit,
  onSaveMemory: (MemoryItem) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var dateString by remember { mutableStateOf("Today") }
  var year by remember { mutableStateOf("2026") }
  var location by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("Childhood") }
  var hasPhoto by remember { mutableStateOf(true) }
  var isVideo by remember { mutableStateOf(false) }
  var tagInput by remember { mutableStateOf("Family") }

  val categories = listOf("Childhood", "Trips", "Birthdays", "Festivals", "Family", "Funny")

  AlertDialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false),
    modifier = Modifier
      .fillMaxWidth(0.94f)
      .padding(vertical = 24.dp),
    shape = RoundedCornerShape(28.dp),
    title = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Create Memory ❤️",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
          )
          Text(
            text = "Saved privately to your shared space",
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
          )
        }
        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = "Close")
        }
      }
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(vertical = 8.dp)
      ) {
        // Media Preview / Picker Box
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(20.dp))
            .clickable { hasPhoto = !hasPhoto },
          contentAlignment = Alignment.Center
        ) {
          if (hasPhoto) {
            Image(
              painter = painterResource(id = R.drawable.hero_family),
              contentDescription = "Memory photo preview",
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxWidth()
            )
            Surface(
              color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
            ) {
              Text(
                text = "Tap to change photo",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                Icons.Default.AddPhotoAlternate,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Attach photo or video",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Title
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Memory Title *") },
          placeholder = { Text("e.g. Our First Family Trip") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Date & Year
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = dateString,
            onValueChange = { dateString = it },
            label = { Text("Date") },
            leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(18.dp)) },
            modifier = Modifier.weight(1.3f),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
          )
          OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Year") },
            modifier = Modifier.weight(0.9f),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Description
        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Memory Story / Description") },
          placeholder = { Text("What made this moment special?") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          minLines = 3,
          maxLines = 5
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Location
        OutlinedTextField(
          value = location,
          onValueChange = { location = it },
          label = { Text("Location (Optional)") },
          leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(18.dp)) },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Category selection
        Text(
          text = "Category / Album:",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
        )
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          categories.forEach { cat ->
            val isSelected = selectedCategory == cat
            FilterChip(
              selected = isSelected,
              onClick = { selectedCategory = cat },
              label = { Text(cat) },
              leadingIcon = if (isSelected) {
                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
              } else null
            )
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val cleanTitle = if (title.isBlank()) "Childhood Memory" else title
          val cleanDesc = if (description.isBlank()) "A special shared moment." else description
          val memory = MemoryItem(
            id = "mem_${UUID.randomUUID().toString().take(8)}",
            title = cleanTitle,
            description = cleanDesc,
            dateString = dateString,
            year = year,
            category = selectedCategory,
            location = location,
            drawableRes = if (hasPhoto) R.drawable.hero_family else null,
            isVideo = isVideo,
            authorName = "Shashank",
            reactionsCount = 1,
            commentsCount = 0,
            tags = listOf(selectedCategory, "Special"),
            isFavorite = true
          )
          onSaveMemory(memory)
          onDismiss()
        },
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
      ) {
        Text("Save Memory ❤️", fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}
