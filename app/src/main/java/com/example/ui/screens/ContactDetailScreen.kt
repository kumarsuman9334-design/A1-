package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ContactEntity
import com.example.ui.components.AvatarView
import com.example.ui.theme.WhatsAppAccentGreen
import com.example.ui.theme.WhatsAppDarkSurface

@Composable
fun ContactDetailScreen(
  contact: ContactEntity?,
  onStartCall: (contactName: String, isVideo: Boolean) -> Unit,
  onStartChat: (ContactEntity) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }

  val isDark = isSystemInDarkTheme()
  var isMuted by remember { mutableStateOf(false) }

  val name = contact?.name ?: "Contact Info"
  val phone = contact?.phone ?: "+1 555-0100"
  val bio = contact?.statusBio ?: "Hey there! I am using WhatsApp."

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(if (isDark) Color(0xFF0B141A) else Color(0xFFF0F2F5))
      .statusBarsPadding()
      .windowInsetsPadding(WindowInsets.navigationBars)
  ) {
    // Header bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = onBack) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
          tint = MaterialTheme.colorScheme.onSurface
        )
      }
      Text(
        text = "Contact info",
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(start = 8.dp)
      )
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
      // Profile Hero Card
      item {
        Card(
          shape = RoundedCornerShape(0.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (isDark) WhatsAppDarkSurface else Color.White
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            AvatarView(name = name, size = 100.dp, isOnline = contact?.isOnline ?: false)
            Spacer(modifier = Modifier.height(14.dp))
            Text(
              text = name,
              fontSize = 22.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = phone,
              fontSize = 15.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons Row (Chat, Audio, Video, Search)
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceEvenly
            ) {
              ContactActionPill(
                icon = Icons.Default.Chat,
                label = "Chat",
                onClick = { contact?.let { onStartChat(it) } }
              )
              ContactActionPill(
                icon = Icons.Default.Call,
                label = "Audio",
                onClick = { onStartCall(name, false) }
              )
              ContactActionPill(
                icon = Icons.Default.Videocam,
                label = "Video",
                onClick = { onStartCall(name, true) }
              )
              ContactActionPill(
                icon = Icons.Default.Search,
                label = "Search",
                onClick = { }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))
      }

      // About & Phone Section
      item {
        Card(
          shape = RoundedCornerShape(0.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (isDark) WhatsAppDarkSurface else Color.White
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = bio,
              fontSize = 16.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "About",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider(
              modifier = Modifier.padding(vertical = 12.dp),
              color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )

            Text(
              text = phone,
              fontSize = 16.sp,
              fontWeight = FontWeight.Medium,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Mobile",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))
      }

      // Media, links and docs section
      item {
        Card(
          shape = RoundedCornerShape(0.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (isDark) WhatsAppDarkSurface else Color.White
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { }
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Media, links, and docs",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "12 items",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Icon(
              imageVector = Icons.Default.ChevronRight,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))
      }

      // Settings (Mute, Encryption)
      item {
        Card(
          shape = RoundedCornerShape(0.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (isDark) WhatsAppDarkSurface else Color.White
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "Mute notifications",
                  fontSize = 16.sp,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
              Switch(
                checked = isMuted,
                onCheckedChange = { isMuted = it },
                colors = SwitchDefaults.colors(checkedThumbColor = WhatsAppAccentGreen)
              )
            }

            HorizontalDivider(
              modifier = Modifier.padding(horizontal = 16.dp),
              color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )

            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = WhatsAppAccentGreen,
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(16.dp))
              Column {
                Text(
                  text = "Encryption",
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Medium,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "Messages and calls are end-to-end encrypted. Tap to verify.",
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))
      }

      // Block and Report
      item {
        Card(
          shape = RoundedCornerShape(0.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (isDark) WhatsAppDarkSurface else Color.White
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { }
                .padding(16.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Block,
                contentDescription = null,
                tint = Color(0xFFE53935)
              )
              Spacer(modifier = Modifier.width(16.dp))
              Text(
                text = "Block $name",
                fontSize = 16.sp,
                color = Color(0xFFE53935)
              )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { }
                .padding(16.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.ThumbDown,
                contentDescription = null,
                tint = Color(0xFFE53935)
              )
              Spacer(modifier = Modifier.width(16.dp))
              Text(
                text = "Report $name",
                fontSize = 16.sp,
                color = Color(0xFFE53935)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}

@Composable
private fun ContactActionPill(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  label: String,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clip(RoundedCornerShape(12.dp))
      .clickable(onClick = onClick)
      .padding(horizontal = 14.dp, vertical = 8.dp)
  ) {
    Box(
      modifier = Modifier
        .size(44.dp)
        .clip(CircleShape)
        .background(WhatsAppAccentGreen.copy(alpha = 0.15f)),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = WhatsAppAccentGreen,
        modifier = Modifier.size(22.dp)
      )
    }
    Spacer(modifier = Modifier.height(6.dp))
    Text(
      text = label,
      fontSize = 12.sp,
      fontWeight = FontWeight.Medium,
      color = WhatsAppAccentGreen
    )
  }
}
