package com.example.ui.screens.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.HeartRed
import kotlinx.coroutines.launch

data class OnboardingStep(
  val title: String,
  val subtitle: String,
  val icon: ImageVector,
  val highlightBadge: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
  onConnectBrotherSister: () -> Unit,
  onCreatePrivateSpace: () -> Unit,
  onNavigateToAuth: () -> Unit
) {
  val steps = listOf(
    OnboardingStep(
      title = "Your memories deserve a special place.",
      subtitle = "A sacred, private sanctuary away from public social noise. Keep the laughter, milestones, and childhood secrets safe together.",
      icon = Icons.Default.Lock,
      highlightBadge = "100% Private & Encrypted"
    ),
    OnboardingStep(
      title = "Stay connected with your brother or sister.",
      subtitle = "No matter where life takes you or how busy days get, celebrate your lifelong bond with real-time shared albums, notes, and messages.",
      icon = Icons.Default.People,
      highlightBadge = "Lifelong Connection"
    ),
    OnboardingStep(
      title = "Capture childhood memories. Celebrate today. Preserve them forever.",
      subtitle = "Build an unbreakable chronological timeline. Relive festival mornings, funny school fights, and birthday surprises whenever you want.",
      icon = Icons.Default.Celebration,
      highlightBadge = "Timeless Moments"
    )
  )

  val pagerState = rememberPagerState(pageCount = { steps.size + 1 })
  val coroutineScope = rememberCoroutineScope()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 24.dp, vertical = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Top row with skip
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = HeartRed,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Brother & Sister",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
          )
        }

        if (pagerState.currentPage < steps.size) {
          TextButton(
            onClick = {
              coroutineScope.launch { pagerState.scrollToPage(steps.size) }
            }
          ) {
            Text(
              text = "Skip",
              style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Pager content
      HorizontalPager(
        state = pagerState,
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
      ) { pageIndex ->
        if (pageIndex < steps.size) {
          val step = steps[pageIndex]
          Column(
            modifier = Modifier
              .fillMaxSize()
              .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            // Visual Card / Hero
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(
                  Brush.verticalGradient(
                    colors = listOf(
                      MaterialTheme.colorScheme.primaryContainer,
                      MaterialTheme.colorScheme.surfaceVariant
                    )
                  )
                ),
              contentAlignment = Alignment.Center
            ) {
              Image(
                painter = painterResource(id = R.drawable.hero_family),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                  .fillMaxSize()
                  .clip(RoundedCornerShape(28.dp))
              )

              // Badge overlay
              Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                  .align(Alignment.BottomCenter)
                  .padding(bottom = 16.dp)
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = step.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = step.highlightBadge,
                    style = MaterialTheme.typography.labelMedium.copy(
                      fontWeight = FontWeight.SemiBold,
                      color = MaterialTheme.colorScheme.primary
                    )
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
              text = step.title,
              style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 30.sp
              ),
              textAlign = TextAlign.Center,
              modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
              text = step.subtitle,
              style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 22.sp
              ),
              textAlign = TextAlign.Center,
              modifier = Modifier.padding(horizontal = 16.dp)
            )
          }
        } else {
          // Page 4: "What would you like to do?"
          Column(
            modifier = Modifier
              .fillMaxSize()
              .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Box(
              modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = HeartRed,
                modifier = Modifier.size(36.dp)
              )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
              text = "What would you like to do?",
              style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              ),
              textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "Choose your starting path into the private space.",
              style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
              ),
              textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Option 1: Connect with my Brother/Sister
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onConnectBrotherSister() },
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
              ),
              elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                  )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = "Connect with my Brother/Sister",
                    style = MaterialTheme.typography.titleMedium.copy(
                      fontWeight = FontWeight.SemiBold,
                      color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                  )
                  Text(
                    text = "Send an invite link or scan QR code",
                    style = MaterialTheme.typography.bodySmall.copy(
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  )
                }
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Option 2: Create a private memory space
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onCreatePrivateSpace() },
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
              ),
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant
              )
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.AddCircleOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(24.dp)
                  )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = "Create a private memory space",
                    style = MaterialTheme.typography.titleMedium.copy(
                      fontWeight = FontWeight.SemiBold,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                  )
                  Text(
                    text = "Start adding childhood photos & notes",
                    style = MaterialTheme.typography.bodySmall.copy(
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  )
                }
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Option 3: Discover Brother/Sister Connections (Coming Soon)
            Card(
              modifier = Modifier
                .fillMaxWidth(),
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
              ),
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
              )
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Explore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(24.dp)
                  )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = "Discover Connections",
                      style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                      )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                      color = MaterialTheme.colorScheme.tertiaryContainer,
                      shape = RoundedCornerShape(12.dp)
                    ) {
                      Text(
                        text = "Coming Soon",
                        style = MaterialTheme.typography.labelSmall.copy(
                          color = MaterialTheme.colorScheme.onTertiaryContainer,
                          fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                      )
                    }
                  }
                  Text(
                    text = "Find long-lost relatives safely with family verification",
                    style = MaterialTheme.typography.bodySmall.copy(
                      color = MaterialTheme.colorScheme.outline
                    )
                  )
                }
              }
            }
          }
        }
      }

      // Bottom bar with dots & Next button
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Dot indicators
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          repeat(steps.size + 1) { index ->
            val isSelected = pagerState.currentPage == index
            Box(
              modifier = Modifier
                .height(8.dp)
                .width(if (isSelected) 24.dp else 8.dp)
                .clip(CircleShape)
                .background(
                  if (isSelected) MaterialTheme.colorScheme.primary
                  else MaterialTheme.colorScheme.outlineVariant
                )
            )
          }
        }

        if (pagerState.currentPage < steps.size) {
          Button(
            onClick = {
              coroutineScope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
              }
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.height(48.dp)
          ) {
            Text("Next", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
          }
        } else {
          TextButton(
            onClick = onNavigateToAuth
          ) {
            Text(
              "Sign In / Register",
              style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
              )
            )
          }
        }
      }
    }
  }
}
