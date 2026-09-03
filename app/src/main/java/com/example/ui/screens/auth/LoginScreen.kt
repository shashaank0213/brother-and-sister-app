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
import androidx.compose.ui.unit.sp
import com.example.ui.theme.HeartRed

@Composable
fun LoginScreen(
  onBack: () -> Unit,
  onLoginSuccess: () -> Unit,
  onForgotPassword: () -> Unit,
  onNavigateToRegister: () -> Unit
) {
  var email by remember { mutableStateOf("shashaank0213@gmail.com") }
  var password by remember { mutableStateOf("brotherSister123") }
  var passwordVisible by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 24.dp, vertical = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Top row with Back
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier.size(48.dp)
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = MaterialTheme.colorScheme.onSurface
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Sign In",
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold
          )
        )
      }

      Spacer(modifier = Modifier.height(32.dp))

      Box(
        modifier = Modifier
          .size(64.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Favorite,
          contentDescription = null,
          tint = HeartRed,
          modifier = Modifier.size(32.dp)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "Welcome Back",
        style = MaterialTheme.typography.headlineLarge.copy(
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "Sign in to access your private brother/sister space",
        style = MaterialTheme.typography.bodyMedium.copy(
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )

      Spacer(modifier = Modifier.height(32.dp))

      // Email field
      OutlinedTextField(
        value = email,
        onValueChange = {
          email = it
          errorMessage = null
        },
        label = { Text("Email Address") },
        leadingIcon = {
          Icon(Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Password field
      OutlinedTextField(
        value = password,
        onValueChange = {
          password = it
          errorMessage = null
        },
        label = { Text("Password") },
        leadingIcon = {
          Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        },
        trailingIcon = {
          IconButton(onClick = { passwordVisible = !passwordVisible }) {
            Icon(
              imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
              contentDescription = if (passwordVisible) "Hide password" else "Show password"
            )
          }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
      )

      // Forgot Password link
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 4.dp),
        contentAlignment = Alignment.CenterEnd
      ) {
        TextButton(onClick = onForgotPassword) {
          Text(
            text = "Forgot Password?",
            style = MaterialTheme.typography.labelLarge.copy(
              color = MaterialTheme.colorScheme.primary
            )
          )
        }
      }

      if (errorMessage != null) {
        Text(
          text = errorMessage!!,
          style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.error),
          modifier = Modifier.padding(vertical = 6.dp)
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Sign In Button
      Button(
        onClick = {
          if (email.isBlank() || password.isBlank()) {
            errorMessage = "Please enter both your email and password."
          } else {
            onLoginSuccess()
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(54.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.primary
        )
      ) {
        Text(
          text = "Sign In",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Google Sign-In button
      OutlinedButton(
        onClick = onLoginSuccess,
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp),
        shape = RoundedCornerShape(16.dp)
      ) {
        Text(
          text = "Continue with Google",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
          )
        )
      }

      Spacer(modifier = Modifier.height(32.dp))

      // Register link
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = "Don't have an account?",
          style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "Create Account",
          style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
          ),
          modifier = Modifier.clickable { onNavigateToRegister() }
        )
      }
    }
  }
}
