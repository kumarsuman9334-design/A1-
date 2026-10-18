package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = WhatsAppAccentGreen,
  onPrimary = Color.Black,
  primaryContainer = WhatsAppOutgoingDark,
  onPrimaryContainer = WhatsAppTextPrimaryDark,
  secondary = WhatsAppTealGreen,
  onSecondary = Color.White,
  background = WhatsAppDarkBackground,
  onBackground = WhatsAppTextPrimaryDark,
  surface = WhatsAppDarkSurface,
  onSurface = WhatsAppTextPrimaryDark,
  surfaceVariant = WhatsAppDarkSurfaceVariant,
  onSurfaceVariant = WhatsAppTextSecondaryDark,
  outline = Color(0xFF2E3840)
)

private val LightColorScheme = lightColorScheme(
  primary = WhatsAppGreen,
  onPrimary = Color.White,
  primaryContainer = WhatsAppOutgoingLight,
  onPrimaryContainer = WhatsAppTextPrimaryLight,
  secondary = WhatsAppTealGreen,
  onSecondary = Color.White,
  background = WhatsAppLightBackground,
  onBackground = WhatsAppTextPrimaryLight,
  surface = WhatsAppLightSurface,
  onSurface = WhatsAppTextPrimaryLight,
  surfaceVariant = WhatsAppLightSurfaceVariant,
  onSurfaceVariant = WhatsAppTextSecondaryLight,
  outline = Color(0xFFE0E0E0)
)

@Composable
fun WhatsAppTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
