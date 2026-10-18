package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WhatsAppAccentGreen

@Composable
fun CreateStatusScreen(
  onPostStatus: (text: String, bgColorHex: String) -> Unit,
  onClose: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onClose() }

  val colorHexes = listOf(
    "#008069",
    "#1E88E5",
    "#D81B60",
    "#E65100",
    "#6A1B9A",
    "#43A047",
    "#263238"
  )
  var colorIndex by remember { mutableIntStateOf(0) }
  var statusText by remember { mutableStateOf("") }

  val currentHex = colorHexes[colorIndex]
  val currentColor = Color(android.graphics.Color.parseColor(currentHex))

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(currentColor)
      .statusBarsPadding()
      .windowInsetsPadding(WindowInsets.navigationBars)
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Top controls (Close, Font, Palette)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = onClose) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = Color.White
          )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = {
          colorIndex = (colorIndex + 1) % colorHexes.size
        }) {
          Icon(
            imageVector = Icons.Default.Palette,
            contentDescription = "Change color",
            tint = Color.White
          )
        }
      }

      // Middle status text area
      Box(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        BasicTextField(
          value = statusText,
          onValueChange = { statusText = it },
          textStyle = TextStyle(
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
          ),
          cursorBrush = SolidColor(Color.White),
          decorationBox = { innerTextField ->
            Box(contentAlignment = Alignment.Center) {
              if (statusText.isEmpty()) {
                Text(
                  text = "Type a status",
                  color = Color.White.copy(alpha = 0.6f),
                  fontSize = 32.sp,
                  fontWeight = FontWeight.Medium,
                  textAlign = TextAlign.Center
                )
              }
              innerTextField()
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("status_input_field")
        )
      }

      // Bottom send FAB row
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .imePadding()
          .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Spacer(modifier = Modifier.weight(1f))

        FloatingActionButton(
          onClick = {
            if (statusText.isNotBlank()) {
              onPostStatus(statusText, currentHex)
              onClose()
            }
          },
          containerColor = WhatsAppAccentGreen,
          contentColor = Color.White,
          shape = CircleShape,
          modifier = Modifier.testTag("post_status_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            contentDescription = "Share status",
            modifier = Modifier.size(24.dp)
          )
        }
      }
    }
  }
}
