package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ChatEntity
import com.example.data.model.MessageEntity
import com.example.ui.components.AvatarView
import com.example.ui.components.MessageActionSheet
import com.example.ui.components.MessageBubble
import com.example.ui.theme.WhatsAppAccentGreen
import com.example.ui.theme.WhatsAppDarkSurface
import com.example.ui.theme.WhatsAppGreen

@Composable
fun ChatConversationScreen(
  chat: ChatEntity?,
  messages: List<MessageEntity>,
  messageInput: String,
  onMessageInputChange: (String) -> Unit,
  onSendMessage: () -> Unit,
  replyingToMessage: MessageEntity?,
  onCancelReply: () -> Unit,
  isRecordingVoice: Boolean,
  recordingDurationSec: Int,
  onStartVoiceRecording: () -> Unit,
  onStopVoiceRecording: (send: Boolean) -> Unit,
  onSendPhoto: () -> Unit,
  onSendLocation: () -> Unit,
  onReactionSelect: (messageId: String, reaction: String?) -> Unit,
  onDeleteMessage: (messageId: String) -> Unit,
  onSetReplyingTo: (MessageEntity) -> Unit,
  onStartCall: (contactName: String, isVideo: Boolean) -> Unit,
  onViewContact: (contactId: String) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }

  val isDark = isSystemInDarkTheme()
  val listState = rememberLazyListState()
  var showAttachmentSheet by remember { mutableStateOf(false) }
  var showQuickEmojis by remember { mutableStateOf(false) }
  var showMoreMenu by remember { mutableStateOf(false) }
  var selectedMessageForActions by remember { mutableStateOf<MessageEntity?>(null) }

  // Auto scroll to bottom when new messages arrive
  LaunchedEffect(messages.size) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size - 1)
    }
  }

  val headerBg = if (isDark) WhatsAppDarkSurface else WhatsAppGreen
  val contactName = chat?.contactName ?: "Chat"

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(if (isDark) Color(0xFF0B141A) else Color(0xFFEFEAE2))
  ) {
    // WhatsApp Conversation Top Bar
    Surface(
      color = headerBg,
      modifier = Modifier.fillMaxWidth(),
      shadowElevation = 3.dp
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding()
          .padding(horizontal = 4.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("chat_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color.White
          )
        }

        Row(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .clickable { chat?.let { onViewContact(it.contactId) } }
            .padding(vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          AvatarView(
            name = contactName,
            size = 40.dp,
            isGroup = chat?.isGroup ?: false,
            isOnline = chat?.isOnline ?: false
          )

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Text(
              text = contactName,
              fontSize = 17.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color.White,
              maxLines = 1
            )
            val subtitle = when {
              chat?.typingStatus == true -> "typing..."
              chat?.isOnline == true -> "online"
              else -> "last seen today at 11:24 AM"
            }
            Text(
              text = subtitle,
              fontSize = 12.sp,
              color = if (chat?.typingStatus == true) WhatsAppAccentGreen else Color.White.copy(alpha = 0.8f)
            )
          }
        }

        IconButton(onClick = { onStartCall(contactName, true) }) {
          Icon(
            imageVector = Icons.Default.Videocam,
            contentDescription = "Video call",
            tint = Color.White
          )
        }

        IconButton(onClick = { onStartCall(contactName, false) }) {
          Icon(
            imageVector = Icons.Default.Call,
            contentDescription = "Audio call",
            tint = Color.White
          )
        }

        Box {
          IconButton(onClick = { showMoreMenu = true }) {
            Icon(
              imageVector = Icons.Default.MoreVert,
              contentDescription = "More",
              tint = Color.White
            )
          }

          DropdownMenu(
            expanded = showMoreMenu,
            onDismissRequest = { showMoreMenu = false }
          ) {
            DropdownMenuItem(
              text = { Text("View contact") },
              onClick = {
                showMoreMenu = false
                chat?.let { onViewContact(it.contactId) }
              }
            )
            DropdownMenuItem(
              text = { Text("Media, links, and docs") },
              onClick = { showMoreMenu = false }
            )
            DropdownMenuItem(
              text = { Text("Search") },
              onClick = { showMoreMenu = false }
            )
            DropdownMenuItem(
              text = { Text("Mute notifications") },
              onClick = { showMoreMenu = false }
            )
            DropdownMenuItem(
              text = { Text("Disappearing messages") },
              onClick = { showMoreMenu = false }
            )
            DropdownMenuItem(
              text = { Text("Wallpaper") },
              onClick = { showMoreMenu = false }
            )
          }
        }
      }
    }

    // Main Chat Message Area with Wallpaper
    Box(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
    ) {
      // Background Wallpaper
      Image(
        painter = painterResource(id = R.drawable.bg_chat_wallpaper),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        alpha = if (isDark) 0.08f else 0.45f
      )

      // Messages List
      LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
      ) {
        // WhatsApp End-to-End Encryption Notice
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 24.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
          ) {
            Card(
              shape = RoundedCornerShape(8.dp),
              colors = CardDefaults.cardColors(
                containerColor = if (isDark) Color(0xFF182229) else Color(0xFFFFEECD)
              ),
              elevation = CardDefaults.cardElevation(1.dp)
            ) {
              Text(
                text = "🔒 Messages and calls are end-to-end encrypted. No one outside of this chat, not even WhatsApp, can read or listen to them.",
                fontSize = 11.5.sp,
                color = if (isDark) Color(0xFFFFD279) else Color(0xFF5A4400),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                lineHeight = 16.sp
              )
            }
          }
        }

        // Date pill: Today
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Card(
              shape = RoundedCornerShape(8.dp),
              colors = CardDefaults.cardColors(
                containerColor = if (isDark) Color(0xFF182229) else Color(0xFFE1F5FE)
              ),
              elevation = CardDefaults.cardElevation(1.dp)
            ) {
              Text(
                text = "TODAY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
              )
            }
          }
        }

        items(messages, key = { it.id }) { message ->
          MessageBubble(
            message = message,
            onLongClick = { selectedMessageForActions = message }
          )
        }
      }
    }

    // Attachment Popup Sheet if opened
    AnimatedVisibility(
      visible = showAttachmentSheet,
      enter = expandVertically() + fadeIn(),
      exit = shrinkVertically() + fadeOut()
    ) {
      AttachmentGrid(
        onDismiss = { showAttachmentSheet = false },
        onPhotoClick = {
          showAttachmentSheet = false
          onSendPhoto()
        },
        onLocationClick = {
          showAttachmentSheet = false
          onSendLocation()
        }
      )
    }

    // Quick Emoji Picker row if opened
    AnimatedVisibility(
      visible = showQuickEmojis,
      enter = expandVertically() + fadeIn(),
      exit = shrinkVertically() + fadeOut()
    ) {
      QuickEmojiBar(
        onSelectEmoji = { emoji ->
          onMessageInputChange(messageInput + emoji)
        }
      )
    }

    // Replying To Banner
    replyingToMessage?.let { replyMsg ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(if (isDark) WhatsAppDarkSurface else Color(0xFFF0F2F5))
          .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .width(4.dp)
            .height(36.dp)
            .background(WhatsAppAccentGreen)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = if (replyMsg.isOutgoing) "You" else contactName,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = WhatsAppAccentGreen
          )
          Text(
            text = replyMsg.text,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
          )
        }
        IconButton(onClick = onCancelReply) {
          Icon(Icons.Default.Close, contentDescription = "Cancel reply", modifier = Modifier.size(18.dp))
        }
      }
    }

    // Bottom Message Input & Mic Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .imePadding()
        .windowInsetsPadding(WindowInsets.navigationBars)
        .padding(horizontal = 6.dp, vertical = 6.dp),
      verticalAlignment = Alignment.Bottom
    ) {
      if (isRecordingVoice) {
        // Voice Recording in progress UI
        Surface(
          shape = RoundedCornerShape(26.dp),
          color = if (isDark) WhatsAppDarkSurface else Color.White,
          shadowElevation = 2.dp,
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxSize()
              .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Mic,
              contentDescription = "Recording",
              tint = Color(0xFFE53935),
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            val mins = recordingDurationSec / 60
            val secs = recordingDurationSec % 60
            Text(
              text = String.format("%02d:%02d", mins, secs),
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFFE53935)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
              text = "Slide to cancel",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { onStopVoiceRecording(false) }) {
              Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Cancel recording",
                tint = Color.Gray
              )
            }
          }
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Confirm Send Voice Note FAB
        Box(
          modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(WhatsAppAccentGreen)
            .clickable { onStopVoiceRecording(true) },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Send voice note",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
          )
        }
      } else {
        // Standard Text & Media Input Bar
        Surface(
          shape = RoundedCornerShape(26.dp),
          color = if (isDark) WhatsAppDarkSurface else Color.White,
          shadowElevation = 2.dp,
          modifier = Modifier.weight(1f)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            IconButton(
              onClick = { showQuickEmojis = !showQuickEmojis },
              modifier = Modifier.size(36.dp)
            ) {
              Icon(
                imageVector = Icons.Default.SentimentSatisfiedAlt,
                contentDescription = "Emoji",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            BasicTextField(
              value = messageInput,
              onValueChange = onMessageInputChange,
              textStyle = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
              ),
              cursorBrush = SolidColor(WhatsAppAccentGreen),
              maxLines = 4,
              decorationBox = { innerTextField ->
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 6.dp, vertical = 8.dp)
                ) {
                  if (messageInput.isEmpty()) {
                    Text(
                      text = "Message",
                      fontSize = 16.sp,
                      color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                  }
                  innerTextField()
                }
              },
              modifier = Modifier
                .weight(1f)
                .testTag("chat_input_text_field")
            )

            IconButton(
              onClick = { showAttachmentSheet = !showAttachmentSheet },
              modifier = Modifier.size(36.dp)
            ) {
              Icon(
                imageVector = Icons.Default.AttachFile,
                contentDescription = "Attach",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            if (messageInput.isEmpty()) {
              IconButton(
                onClick = onSendPhoto,
                modifier = Modifier.size(36.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.CameraAlt,
                  contentDescription = "Camera",
                  tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Dynamic Mic or Send FAB
        val isInputEmpty = messageInput.isBlank()
        Box(
          modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(WhatsAppAccentGreen)
            .clickable {
              if (isInputEmpty) {
                onStartVoiceRecording()
              } else {
                onSendMessage()
              }
            }
            .testTag(if (isInputEmpty) "chat_mic_button" else "chat_send_button"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (isInputEmpty) Icons.Default.Mic else Icons.AutoMirrored.Filled.Send,
            contentDescription = if (isInputEmpty) "Record voice" else "Send message",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
          )
        }
      }
    }
  }

  // Message Action Sheet (Reactions, Reply, Copy, Delete)
  selectedMessageForActions?.let { message ->
    MessageActionSheet(
      message = message,
      onDismiss = { selectedMessageForActions = null },
      onSelectReaction = { reaction ->
        onReactionSelect(message.id, reaction)
      },
      onReply = {
        onSetReplyingTo(message)
      },
      onCopy = {
        // Text copied
      },
      onDelete = {
        onDeleteMessage(message.id)
      }
    )
  }
}

@Composable
private fun QuickEmojiBar(onSelectEmoji: (String) -> Unit) {
  val emojis = listOf("😊", "😂", "❤️", "👍", "🔥", "🙏", "🎉", "😍", "👏", "✨", "🙌", "🤩")
  Surface(
    color = MaterialTheme.colorScheme.surfaceVariant,
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceAround
    ) {
      emojis.forEach { emoji ->
        Text(
          text = emoji,
          fontSize = 24.sp,
          modifier = Modifier
            .clip(CircleShape)
            .clickable { onSelectEmoji(emoji) }
            .padding(4.dp)
        )
      }
    }
  }
}

@Composable
private fun AttachmentGrid(
  onDismiss: () -> Unit,
  onPhotoClick: () -> Unit,
  onLocationClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(8.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        AttachmentOptionItem(
          label = "Document",
          icon = Icons.Default.Description,
          bgColor = Color(0xFF7F66FF),
          onClick = onPhotoClick
        )
        AttachmentOptionItem(
          label = "Camera",
          icon = Icons.Default.CameraAlt,
          bgColor = Color(0xFFD3396D),
          onClick = onPhotoClick
        )
        AttachmentOptionItem(
          label = "Gallery",
          icon = Icons.Default.Image,
          bgColor = Color(0xFFAC44CF),
          onClick = onPhotoClick
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        AttachmentOptionItem(
          label = "Audio",
          icon = Icons.Default.Headphones,
          bgColor = Color(0xFFE56E25),
          onClick = onPhotoClick
        )
        AttachmentOptionItem(
          label = "Location",
          icon = Icons.Default.LocationOn,
          bgColor = Color(0xFF1EA952),
          onClick = onLocationClick
        )
        AttachmentOptionItem(
          label = "Contact",
          icon = Icons.Default.Person,
          bgColor = Color(0xFF009688),
          onClick = onDismiss
        )
      }
    }
  }
}

@Composable
private fun AttachmentOptionItem(
  label: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  bgColor: Color,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clip(RoundedCornerShape(8.dp))
      .clickable(onClick = onClick)
      .padding(8.dp)
  ) {
    Box(
      modifier = Modifier
        .size(54.dp)
        .clip(CircleShape)
        .background(bgColor),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = Color.White,
        modifier = Modifier.size(26.dp)
      )
    }
    Spacer(modifier = Modifier.height(6.dp))
    Text(
      text = label,
      fontSize = 12.sp,
      color = MaterialTheme.colorScheme.onSurface
    )
  }
}
