package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.UserProfile
import com.example.ui.components.AvatarView
import com.example.ui.theme.WhatsAppAccentGreen
import com.example.ui.theme.WhatsAppDarkSurface
import com.example.ui.theme.WhatsAppGreen

@Composable
fun SettingsScreen(
  userProfile: UserProfile,
  onUpdateProfile: (name: String, about: String) -> Unit,
  onToggleTheme: () -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }

  val isDark = isSystemInDarkTheme()
  var showEditProfileDialog by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .statusBarsPadding()
      .windowInsetsPadding(WindowInsets.navigationBars)
  ) {
    // Header
    Surface(
      color = if (isDark) WhatsAppDarkSurface else WhatsAppGreen,
      modifier = Modifier.fillMaxWidth(),
      shadowElevation = 2.dp
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 4.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("settings_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color.White
          )
        }
        Text(
          text = "Settings",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White,
          modifier = Modifier.padding(start = 8.dp)
        )
      }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
      // User Profile Card
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { showEditProfileDialog = true }
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          AvatarView(name = userProfile.name, size = 64.dp)

          Spacer(modifier = Modifier.width(16.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = userProfile.name,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = userProfile.about,
              fontSize = 14.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 1
            )
          }

          IconButton(onClick = {}) {
            Icon(
              imageVector = Icons.Default.QrCode,
              contentDescription = "QR Code",
              tint = WhatsAppAccentGreen
            )
          }
        }

        HorizontalDivider(
          modifier = Modifier.padding(horizontal = 16.dp),
          color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
      }

      // Settings Rows
      item {
        SettingsItem(
          icon = Icons.Default.Key,
          title = "Account",
          subtitle = "Security notifications, change number"
        )
        SettingsItem(
          icon = Icons.Default.Lock,
          title = "Privacy",
          subtitle = "Block contacts, disappearing messages"
        )
        SettingsItem(
          icon = Icons.Default.Brightness4,
          title = "Theme",
          subtitle = if (isDark) "Dark mode (Tap to switch)" else "Light mode (Tap to switch)",
          onClick = onToggleTheme
        )
        SettingsItem(
          icon = Icons.Default.Chat,
          title = "Chats",
          subtitle = "Theme, wallpapers, chat history"
        )
        SettingsItem(
          icon = Icons.Default.Notifications,
          title = "Notifications",
          subtitle = "Message, group & call tones"
        )
        SettingsItem(
          icon = Icons.Default.DataUsage,
          title = "Storage and data",
          subtitle = "Network usage, auto-download"
        )
        SettingsItem(
          icon = Icons.Default.Help,
          title = "Help",
          subtitle = "Help center, contact us, privacy policy"
        )
        SettingsItem(
          icon = Icons.Default.Share,
          title = "Invite a friend",
          subtitle = null
        )

        Spacer(modifier = Modifier.height(24.dp))
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "from",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
            Text(
              text = "Meta",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = WhatsAppAccentGreen
            )
          }
        }
      }
    }
  }

  // Edit Profile Dialog
  if (showEditProfileDialog) {
    var editName by remember { mutableStateOf(userProfile.name) }
    var editAbout by remember { mutableStateOf(userProfile.about) }

    AlertDialog(
      onDismissRequest = { showEditProfileDialog = false },
      title = { Text("Profile") },
      text = {
        Column {
          OutlinedTextField(
            value = editName,
            onValueChange = { editName = it },
            label = { Text("Your name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(12.dp))
          OutlinedTextField(
            value = editAbout,
            onValueChange = { editAbout = it },
            label = { Text("About") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            onUpdateProfile(editName, editAbout)
            showEditProfileDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = WhatsAppAccentGreen)
        ) {
          Text("Save", color = Color.White)
        }
      },
      dismissButton = {
        TextButton(onClick = { showEditProfileDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

@Composable
private fun SettingsItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  subtitle: String?,
  onClick: () -> Unit = {}
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .padding(horizontal = 16.dp, vertical = 14.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = title,
      tint = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.size(24.dp)
    )

    Spacer(modifier = Modifier.width(20.dp))

    Column {
      Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurface
      )
      if (subtitle != null) {
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = subtitle,
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}
