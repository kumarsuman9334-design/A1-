package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StatusEntity
import com.example.ui.components.AvatarView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StatusViewerScreen(
  status: StatusEntity?,
  onReplyStatus: (String) -> Unit,
  onClose: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onClose() }

  if (status == null) {
    onClose()
    return
  }

  var isPaused by remember { mutableStateOf(false) }
  var progress by remember { mutableFloatStateOf(0f) }
  var replyText by remember { mutableStateOf("") }

  val bgColor = remember(status.backgroundColorHex) {
    try {
      Color(android.graphics.Color.parseColor(status.backgroundColorHex))
    } catch (e: Exception) {
      Color(0xFF008069)
    }
  }

  LaunchedEffect(isPaused) {
    if (!isPaused) {
      val anim = Animatable(progress)
      val durationRemaining = ((1f - progress) * 5000).toInt()
      anim.animateTo(
        targetValue = 1f,
        animationSpec = tween(durationMillis = durationRemaining, easing = LinearEasing)
      ) {
        progress = value
      }
      if (progress >= 1f) {
        onClose()
      }
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(bgColor)
      .pointerInput(Unit) {
        detectTapGestures(
          onPress = {
            isPaused = true
            tryAwaitRelease()
            isPaused = false
          },
          onTap = { offset ->
            if (offset.x < size.width / 3f) {
              // Tap left: restart or close
              progress = 0f
            } else if (offset.x > 2 * size.width / 3f) {
              // Tap right: advance
              onClose()
            }
          }
        )
      }
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding()
    ) {
      // Top Progress Indicator Bar
      LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 8.dp)
          .height(3.dp)
          .clip(RoundedCornerShape(2.dp)),
        color = Color.White,
        trackColor = Color.White.copy(alpha = 0.3f)
      )

      // Status Header (Avatar, Contact Name, Time, Close)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        AvatarView(name = status.contactName, size = 42.dp)

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = status.contactName,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
          )
          val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
          Text(
            text = "Today at " + timeFormat.format(Date(status.timestamp)),
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 12.sp
          )
        }

        IconButton(onClick = onClose) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = Color.White
          )
        }
      }

      // Middle Story Content
      Box(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = status.text,
          color = Color.White,
          fontSize = 28.sp,
          fontWeight = FontWeight.Medium,
          textAlign = TextAlign.Center,
          lineHeight = 38.sp
        )
      }

      // Quick Reaction Emojis
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        val emojis = listOf("❤️", "😂", "😮", "😢", "🙏", "👏")
        emojis.forEach { emoji ->
          Text(
            text = emoji,
            fontSize = 26.sp,
            modifier = Modifier
              .clip(CircleShape)
              .clickable {
                onReplyStatus(emoji)
                onClose()
              }
              .padding(4.dp)
          )
        }
      }

      // Reply to Status Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .imePadding()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(24.dp),
          color = Color.Black.copy(alpha = 0.35f),
          modifier = Modifier.weight(1f)
        ) {
          BasicTextField(
            value = replyText,
            onValueChange = { replyText = it },
            textStyle = TextStyle(fontSize = 15.sp, color = Color.White),
            cursorBrush = SolidColor(Color.White),
            singleLine = true,
            decorationBox = { innerTextField ->
              Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                if (replyText.isEmpty()) {
                  Text(
                    text = "Reply to " + status.contactName,
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp
                  )
                }
                innerTextField()
              }
            },
            modifier = Modifier.fillMaxWidth()
          )
        }

        if (replyText.isNotBlank()) {
          Spacer(modifier = Modifier.width(8.dp))
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(Color(0xFF25D366))
              .clickable {
                onReplyStatus(replyText)
                onClose()
              },
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.Send,
              contentDescription = "Send",
              tint = Color.White,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }
  }
}
