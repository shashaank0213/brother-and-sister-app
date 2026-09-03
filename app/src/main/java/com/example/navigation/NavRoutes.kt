package com.example.navigation

sealed class Screen(val route: String) {
  object Splash : Screen("splash")
  object Onboarding : Screen("onboarding")
  object Welcome : Screen("welcome")
  object Login : Screen("login")
  object Register : Screen("register")
  object ForgotPassword : Screen("forgot_password")
  
  // Main app shell
  object Main : Screen("main")

  // Main app tabs
  object Home : Screen("home")
  object Memories : Screen("memories")
  object Chat : Screen("chat")
  object Albums : Screen("albums")
  object Profile : Screen("profile")
  
  // Feature screens
  object ConnectSibling : Screen("connect_sibling")
  object AddMemory : Screen("add_memory")
}
