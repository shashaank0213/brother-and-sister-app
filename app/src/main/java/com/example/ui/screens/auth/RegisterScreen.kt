package com.example.ui.screens.auth

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RegisterScreen(
  onBack: () -> Unit,
  onRegisterSuccess: () -> Unit,
  onNavigateToLogin: () -> Unit
) {
  var firstName by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var dateOfBirth by remember { mutableStateOf("") }
  var gender by remember { mutableStateOf("Brother") }
  var country by remember { mutableStateOf("") }
  var city by remember { mutableStateOf("") }
  var bio by remember { mutableStateOf("") }
  var photoSelected by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  var isLoading by remember { mutableStateOf(false) }

  val auth = remember { FirebaseAuth.getInstance() }
  val firestore = remember { FirebaseFirestore.getInstance() }

  val availableInterests = listOf(
    "Childhood Stories", "Family Road Trips", "Festival Cooking",
    "School Nostalgia", "Photography", "Board Games", "Old Music", "Celebrations"
  )
  val selectedInterests = remember { mutableStateListOf<String>() }

  Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
    Column(
      modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
      Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text("Create Profile", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
      }

      Spacer(modifier = Modifier.height(20.dp))
      Text("Your Family Identity", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
      Text("Only your connected brother or sister will ever see this information.", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
      Spacer(modifier = Modifier.height(24.dp))

      Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Box(
            modifier = Modifier.size(96.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer)
              .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape).clickable { photoSelected = !photoSelected },
            contentAlignment = Alignment.Center
          ) {
            if (photoSelected) {
              Image(painter = painterResource(id = R.drawable.ic_app_logo), contentDescription = "Profile Photo", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            } else {
              Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Add Photo", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
            }
          }
          Spacer(modifier = Modifier.height(8.dp))
          Text(if (photoSelected) "Tap to change photo" else "Add profile photo", style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium))
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
      OutlinedTextField(value = firstName, onValueChange = { firstName = it; errorMessage = null }, label = { Text("First Name *") }, leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), singleLine = true)
      Spacer(modifier = Modifier.height(14.dp))
      OutlinedTextField(value = email, onValueChange = { email = it; errorMessage = null }, label = { Text("Email Address *") }, leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
      Spacer(modifier = Modifier.height(14.dp))
      OutlinedTextField(value = password, onValueChange = { password = it; errorMessage = null }, label = { Text("Password *") }, leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
      Spacer(modifier = Modifier.height(14.dp))
      OutlinedTextField(value = dateOfBirth, onValueChange = { dateOfBirth = it }, label = { Text("Date of Birth / Age") }, leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), singleLine = true)
      Spacer(modifier = Modifier.height(14.dp))

      Text("I am a (Optional):", style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium))
      Spacer(modifier = Modifier.height(6.dp))
      Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        listOf("Brother", "Sister", "Prefer not to say").forEach { role ->
          val isSelected = gender == role
          FilterChip(selected = isSelected, onClick = { gender = role }, label = { Text(role) }, leadingIcon = if (isSelected) ({ Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }) else null, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primaryContainer, selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer))
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(value = country, onValueChange = { country = it }, label = { Text("Country") }, leadingIcon = { Icon(Icons.Default.Public, contentDescription = null) }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp), singleLine = true)
        OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City / State (Optional)") }, leadingIcon = { Icon(Icons.Default.LocationCity, contentDescription = null) }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp), singleLine = true)
      }

      Spacer(modifier = Modifier.height(14.dp))
      OutlinedTextField(value = bio, onValueChange = { bio = it }, label = { Text("Family Bio / Note to Sibling") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), minLines = 2, maxLines = 4)
      Spacer(modifier = Modifier.height(18.dp))

      Text("Shared Family Interests:", style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium))
      Spacer(modifier = Modifier.height(8.dp))
      FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        availableInterests.forEach { interest ->
          val isSelected = selectedInterests.contains(interest)
          FilterChip(selected = isSelected, onClick = { if (isSelected) selectedInterests.remove(interest) else selectedInterests.add(interest) }, label = { Text(interest) }, leadingIcon = if (isSelected) ({ Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }) else null)
        }
      }

      if (errorMessage != null) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(errorMessage!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
      }

      Spacer(modifier = Modifier.height(28.dp))
      Button(
        enabled = !isLoading,
        onClick = {
          val cleanEmail = email.trim()
          when {
            firstName.isBlank() -> errorMessage = "Please enter your first name."
            cleanEmail.isBlank() -> errorMessage = "Please enter your email address."
            password.length < 6 -> errorMessage = "Password must be at least 6 characters."
            else -> {
              isLoading = true
              errorMessage = null
              auth.createUserWithEmailAndPassword(cleanEmail, password).addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                  isLoading = false
                  errorMessage = task.exception?.localizedMessage ?: "Registration failed. Please try again."
                  return@addOnCompleteListener
                }

                val uid = auth.currentUser?.uid
                if (uid == null) {
                  isLoading = false
                  errorMessage = "Account was created, but the user ID could not be found. Please try again."
                  return@addOnCompleteListener
                }

                val profile = hashMapOf<String, Any>(
                  "firstName" to firstName.trim(),
                  "email" to cleanEmail,
                  "dateOfBirth" to dateOfBirth.trim(),
                  "gender" to gender,
                  "country" to country.trim(),
                  "city" to city.trim(),
                  "bio" to bio.trim(),
                  "interests" to selectedInterests.toList(),
                  "profilePhoto" to "",
                  "createdAt" to FieldValue.serverTimestamp()
                )

                firestore.collection("users").document(uid).set(profile)
                  .addOnSuccessListener {
                    isLoading = false
                    onRegisterSuccess()
                  }
                  .addOnFailureListener { exception ->
                    isLoading = false
                    errorMessage = exception.localizedMessage ?: "Account created, but the profile could not be saved. Please try again."
                  }
              }
            }
          }
        },
        modifier = Modifier.fillMaxWidth().height(54.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
      ) {
        Text(if (isLoading) "Creating Account..." else "Create Family Space", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
      }

      Spacer(modifier = Modifier.height(16.dp))
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        Text("Already registered?", style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Sign In", style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold), modifier = Modifier.clickable { onNavigateToLogin() })
      }
      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}
