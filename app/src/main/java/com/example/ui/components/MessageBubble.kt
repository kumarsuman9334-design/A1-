package com.example.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MessageEntity
import com.example.data.model.MessageStatus
import com.example.data.model.MessageType
import com.example.ui.theme.WhatsAppBlueTick
import com.example.ui.theme.WhatsAppGreyTick
import com.example.ui.theme.WhatsAppIncomingDark
import com.example.ui.theme.WhatsAppIncomingLight
import com.example.ui.theme.WhatsAppOutgoingDark
import com.example.ui.theme.WhatsAppOutgoingLight
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageBubble(
  message: MessageEntity,
  onLongClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isDark = isSystemInDarkTheme()
  val bubbleColor = if (message.isOutgoing) {
    if (isDark) WhatsAppOutgoingDark else WhatsAppOutgoingLight
  } else {
    if (isDark) WhatsAppIncomingDark else WhatsAppIncomingLight
  }

  val bubbleShape = if (message.isOutgoing) {
    RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomEnd = 16.dp, bottomStart = 16.dp)
  } else {
    RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp)
  }

  val timeFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())
  val timeString = timeFormatter.format(Date(message.timestamp))

  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp, vertical = 2.dp),
    contentAlignment = if (message.isOutgoing) Alignment.CenterEnd else Alignment.CenterStart
  ) {
    Column(
      horizontalAlignment = if (message.isOutgoing) Alignment.End else Alignment.Start
    ) {
      Surface(
        shape = bubbleShape,
        color = bubbleColor,
        shadowElevation = 1.dp,
        modifier = Modifier
          .widthIn(max = 300.dp)
          .combinedClickable(
            onClick = {},
            onLongClick = onLongClick
          )
      ) {
        Column(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          // Replied Message Quote Header if any
          if (!message.replyToText.isNullOrBlank()) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(
                  if (message.isOutgoing) Color(0x2000A884) else Color(0x15000000)
                )
                .padding(start = 6.dp)
            ) {
              Box(
                modifier = Modifier
                  .width(3.dp)
                  .matchParentSize()
                  .background(Color(0xFF00A884))
              )
              Text(
                text = message.replyToText,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                modifier = Modifier.padding(6.dp)
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
          }

          // Message content based on type
          when (message.type) {
            MessageType.TEXT -> {
              Text(
                text = message.text,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp
              )
            }
            MessageType.VOICE -> {
              AudioWaveformView(
                durationSec = message.voiceDurationSec,
                isOutgoing = message.isOutgoing
              )
            }
            MessageType.IMAGE -> {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(180.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xFF333333)),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "📷 " + message.text,
                  color = Color.White,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Medium
                )
              }
              if (message.text.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = message.text,
                  fontSize = 14.sp,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }
            MessageType.LOCATION -> {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.LocationOn,
                  contentDescription = "Location",
                  tint = Color(0xFFE53935),
                  modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = message.text,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Medium,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }

          // Timestamp & Delivery status ticks
          Row(
            modifier = Modifier
              .align(Alignment.End)
              .padding(top = 2.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = timeString,
              fontSize = 10.5.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )

            if (message.isOutgoing) {
              Spacer(modifier = Modifier.width(4.dp))
              when (message.status) {
                MessageStatus.SENDING -> {
                  Text("🕒", fontSize = 9.sp)
                }
                MessageStatus.SENT -> {
                  Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "Sent",
                    tint = WhatsAppGreyTick,
                    modifier = Modifier.size(14.dp)
                  )
                }
                MessageStatus.DELIVERED -> {
                  Icon(
                    imageVector = Icons.Default.DoneAll,
                    contentDescription = "Delivered",
                    tint = WhatsAppGreyTick,
                    modifier = Modifier.size(15.dp)
                  )
                }
                MessageStatus.READ -> {
                  Icon(
                    imageVector = Icons.Default.DoneAll,
                    contentDescription = "Read",
                    tint = WhatsAppBlueTick,
                    modifier = Modifier.size(15.dp)
                  )
                }
              }
            }
          }
        }
      }

      // Reaction Badge if present
      if (!message.reaction.isNullOrBlank()) {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
          ),
          elevation = CardDefaults.cardElevation(2.dp),
          modifier = Modifier
            .padding(start = if (!message.isOutgoing) 8.dp else 0.dp, end = if (message.isOutgoing) 8.dp else 0.dp)
        ) {
          Text(
            text = message.reaction,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }
    }
  }
}
