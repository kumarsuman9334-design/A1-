package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MessageEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageActionSheet(
  message: MessageEntity,
  onDismiss: () -> Unit,
  onSelectReaction: (String) -> Unit,
  onReply: () -> Unit,
  onCopy: () -> Unit,
  onDelete: () -> Unit
) {
  val sheetState = rememberModalBottomSheetState()
  val reactionEmojis = listOf("👍", "❤️", "😂", "😮", "😢", "🙏")

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surface
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
      // Reactions Pill
      Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          reactionEmojis.forEach { emoji ->
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .clickable {
                  onSelectReaction(emoji)
                  onDismiss()
                },
              contentAlignment = Alignment.Center
            ) {
              Text(text = emoji, fontSize = 24.sp)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Action items
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable {
            onReply()
            onDismiss()
          }
          .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Reply,
          contentDescription = "Reply",
          tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
          text = "Reply",
          fontSize = 16.sp,
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable {
            onCopy()
            onDismiss()
          }
          .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.ContentCopy,
          contentDescription = "Copy",
          tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
          text = "Copy text",
          fontSize = 16.sp,
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable {
            onDelete()
            onDismiss()
          }
          .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Delete,
          contentDescription = "Delete",
          tint = Color(0xFFE53935)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
          text = "Delete message",
          fontSize = 16.sp,
          color = Color(0xFFE53935)
        )
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
