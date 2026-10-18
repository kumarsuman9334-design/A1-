package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WhatsAppAccentGreen
import kotlinx.coroutines.launch

@Composable
fun AudioWaveformView(
  durationSec: Int,
  isOutgoing: Boolean,
  modifier: Modifier = Modifier
) {
  var isPlaying by remember { mutableStateOf(false) }
  var playbackProgress by remember { mutableFloatStateOf(0f) }
  val scope = rememberCoroutineScope()

  // Generate fixed pseudorandom waveform bar heights
  val barHeights = remember {
    listOf(0.3f, 0.6f, 0.9f, 0.5f, 0.8f, 0.4f, 0.7f, 1.0f, 0.85f, 0.45f, 0.65f, 0.35f, 0.75f, 0.55f, 0.9f, 0.4f, 0.6f, 0.8f)
  }

  val activeTint = if (isOutgoing) WhatsAppAccentGreen else Color(0xFF00A884)
  val inactiveTint = Color.Gray.copy(alpha = 0.5f)

  LaunchedEffect(isPlaying) {
    if (isPlaying) {
      val progressAnim = Animatable(playbackProgress)
      val remaining = (1f - playbackProgress) * (durationSec.coerceAtLeast(2) * 1000)
      progressAnim.animateTo(
        targetValue = 1f,
        animationSpec = tween(durationMillis = remaining.toInt(), easing = LinearEasing)
      ) {
        playbackProgress = value
      }
      isPlaying = false
      playbackProgress = 0f
    }
  }

  Row(
    modifier = modifier.padding(vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Play / Pause Circle
    Box(
      modifier = Modifier
        .size(38.dp)
        .clip(CircleShape)
        .background(activeTint)
        .clickable {
          if (isPlaying) {
            isPlaying = false
          } else {
            if (playbackProgress >= 1f) playbackProgress = 0f
            isPlaying = true
          }
        },
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
        contentDescription = if (isPlaying) "Pause" else "Play",
        tint = Color.White,
        modifier = Modifier.size(22.dp)
      )
    }

    Spacer(modifier = Modifier.width(10.dp))

    // Waveform Canvas
    Canvas(
      modifier = Modifier
        .weight(1f)
        .height(30.dp)
    ) {
      val canvasWidth = size.width
      val canvasHeight = size.height
      val barCount = barHeights.size
      val barWidth = 3.5.dp.toPx()
      val spacing = (canvasWidth - (barCount * barWidth)) / (barCount - 1).coerceAtLeast(1)

      barHeights.forEachIndexed { index, heightRatio ->
        val x = index * (barWidth + spacing)
        val barH = canvasHeight * heightRatio
        val y = (canvasHeight - barH) / 2f
        val isPassed = (index.toFloat() / barCount) <= playbackProgress

        drawRoundRect(
          color = if (isPassed) activeTint else inactiveTint,
          topLeft = Offset(x, y),
          size = Size(barWidth, barH),
          cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
        )
      }
    }

    Spacer(modifier = Modifier.width(8.dp))

    // Mic badge / indicator
    Icon(
      imageVector = Icons.Default.Mic,
      contentDescription = "Voice Note",
      tint = activeTint,
      modifier = Modifier.size(18.dp)
    )

    Spacer(modifier = Modifier.width(4.dp))

    // Duration display
    val currentSec = if (isPlaying) (playbackProgress * durationSec).toInt() else durationSec
    val minutes = currentSec / 60
    val seconds = currentSec % 60
    Text(
      text = String.format("%02d:%02d", minutes, seconds),
      fontSize = 11.sp,
      color = Color.Gray
    )
  }
}
