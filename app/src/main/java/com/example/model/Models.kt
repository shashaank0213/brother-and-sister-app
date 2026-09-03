package com.example.model

data class UserProfile(
  val id: String = "user_shashank",
  val firstName: String = "Shashank",
  val email: String = "shashaank0213@gmail.com",
  val avatarResId: Int? = null,
  val dateOfBirth: String = "15 August 1998",
  val gender: String = "Male",
  val country: String = "India",
  val city: String = "Bengaluru",
  val bio: String = "Big brother, memory keeper, capturing every smile and adventure.",
  val interests: List<String> = listOf("Photography", "Road Trips", "Family Cooking", "Board Games"),
  val memoryCount: Int = 42,
  val albumCount: Int = 7,
  val connectedSiblingName: String = "Ananya",
  val isConnected: Boolean = true
)

data class SiblingConnection(
  val connectionId: String = "conn_1",
  val siblingName: String = "Ananya",
  val siblingRole: String = "Sister",
  val status: ConnectionStatus = ConnectionStatus.CONNECTED,
  val connectedSince: String = "May 2020",
  val sharedMemoryCount: Int = 42,
  val photoCount: Int = 128,
  val videoCount: Int = 14
)

enum class ConnectionStatus {
  CONNECTED,
  PENDING,
  INVITED
}

data class MemoryItem(
  val id: String,
  val title: String,
  val description: String,
  val dateString: String,
  val year: String,
  val category: String, // "Childhood", "Trips", "Birthdays", "Festivals", "Family"
  val location: String = "",
  val drawableRes: Int? = null,
  val isVideo: Boolean = false,
  val authorName: String,
  val reactionsCount: Int = 0,
  val commentsCount: Int = 0,
  val tags: List<String> = emptyList(),
  val isFavorite: Boolean = false
)

data class AlbumItem(
  val id: String,
  val title: String,
  val description: String,
  val mediaCount: Int,
  val coverDrawableRes: Int? = null,
  val createdYear: String = "2024"
)

data class ChatMessage(
  val id: String,
  val senderId: String,
  val senderName: String,
  val text: String,
  val timestamp: String,
  val isFromMe: Boolean,
  val hasPhoto: Boolean = false,
  val photoDrawableRes: Int? = null,
  val isRead: Boolean = true,
  val reaction: String? = null
)

data class ImportantDateItem(
  val id: String,
  val title: String,
  val dateString: String,
  val daysRemaining: Int,
  val type: String, // "Birthday", "Festival", "Graduation", "Anniversary"
  val isSiblingBirthday: Boolean = false
)
