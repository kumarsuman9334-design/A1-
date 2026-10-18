package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WhatsAppStatusRing
import com.example.ui.theme.WhatsAppStatusRingViewed
import com.example.ui.theme.WhatsAppTealGreen

@Composable
fun AvatarView(
  name: String,
  modifier: Modifier = Modifier,
  size: Dp = 50.dp,
  isGroup: Boolean = false,
  isOnline: Boolean = false,
  statusRingState: StatusRingType = StatusRingType.NONE
) {
  // Generate a consistent pleasing pastel color based on name hash
  val colors = listOf(
    Color(0xFF00A884),
    Color(0xFF34B7F1),
    Color(0xFFE91E63),
    Color(0xFF9C27B0),
    Color(0xFFFF9800),
    Color(0xFF009688),
    Color(0xFF3F51B5),
    Color(0xFF673AB7)
  )
  val bgColor = if (name.isNotBlank()) colors[kotlin.math.abs(name.hashCode()) % colors.size] else WhatsAppTealGreen

  Box(
    modifier = modifier.size(size),
    contentAlignment = Alignment.Center
  ) {
    // Outer Status Ring if applicable
    if (statusRingState != StatusRingType.NONE) {
      val ringColor = if (statusRingState == StatusRingType.UNVIEWED) WhatsAppStatusRing else WhatsAppStatusRingViewed
      Canvas(modifier = Modifier.size(size)) {
        drawCircle(
          brush = SolidColor(ringColor),
          radius = (size.toPx() / 2f) - 1.5f,
          style = Stroke(width = 2.5f.dp.toPx())
        )
      }
    }

    val innerSize = if (statusRingState != StatusRingType.NONE) size - 8.dp else size
    Box(
      modifier = Modifier
        .size(innerSize)
        .clip(CircleShape)
        .background(bgColor),
      contentAlignment = Alignment.Center
    ) {
      if (isGroup) {
        Icon(
          imageVector = Icons.Default.Group,
          contentDescription = "Group",
          tint = Color.White,
          modifier = Modifier.size(innerSize * 0.55f)
        )
      } else if (name.isBlank()) {
        Icon(
          imageVector = Icons.Default.Person,
          contentDescription = "User",
          tint = Color.White,
          modifier = Modifier.size(innerSize * 0.55f)
        )
      } else {
        val initials = name.split(" ")
          .take(2)
          .mapNotNull { it.firstOrNull()?.uppercase() }
          .joinToString("")
        Text(
          text = if (initials.isNotEmpty()) initials else name.take(1).uppercase(),
          color = Color.White,
          fontSize = (innerSize.value * 0.42f).sp,
          fontWeight = FontWeight.Bold
        )
      }
    }

    // Online indicator dot
    if (isOnline) {
      Box(
        modifier = Modifier
          .size(size * 0.28f)
          .align(Alignment.BottomEnd)
          .clip(CircleShape)
          .background(Color(0xFF25D366))
          .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
      )
    }
  }
}

enum class StatusRingType {
  NONE, UNVIEWED, VIEWED
}
