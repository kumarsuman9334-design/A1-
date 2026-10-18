package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AvatarView
import kotlinx.coroutines.delay

@Composable
fun ActiveCallScreen(
  contactName: String,
  isVideo: Boolean,
  onEndCall: (durationSec: Int) -> Unit,
  modifier: Modifier = Modifier
) {
  var callDuration by remember { mutableIntStateOf(0) }
  var isConnected by remember { mutableStateOf(false) }
  var isMuted by remember { mutableStateOf(false) }
  var isSpeaker by remember { mutableStateOf(false) }
  var isVideoEnabled by remember { mutableStateOf(isVideo) }

  BackHandler { onEndCall(callDuration) }

  // Call connection and timer simulation
  LaunchedEffect(Unit) {
    delay(2000)
    isConnected = true
    while (true) {
      delay(1000)
      callDuration += 1
    }
  }

  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.15f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "scale"
  )

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFF101D25))
      .statusBarsPadding()
      .windowInsetsPadding(WindowInsets.navigationBars)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 24.dp, vertical = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Top encryption indicator
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 16.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Lock,
          contentDescription = null,
          tint = Color.White.copy(alpha = 0.6f),
          modifier = Modifier.size(13.dp)
        )
        Text(
          text = " End-to-end encrypted",
          fontSize = 12.sp,
          color = Color.White.copy(alpha = 0.6f)
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Contact Name
      Text(
        text = contactName,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Call status / duration
      val statusText = if (!isConnected) {
        if (isVideo) "WhatsApp video calling..." else "WhatsApp audio calling..."
      } else {
        val mins = callDuration / 60
        val secs = callDuration % 60
        String.format("%02d:%02d", mins, secs)
      }

      Text(
        text = statusText,
        fontSize = 15.sp,
        color = if (isConnected) Color(0xFF25D366) else Color.White.copy(alpha = 0.7f)
      )

      Spacer(modifier = Modifier.weight(1f))

      // Center Avatar with pulsing effect
      Box(contentAlignment = Alignment.Center) {
        if (!isConnected) {
          Box(
            modifier = Modifier
              .size(140.dp)
              .scale(pulseScale)
              .clip(CircleShape)
              .background(Color(0xFF25D366).copy(alpha = 0.25f))
          )
        }
        AvatarView(
          name = contactName,
          size = 110.dp
        )
      }

      Spacer(modifier = Modifier.weight(1f))

      // Controls Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(CircleShape)
          .background(Color.White.copy(alpha = 0.1f))
          .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Speaker toggle
        IconButton(onClick = { isSpeaker = !isSpeaker }) {
          Icon(
            imageVector = if (isSpeaker) Icons.Default.VolumeUp else Icons.Default.VolumeDown,
            contentDescription = "Speaker",
            tint = if (isSpeaker) Color(0xFF25D366) else Color.White,
            modifier = Modifier.size(28.dp)
          )
        }

        // Video toggle
        IconButton(onClick = { isVideoEnabled = !isVideoEnabled }) {
          Icon(
            imageVector = if (isVideoEnabled) Icons.Default.Videocam else Icons.Default.VideocamOff,
            contentDescription = "Video",
            tint = if (isVideoEnabled) Color(0xFF25D366) else Color.White,
            modifier = Modifier.size(28.dp)
          )
        }

        // Mute toggle
        IconButton(onClick = { isMuted = !isMuted }) {
          Icon(
            imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
            contentDescription = "Mute",
            tint = if (isMuted) Color(0xFFE53935) else Color.White,
            modifier = Modifier.size(28.dp)
          )
        }

        // End Call Button
        Box(
          modifier = Modifier
            .size(54.dp)
            .clip(CircleShape)
            .background(Color(0xFFE53935))
            .clickable { onEndCall(callDuration) }
            .testTag("end_call_button"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.CallEnd,
            contentDescription = "End Call",
            tint = Color.White,
            modifier = Modifier.size(28.dp)
          )
        }
      }
    }
  }
}
