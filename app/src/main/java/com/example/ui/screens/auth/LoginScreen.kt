package com.example.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ui.theme.HeartRed
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun LoginScreen(
  onBack: () -> Unit,
  onLoginSuccess: () -> Unit,
  onForgotPassword: () -> Unit,
  onNavigateToRegister: () -> Unit
) {
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var passwordVisible by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  var isLoading by remember { mutableStateOf(false) }
  val auth = remember { FirebaseAuth.getInstance() }
  val firestore = remember { FirebaseFirestore.getInstance() }

  Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
      Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
        Spacer(modifier = Modifier.width(8.dp))
        Text("Sign In", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
      }

      Spacer(modifier = Modifier.height(32.dp))
      Box(modifier = Modifier.size(64.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
        Icon(Icons.Default.Favorite, contentDescription = null, tint = HeartRed, modifier = Modifier.size(32.dp))
      }
      Spacer(modifier = Modifier.height(16.dp))
      Text("Welcome Back", style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold))
      Spacer(modifier = Modifier.height(6.dp))
      Text("Sign in to access your private brother/sister space", style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
      Spacer(modifier = Modifier.height(32.dp))

      OutlinedTextField(value = email, onValueChange = { email = it; errorMessage = null }, label = { Text("Email Address") }, leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
      Spacer(modifier = Modifier.height(16.dp))
      OutlinedTextField(value = password, onValueChange = { password = it; errorMessage = null }, label = { Text("Password") }, leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) }, trailingIcon = { IconButton(onClick = { passwordVisible = !passwordVisible }) { Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = if (passwordVisible) "Hide password" else "Show password") } }, visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))

      Box(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), contentAlignment = Alignment.CenterEnd) {
        TextButton(onClick = onForgotPassword) { Text("Forgot Password?", style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary)) }
      }

      if (errorMessage != null) {
        androidx.compose.material3.Card(
          modifier = Modifier.fillMaxWidth(),
          colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
          ),
          shape = RoundedCornerShape(12.dp)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = errorMessage!!,
              style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onErrorContainer)
            )
            if (errorMessage!!.contains("Tap 'Create Account'") || errorMessage!!.contains("Incorrect email or password")) {
              Spacer(modifier = Modifier.height(8.dp))
              Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                TextButton(
                  onClick = onNavigateToRegister,
                  colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) {
                  Text("Create Account", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                }
                TextButton(
                  onClick = onForgotPassword,
                  colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
                ) {
                  Text("Reset Password", style = MaterialTheme.typography.labelMedium)
                }
              }
            }
          }
        }
      }
      Spacer(modifier = Modifier.height(16.dp))

      Button(
        enabled = !isLoading,
        onClick = {
          val cleanEmail = email.trim().lowercase()
          if (cleanEmail.isBlank() || password.isBlank()) {
            errorMessage = "Please enter both your email and password."
          } else {
            isLoading = true
            errorMessage = null
            auth.signInWithEmailAndPassword(cleanEmail, password).addOnCompleteListener { task ->
              if (!task.isSuccessful) {
                isLoading = false
                val ex = task.exception
                val rawMsg = ex?.localizedMessage.orEmpty()
                errorMessage = when {
                  ex is com.google.firebase.auth.FirebaseAuthInvalidUserException ->
                    "No account found with this email. Tap 'Create Account' below to sign up."
                  ex is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException ||
                  rawMsg.contains("credential", ignoreCase = true) ||
                  rawMsg.contains("malformed", ignoreCase = true) ||
                  rawMsg.contains("expired", ignoreCase = true) ->
                    "Incorrect email or password. Please verify your credentials or tap 'Forgot Password?' to reset."
                  ex is com.google.firebase.FirebaseNetworkException ->
                    "Network error. Please check your internet connection and try again."
                  else -> rawMsg.ifBlank { "Sign in failed. Please check your email and password." }
                }
                return@addOnCompleteListener
              }

              val firebaseUser = auth.currentUser
              if (firebaseUser == null) {
                isLoading = false
                errorMessage = "Signed in, but the account could not be loaded. Please try again."
                return@addOnCompleteListener
              }

              val uid = firebaseUser.uid
              val userEmail = firebaseUser.email?.trim()?.lowercase() ?: cleanEmail.lowercase()
              val userDocument = firestore.collection("users").document(uid)

              // Some accounts were created before Firestore profile creation was added.
              // Create a minimal profile on first login so sibling email lookup can find them.
              userDocument.get()
                .addOnSuccessListener { document ->
                  if (document.exists()) {
                    isLoading = false
                    onLoginSuccess()
                  } else {
                    val fallbackName = userEmail.substringBefore("@").replaceFirstChar { it.uppercase() }
                    val profile = hashMapOf<String, Any>(
                      "firstName" to fallbackName,
                      "email" to userEmail,
                      "dateOfBirth" to "",
                      "gender" to "",
                      "country" to "India",
                      "city" to "",
                      "bio" to "",
                      "interests" to emptyList<String>(),
                      "profilePhoto" to "",
                      "createdAt" to FieldValue.serverTimestamp()
                    )

                    userDocument.set(profile)
                      .addOnSuccessListener {
                        isLoading = false
                        onLoginSuccess()
                      }
                      .addOnFailureListener { exception ->
                        isLoading = false
                        errorMessage = exception.localizedMessage ?: "Signed in, but your profile could not be created."
                      }
                  }
                }
                .addOnFailureListener { exception ->
                  isLoading = false
                  errorMessage = exception.localizedMessage ?: "Signed in, but your profile could not be loaded."
                }
            }
          }
        },
        modifier = Modifier.fillMaxWidth().height(54.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
      ) { Text(if (isLoading) "Signing In..." else "Sign In", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)) }

      Spacer(modifier = Modifier.height(16.dp))
      OutlinedButton(onClick = { errorMessage = "Google Sign-In is not enabled yet. Please use email and password." }, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp)) {
        Text("Continue with Google", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium))
      }

      Spacer(modifier = Modifier.height(32.dp))
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        Text("Don't have an account?", style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Create Account", style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold), modifier = Modifier.clickable { onNavigateToRegister() })
      }
    }
  }
}
