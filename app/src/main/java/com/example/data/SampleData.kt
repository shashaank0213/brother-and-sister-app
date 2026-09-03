package com.example.data

import com.example.R
import com.example.model.AlbumItem
import com.example.model.ChatMessage
import com.example.model.ImportantDateItem
import com.example.model.MemoryItem
import com.example.model.SiblingConnection
import com.example.model.UserProfile

object SampleData {

  val currentUser = UserProfile(
    id = "user_shashank",
    firstName = "Shashank",
    email = "shashaank0213@gmail.com",
    avatarResId = R.drawable.ic_app_logo,
    dateOfBirth = "15 August 1998",
    gender = "Male",
    country = "India",
    city = "Bengaluru",
    bio = "Big brother, memory keeper. Here to cherish every childhood secret and grown-up milestone with my sister.",
    interests = listOf("Family Trips", "Photography", "Festival Celebrations", "Old Stories"),
    memoryCount = 42,
    albumCount = 7,
    connectedSiblingName = "Ananya",
    isConnected = true
  )

  val siblingConnection = SiblingConnection(
    connectionId = "conn_shashank_ananya",
    siblingName = "Ananya",
    siblingRole = "Sister",
    connectedSince = "May 2020",
    sharedMemoryCount = 42,
    photoCount = 128,
    videoCount = 14
  )

  val sampleMemories = listOf(
    MemoryItem(
      id = "mem_1",
      title = "Our First Family Trip",
      description = "One of my favorite memories from our childhood. We woke up at 4 AM, packed sandwiches, and sang old car songs all the way to the hill station.",
      dateString = "20 May 2015",
      year = "2015",
      category = "Trips",
      location = "Ooty, Tamil Nadu",
      drawableRes = R.drawable.hero_family,
      isVideo = false,
      authorName = "Shashank",
      reactionsCount = 6,
      commentsCount = 4,
      tags = listOf("RoadTrip", "Childhood", "Family"),
      isFavorite = true
    ),
    MemoryItem(
      id = "mem_2",
      title = "School Days — The Science Fair",
      description = "Remember when our volcano model erupted all over the judges' table? We were so scared, but we ended up winning the creative prize!",
      dateString = "12 November 2018",
      year = "2018",
      category = "Childhood",
      location = "St. Paul's High School",
      drawableRes = R.drawable.hero_family,
      isVideo = false,
      authorName = "Ananya",
      reactionsCount = 9,
      commentsCount = 8,
      tags = listOf("School", "ScienceFair", "Fun"),
      isFavorite = true
    ),
    MemoryItem(
      id = "mem_3",
      title = "Childhood Birthday — The Chocolate Cake War",
      description = "Ananya turned 7 and decided the chocolate icing belonged on everyone's face rather than on the plates.",
      dateString = "28 July 2012",
      year = "Childhood",
      category = "Birthdays",
      location = "Grandma's House",
      drawableRes = R.drawable.hero_family,
      isVideo = false,
      authorName = "Shashank",
      reactionsCount = 12,
      commentsCount = 6,
      tags = listOf("Birthday", "Cousins", "SweetMemories"),
      isFavorite = true
    ),
    MemoryItem(
      id = "mem_4",
      title = "Festival Memories — Diwali Lights & Sweets",
      description = "Making handmade diyas on the rooftop together. The entire terrace smelled like jasmine and homemade ladoos.",
      dateString = "4 November 2022",
      year = "2022",
      category = "Festivals",
      location = "Home Balcony",
      drawableRes = R.drawable.hero_family,
      isVideo = false,
      authorName = "Ananya",
      reactionsCount = 15,
      commentsCount = 5,
      tags = listOf("Diwali", "Tradition", "Warmth"),
      isFavorite = true
    ),
    MemoryItem(
      id = "mem_5",
      title = "College Farewell Surprise",
      description = "You travelled 300km just to be there at my convocation ceremony. Best surprise brother ever!",
      dateString = "18 June 2025",
      year = "2025",
      category = "Family",
      location = "University Campus",
      drawableRes = R.drawable.hero_family,
      isVideo = false,
      authorName = "Shashank",
      reactionsCount = 8,
      commentsCount = 3,
      tags = listOf("Milestones", "Pride", "BrotherSister"),
      isFavorite = false
    ),
    MemoryItem(
      id = "mem_6",
      title = "Baking Cookies in the Rain",
      description = "Torrential monsoon rain outside, warm cinnamon cookies in the oven, and fighting over who gets the mixing bowl spoon.",
      dateString = "14 August 2026",
      year = "2026",
      category = "Childhood",
      location = "Kitchen",
      drawableRes = R.drawable.hero_family,
      isVideo = true,
      authorName = "Ananya",
      reactionsCount = 5,
      commentsCount = 2,
      tags = listOf("Monsoon", "Baking", "Laughs"),
      isFavorite = false
    )
  )

  val sampleAlbums = listOf(
    AlbumItem(
      id = "alb_childhood",
      title = "Childhood ❤️",
      description = "Our earliest adventures, silly fights, and playground triumphs.",
      mediaCount = 48,
      coverDrawableRes = R.drawable.hero_family,
      createdYear = "Childhood"
    ),
    AlbumItem(
      id = "alb_school",
      title = "School Days",
      description = "Uniforms, lunchbox trades, annual days, and homework pacts.",
      mediaCount = 24,
      coverDrawableRes = R.drawable.hero_family,
      createdYear = "2014-2019"
    ),
    AlbumItem(
      id = "alb_college",
      title = "College Days",
      description = "Hostel visits, late night calls, exam cheerups, and convocations.",
      mediaCount = 18,
      coverDrawableRes = R.drawable.hero_family,
      createdYear = "2020-2024"
    ),
    AlbumItem(
      id = "alb_trips",
      title = "Family Trips",
      description = "Train journeys, beach sunsets, snowy hills, and highway dhabas.",
      mediaCount = 36,
      coverDrawableRes = R.drawable.hero_family,
      createdYear = "2015-2025"
    ),
    AlbumItem(
      id = "alb_birthdays",
      title = "Birthdays 🎂",
      description = "Every candle blown, every surprise party planned with love.",
      mediaCount = 19,
      coverDrawableRes = R.drawable.hero_family,
      createdYear = "All Years"
    ),
    AlbumItem(
      id = "alb_festivals",
      title = "Festivals",
      description = "Raksha Bandhan threads, Diwali lights, Holi colors, and sweet plates.",
      mediaCount = 31,
      coverDrawableRes = R.drawable.hero_family,
      createdYear = "All Years"
    ),
    AlbumItem(
      id = "alb_funny",
      title = "Funny Memories 😂",
      description = "Goofy faces, candid fails, and things we promised never to show mom.",
      mediaCount = 15,
      coverDrawableRes = R.drawable.hero_family,
      createdYear = "All Years"
    )
  )

  val sampleChatMessages = listOf(
    ChatMessage(
      id = "msg_1",
      senderId = "user_ananya",
      senderName = "Ananya",
      text = "Hey bhaiya! Look what I found in grandma's old album box ❤️",
      timestamp = "10:15 AM",
      isFromMe = false,
      hasPhoto = true,
      photoDrawableRes = R.drawable.hero_family,
      isRead = true,
      reaction = "❤️"
    ),
    ChatMessage(
      id = "msg_2",
      senderId = "user_shashank",
      senderName = "Shashank",
      text = "Oh wow, is that from our first trip to Ooty?! Look at my funny haircut 😂",
      timestamp = "10:17 AM",
      isFromMe = true,
      isRead = true,
      reaction = "😂"
    ),
    ChatMessage(
      id = "msg_3",
      senderId = "user_ananya",
      senderName = "Ananya",
      text = "Yes! You insisted on wearing that red cap everywhere for two whole months.",
      timestamp = "10:18 AM",
      isFromMe = false,
      isRead = true
    ),
    ChatMessage(
      id = "msg_4",
      senderId = "user_shashank",
      senderName = "Shashank",
      text = "I just created a memory card for it in the Trips album so it stays saved forever.",
      timestamp = "10:20 AM",
      isFromMe = true,
      isRead = true
    ),
    ChatMessage(
      id = "msg_5",
      senderId = "user_ananya",
      senderName = "Ananya",
      text = "Yay! Also don't forget it's 5 days to mom's surprise dinner. Let's pick photos for the slideshow tonight!",
      timestamp = "10:22 AM",
      isFromMe = false,
      isRead = true,
      reaction = "🎂"
    )
  )

  val sampleImportantDates = listOf(
    ImportantDateItem(
      id = "date_1",
      title = "Sister's Birthday 🎂",
      dateString = "September 18",
      daysRemaining = 15,
      type = "Birthday",
      isSiblingBirthday = true
    ),
    ImportantDateItem(
      id = "date_2",
      title = "Raksha Bandhan Celebration",
      dateString = "August 28",
      daysRemaining = 5,
      type = "Festival"
    ),
    ImportantDateItem(
      id = "date_3",
      title = "Mom & Dad's Anniversary",
      dateString = "October 10",
      daysRemaining = 37,
      type = "Anniversary"
    )
  )

  val onThisDayMemory = MemoryItem(
    id = "mem_on_this_day",
    title = "On This Day in 2018 ❤️",
    description = "7 years ago today, we built that secret treehouse fort in the backyard. Remember the secret password?",
    dateString = "3 September 2018",
    year = "2018",
    category = "Childhood",
    location = "Backyard Fort",
    drawableRes = R.drawable.hero_family,
    authorName = "Shashank",
    reactionsCount = 14,
    commentsCount = 7,
    tags = listOf("OnThisDay", "SecretFort", "ChildhoodMagic"),
    isFavorite = true
  )
}
