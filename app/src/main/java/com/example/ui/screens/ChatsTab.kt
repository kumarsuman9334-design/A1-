package com.example.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatEntity
import com.example.ui.ChatFilter
import com.example.ui.components.AvatarView
import com.example.ui.theme.WhatsAppAccentGreen
import com.example.ui.theme.WhatsAppBlueTick
import com.example.ui.theme.WhatsAppGreyTick
import com.example.ui.theme.WhatsAppUnreadBadge
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ChatsTab(
  chats: List<ChatEntity>,
  selectedFilter: ChatFilter,
  onSelectFilter: (ChatFilter) -> Unit,
  onChatClick: (String) -> Unit,
  onNewChatClick: () -> Unit,
  onTogglePin: (String) -> Unit,
  onToggleMute: (String) -> Unit,
  onToggleArchive: (String) -> Unit,
  onDeleteChat: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedChatForActions by remember { mutableStateOf<ChatEntity?>(null) }

  Box(modifier = modifier.fillMaxSize()) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Filter Chips Row (All, Unread, Favorites, Groups)
      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        val filters = listOf(
          ChatFilter.ALL to "All",
          ChatFilter.UNREAD to "Unread",
          ChatFilter.FAVORITES to "Favorites",
          ChatFilter.GROUPS to "Groups"
        )
        items(filters) { (filter, label) ->
          val isSelected = selectedFilter == filter
          FilterChip(
            selected = isSelected,
            onClick = { onSelectFilter(filter) },
            label = {
              Text(
                text = label,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 13.sp
              )
            },
            shape = RoundedCornerShape(18.dp),
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = WhatsAppAccentGreen.copy(alpha = 0.2f),
              selectedLabelColor = WhatsAppAccentGreen
            )
          )
        }
      }

      if (chats.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Default.Chat,
              contentDescription = "No chats",
              tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
              modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
              text = "No chats found",
              fontSize = 18.sp,
              fontWeight = FontWeight.Medium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Tap the green button below to start a new chat",
              fontSize = 13.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
          }
        }
      } else {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(bottom = 88.dp)
        ) {
          items(chats, key = { it.id }) { chat ->
            ChatItemRow(
              chat = chat,
              onClick = { onChatClick(chat.id) },
              onLongClick = { selectedChatForActions = chat }
            )
            HorizontalDivider(
              modifier = Modifier.padding(start = 76.dp, end = 16.dp),
              thickness = 0.5.dp,
              color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )
          }
        }
      }
    }

    // New Chat Floating Action Button
    FloatingActionButton(
      onClick = onNewChatClick,
      containerColor = WhatsAppAccentGreen,
      contentColor = Color.White,
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp)
        .testTag("new_chat_fab")
    ) {
      Icon(
        imageVector = Icons.Default.Message,
        contentDescription = "New chat",
        modifier = Modifier.size(24.dp)
      )
    }

    // Long press action dialog
    selectedChatForActions?.let { chat ->
      AlertDialog(
        onDismissRequest = { selectedChatForActions = null },
        title = { Text(text = chat.contactName) },
        text = {
          Column {
            TextButton(
              onClick = {
                onTogglePin(chat.id)
                selectedChatForActions = null
              },
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.PushPin, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text(if (chat.isPinned) "Unpin chat" else "Pin chat")
              }
            }
            TextButton(
              onClick = {
                onToggleMute(chat.id)
                selectedChatForActions = null
              },
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.NotificationsOff, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text(if (chat.isMuted) "Unmute notifications" else "Mute notifications")
              }
            }
            TextButton(
              onClick = {
                onToggleArchive(chat.id)
                selectedChatForActions = null
              },
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.Archive, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text(if (chat.isArchived) "Unarchive chat" else "Archive chat")
              }
            }
            TextButton(
              onClick = {
                onDeleteChat(chat.id)
                selectedChatForActions = null
              },
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Delete chat", color = Color.Red)
              }
            }
          }
        },
        confirmButton = {
          TextButton(onClick = { selectedChatForActions = null }) {
            Text("Cancel")
          }
        }
      )
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ChatItemRow(
  chat: ChatEntity,
  onClick: () -> Unit,
  onLongClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .combinedClickable(onClick = onClick, onLongClick = onLongClick)
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    AvatarView(
      name = chat.contactName,
      size = 52.dp,
      isGroup = chat.isGroup,
      isOnline = chat.isOnline
    )

    Spacer(modifier = Modifier.width(14.dp))

    Column(modifier = Modifier.weight(1f)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = chat.contactName,
          fontSize = 16.sp,
          fontWeight = if (chat.unreadCount > 0) FontWeight.Bold else FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.weight(1f)
        )

        Text(
          text = formatChatTime(chat.lastMessageTimestamp),
          fontSize = 12.sp,
          fontWeight = if (chat.unreadCount > 0) FontWeight.Bold else FontWeight.Normal,
          color = if (chat.unreadCount > 0) WhatsAppUnreadBadge else MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.height(4.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier.weight(1f),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Outgoing read tick icon on last message if unreadCount == 0 and not typing
          if (chat.unreadCount == 0 && !chat.typingStatus) {
            Icon(
              imageVector = Icons.Default.DoneAll,
              contentDescription = "Read",
              tint = WhatsAppBlueTick,
              modifier = Modifier
                .size(15.dp)
                .padding(end = 4.dp)
            )
          }

          Text(
            text = if (chat.typingStatus) "typing..." else chat.lastMessage,
            fontSize = 14.sp,
            color = if (chat.typingStatus) WhatsAppAccentGreen else MaterialTheme.colorScheme.onSurfaceVariant,
            fontStyle = if (chat.typingStatus) androidx.compose.ui.text.font.FontStyle.Italic else null,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          if (chat.isMuted) {
            Icon(
              imageVector = Icons.Default.NotificationsOff,
              contentDescription = "Muted",
              tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
              modifier = Modifier
                .size(16.dp)
                .padding(end = 4.dp)
            )
          }
          if (chat.isPinned) {
            Icon(
              imageVector = Icons.Default.PushPin,
              contentDescription = "Pinned",
              tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
              modifier = Modifier
                .size(16.dp)
                .padding(end = 4.dp)
            )
          }
          if (chat.unreadCount > 0) {
            Box(
              modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(WhatsAppUnreadBadge),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = if (chat.unreadCount > 99) "99+" else chat.unreadCount.toString(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }
        }
      }
    }
  }
}

private fun formatChatTime(timestamp: Long): String {
  val now = Calendar.getInstance()
  val messageTime = Calendar.getInstance().apply { timeInMillis = timestamp }

  return if (now.get(Calendar.YEAR) == messageTime.get(Calendar.YEAR) &&
    now.get(Calendar.DAY_OF_YEAR) == messageTime.get(Calendar.DAY_OF_YEAR)
  ) {
    SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(timestamp))
  } else if (now.get(Calendar.YEAR) == messageTime.get(Calendar.YEAR) &&
    now.get(Calendar.DAY_OF_YEAR) - messageTime.get(Calendar.DAY_OF_YEAR) == 1
  ) {
    "Yesterday"
  } else {
    SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(Date(timestamp))
  }
}
