package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.WhatsAppAccentGreen
import com.example.ui.theme.WhatsAppDarkSurface
import com.example.ui.theme.WhatsAppGreen

@Composable
fun WhatsAppTopBar(
  isSearchActive: Boolean,
  searchQuery: String,
  onSearchQueryChange: (String) -> Unit,
  onSearchActiveChange: (Boolean) -> Unit,
  onCameraClick: () -> Unit,
  onSettingsClick: () -> Unit,
  onToggleTheme: () -> Unit,
  onNewGroupClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isDark = isSystemInDarkTheme()
  var showMenu by remember { mutableStateOf(false) }

  val topBarColor = if (isDark) WhatsAppDarkSurface else MaterialTheme.colorScheme.surface
  val titleColor = if (isDark) WhatsAppAccentGreen else WhatsAppGreen
  val iconColor = if (isDark) Color(0xFF8696A0) else Color(0xFF54656F)

  Surface(
    color = topBarColor,
    modifier = modifier.fillMaxWidth(),
    shadowElevation = 2.dp
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
      if (isSearchActive) {
        // Active Search Bar
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              color = MaterialTheme.colorScheme.surfaceVariant,
              shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = { onSearchActiveChange(false) },
            modifier = Modifier.testTag("search_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = MaterialTheme.colorScheme.onSurface
            )
          }

          Spacer(modifier = Modifier.width(4.dp))

          BasicTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            singleLine = true,
            textStyle = TextStyle(
              fontSize = 16.sp,
              color = MaterialTheme.colorScheme.onSurface
            ),
            cursorBrush = SolidColor(WhatsAppAccentGreen),
            decorationBox = { innerTextField ->
              if (searchQuery.isEmpty()) {
                Text(
                  text = "Search chats and messages...",
                  color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                  fontSize = 15.sp
                )
              }
              innerTextField()
            },
            modifier = Modifier
              .weight(1f)
              .testTag("search_input_field")
          )

          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { onSearchQueryChange("") }) {
              Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      } else {
        // Standard WhatsApp Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "WhatsApp",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = titleColor,
            modifier = Modifier
              .weight(1f)
              .padding(start = 12.dp)
          )

          IconButton(
            onClick = onCameraClick,
            modifier = Modifier.testTag("topbar_camera_button")
          ) {
            Icon(
              imageVector = Icons.Default.CameraAlt,
              contentDescription = "Camera",
              tint = iconColor
            )
          }

          IconButton(
            onClick = { onSearchActiveChange(true) },
            modifier = Modifier.testTag("topbar_search_button")
          ) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search",
              tint = iconColor
            )
          }

          Box {
            IconButton(
              onClick = { showMenu = true },
              modifier = Modifier.testTag("topbar_overflow_menu")
            ) {
              Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More options",
                tint = iconColor
              )
            }

            DropdownMenu(
              expanded = showMenu,
              onDismissRequest = { showMenu = false }
            ) {
              DropdownMenuItem(
                text = { Text("New group") },
                onClick = {
                  showMenu = false
                  onNewGroupClick()
                }
              )
              DropdownMenuItem(
                text = { Text("New broadcast") },
                onClick = { showMenu = false }
              )
              DropdownMenuItem(
                text = { Text("Linked devices") },
                onClick = { showMenu = false }
              )
              DropdownMenuItem(
                text = { Text("Starred messages") },
                onClick = { showMenu = false }
              )
              DropdownMenuItem(
                text = {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = if (isDark) Icons.Default.Brightness7 else Icons.Default.Brightness4,
                      contentDescription = "Toggle theme"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isDark) "Light mode" else "Dark mode")
                  }
                },
                onClick = {
                  showMenu = false
                  onToggleTheme()
                }
              )
              DropdownMenuItem(
                text = {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Settings")
                  }
                },
                onClick = {
                  showMenu = false
                  onSettingsClick()
                }
              )
            }
          }
        }
      }
    }
  }
}
