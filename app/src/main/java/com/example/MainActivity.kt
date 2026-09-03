package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.navigation.Screen
import com.example.ui.screens.MainAppShell
import com.example.ui.screens.auth.ForgotPasswordScreen
import com.example.ui.screens.auth.LoginScreen
import com.example.ui.screens.auth.RegisterScreen
import com.example.ui.screens.auth.WelcomeScreen
import com.example.ui.screens.connection.ConnectSiblingScreen
import com.example.ui.screens.onboarding.OnboardingScreen
import com.example.ui.screens.splash.SplashScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          BrotherSisterApp()
        }
      }
    }
  }
}

@Composable
fun BrotherSisterApp() {
  val navController = rememberNavController()

  NavHost(
    navController = navController,
    startDestination = Screen.Splash.route
  ) {
    composable(Screen.Splash.route) {
      SplashScreen(
        onNavigateNext = {
          navController.navigate(Screen.Onboarding.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
          }
        }
      )
    }

    composable(Screen.Onboarding.route) {
      OnboardingScreen(
        onConnectBrotherSister = {
          navController.navigate(Screen.ConnectSibling.route)
        },
        onCreatePrivateSpace = {
          navController.navigate(Screen.Register.route)
        },
        onNavigateToAuth = {
          navController.navigate(Screen.Welcome.route)
        }
      )
    }

    composable(Screen.Welcome.route) {
      WelcomeScreen(
        onNavigateToLogin = {
          navController.navigate(Screen.Login.route)
        },
        onNavigateToRegister = {
          navController.navigate(Screen.Register.route)
        },
        onExploreDemo = {
          navController.navigate(Screen.Main.route) {
            popUpTo(Screen.Welcome.route) { inclusive = true }
          }
        }
      )
    }

    composable(Screen.Login.route) {
      LoginScreen(
        onBack = { navController.popBackStack() },
        onLoginSuccess = {
          navController.navigate(Screen.Main.route) {
            popUpTo(Screen.Welcome.route) { inclusive = true }
          }
        },
        onForgotPassword = {
          navController.navigate(Screen.ForgotPassword.route)
        },
        onNavigateToRegister = {
          navController.navigate(Screen.Register.route)
        }
      )
    }

    composable(Screen.Register.route) {
      RegisterScreen(
        onBack = { navController.popBackStack() },
        onRegisterSuccess = {
          navController.navigate(Screen.Main.route) {
            popUpTo(Screen.Welcome.route) { inclusive = true }
          }
        },
        onNavigateToLogin = {
          navController.navigate(Screen.Login.route)
        }
      )
    }

    composable(Screen.ForgotPassword.route) {
      ForgotPasswordScreen(
        onBack = { navController.popBackStack() }
      )
    }

    composable(Screen.ConnectSibling.route) {
      ConnectSiblingScreen(
        onBack = { navController.popBackStack() },
        onConnectedSuccess = {
          navController.navigate(Screen.Main.route) {
            popUpTo(Screen.Onboarding.route) { inclusive = true }
          }
        }
      )
    }

    composable(Screen.Main.route) {
      MainAppShell(
        onLogout = {
          com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
          navController.navigate(Screen.Welcome.route) {
            popUpTo(Screen.Main.route) {
              this.inclusive = true
            }
          }
        }
      )
    }
  }
}

// Retained for existing test suite compatibility
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}
