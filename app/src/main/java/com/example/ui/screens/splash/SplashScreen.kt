package com.example.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.HeartRed
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
  onNavigateNext: () -> Unit
) {
  val scale = remember { Animatable(0.75f) }
  val alpha = remember { Animatable(0f) }

  LaunchedEffect(Unit) {
    scale.animateTo(
      targetValue = 1f,
      animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
    )
    alpha.animateTo(
      targetValue = 1f,
      animationSpec = tween(durationMillis = 600)
    )
    delay(1200)
    onNavigateNext()
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
            MaterialTheme.colorScheme.surface
          )
        )
      ),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier
        .scale(scale.value)
        .padding(32.dp)
    ) {
      Box(
        modifier = Modifier
          .size(120.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
      ) {
        Image(
          painter = painterResource(id = R.drawable.ic_app_logo),
          contentDescription = stringResource(R.string.app_name),
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      Text(
        text = "Brother & Sister",
        style = MaterialTheme.typography.displayMedium.copy(
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        ),
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "“Memories that belong to us.”",
        style = MaterialTheme.typography.bodyLarge.copy(
          color = MaterialTheme.colorScheme.primary,
          fontWeight = FontWeight.Medium,
          letterSpacing = 0.2.sp
        ),
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "Private • Emotional • Forever ❤️",
        style = MaterialTheme.typography.labelMedium.copy(
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )
    }
  }
}
