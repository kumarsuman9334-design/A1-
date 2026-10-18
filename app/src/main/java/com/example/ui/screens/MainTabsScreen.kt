package com.example.ui.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CallEntity
import com.example.data.model.ChatEntity
import com.example.data.model.StatusEntity
import com.example.ui.ChatFilter
import com.example.ui.MainTab
import com.example.ui.components.WhatsAppTopBar
import com.example.ui.theme.WhatsAppAccentGreen
import com.example.ui.theme.WhatsAppDarkSurface
import com.example.ui.theme.WhatsAppUnreadBadge

@Composable
fun MainTabsScreen(
  selectedTab: MainTab,
  onSelectTab: (MainTab) -> Unit,
  chats: List<ChatEntity>,
  selectedFilter: ChatFilter,
  onSelectFilter: (ChatFilter) -> Unit,
  statusUpdates: List<StatusEntity>,
  calls: List<CallEntity>,
  isSearchActive: Boolean,
  searchQuery: String,
  onSearchQueryChange: (String) -> Unit,
  onSearchActiveChange: (Boolean) -> Unit,
  onChatClick: (String) -> Unit,
  onNewChatClick: () -> Unit,
  onTogglePin: (String) -> Unit,
  onToggleMute: (String) -> Unit,
  onToggleArchive: (String) -> Unit,
  onDeleteChat: (String) -> Unit,
  onStatusClick: (String) -> Unit,
  onAddStatusClick: () -> Unit,
  onStartCall: (String, Boolean) -> Unit,
  onNewCallClick: () -> Unit,
  onCameraClick: () -> Unit,
  onSettingsClick: () -> Unit,
  onToggleTheme: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isDark = isSystemInDarkTheme()
  val totalUnread = chats.sumOf { it.unreadCount }
  val hasUnviewedStatus = statusUpdates.any { !it.isMyStatus && !it.isViewed }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      WhatsAppTopBar(
        isSearchActive = isSearchActive,
        searchQuery = searchQuery,
        onSearchQueryChange = onSearchQueryChange,
        onSearchActiveChange = onSearchActiveChange,
        onCameraClick = onCameraClick,
        onSettingsClick = onSettingsClick,
        onToggleTheme = onToggleTheme,
        onNewGroupClick = onNewChatClick
      )
    },
    bottomBar = {
      NavigationBar(
        modifier = Modifier
          .windowInsetsPadding(WindowInsets.navigationBars)
          .testTag("whatsapp_bottom_navigation"),
        containerColor = if (isDark) WhatsAppDarkSurface else MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
      ) {
        // Tab 1: Chats
        NavigationBarItem(
          selected = selectedTab == MainTab.CHATS,
          onClick = { onSelectTab(MainTab.CHATS) },
          icon = {
            BadgedBox(
              badge = {
                if (totalUnread > 0) {
                  Badge(
                    containerColor = WhatsAppUnreadBadge,
                    contentColor = Color.White
                  ) {
                    Text(text = if (totalUnread > 99) "99+" else totalUnread.toString())
                  }
                }
              }
            ) {
              Icon(
                imageVector = if (selectedTab == MainTab.CHATS) Icons.Filled.Chat else Icons.Outlined.Chat,
                contentDescription = "Chats"
              )
            }
          },
          label = {
            Text(
              text = "Chats",
              fontWeight = if (selectedTab == MainTab.CHATS) FontWeight.Bold else FontWeight.Normal,
              fontSize = 12.sp
            )
          },
          colors = NavigationBarItemDefaults.colors(
            indicatorColor = WhatsAppAccentGreen.copy(alpha = 0.2f),
            selectedIconColor = WhatsAppAccentGreen,
            selectedTextColor = WhatsAppAccentGreen
          ),
          modifier = Modifier.testTag("tab_chats")
        )

        // Tab 2: Updates
        NavigationBarItem(
          selected = selectedTab == MainTab.UPDATES,
          onClick = { onSelectTab(MainTab.UPDATES) },
          icon = {
            BadgedBox(
              badge = {
                if (hasUnviewedStatus) {
                  Badge(containerColor = WhatsAppAccentGreen)
                }
              }
            ) {
              Icon(
                imageVector = if (selectedTab == MainTab.UPDATES) Icons.Filled.Update else Icons.Outlined.Update,
                contentDescription = "Updates"
              )
            }
          },
          label = {
            Text(
              text = "Updates",
              fontWeight = if (selectedTab == MainTab.UPDATES) FontWeight.Bold else FontWeight.Normal,
              fontSize = 12.sp
            )
          },
          colors = NavigationBarItemDefaults.colors(
            indicatorColor = WhatsAppAccentGreen.copy(alpha = 0.2f),
            selectedIconColor = WhatsAppAccentGreen,
            selectedTextColor = WhatsAppAccentGreen
          ),
          modifier = Modifier.testTag("tab_updates")
        )

        // Tab 3: Communities
        NavigationBarItem(
          selected = selectedTab == MainTab.COMMUNITIES,
          onClick = { onSelectTab(MainTab.COMMUNITIES) },
          icon = {
            Icon(
              imageVector = if (selectedTab == MainTab.COMMUNITIES) Icons.Filled.Groups else Icons.Outlined.Groups,
              contentDescription = "Communities"
            )
          },
          label = {
            Text(
              text = "Communities",
              fontWeight = if (selectedTab == MainTab.COMMUNITIES) FontWeight.Bold else FontWeight.Normal,
              fontSize = 12.sp
            )
          },
          colors = NavigationBarItemDefaults.colors(
            indicatorColor = WhatsAppAccentGreen.copy(alpha = 0.2f),
            selectedIconColor = WhatsAppAccentGreen,
            selectedTextColor = WhatsAppAccentGreen
          ),
          modifier = Modifier.testTag("tab_communities")
        )

        // Tab 4: Calls
        NavigationBarItem(
          selected = selectedTab == MainTab.CALLS,
          onClick = { onSelectTab(MainTab.CALLS) },
          icon = {
            Icon(
              imageVector = if (selectedTab == MainTab.CALLS) Icons.Filled.Call else Icons.Outlined.Call,
              contentDescription = "Calls"
            )
          },
          label = {
            Text(
              text = "Calls",
              fontWeight = if (selectedTab == MainTab.CALLS) FontWeight.Bold else FontWeight.Normal,
              fontSize = 12.sp
            )
          },
          colors = NavigationBarItemDefaults.colors(
            indicatorColor = WhatsAppAccentGreen.copy(alpha = 0.2f),
            selectedIconColor = WhatsAppAccentGreen,
            selectedTextColor = WhatsAppAccentGreen
          ),
          modifier = Modifier.testTag("tab_calls")
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (selectedTab) {
        MainTab.CHATS -> {
          ChatsTab(
            chats = chats,
            selectedFilter = selectedFilter,
            onSelectFilter = onSelectFilter,
            onChatClick = onChatClick,
            onNewChatClick = onNewChatClick,
            onTogglePin = onTogglePin,
            onToggleMute = onToggleMute,
            onToggleArchive = onToggleArchive,
            onDeleteChat = onDeleteChat
          )
        }
        MainTab.UPDATES -> {
          UpdatesTab(
            statusUpdates = statusUpdates,
            onStatusClick = onStatusClick,
            onAddStatusClick = onAddStatusClick
          )
        }
        MainTab.COMMUNITIES -> {
          CommunitiesTab(
            onCommunityClick = { name ->
              onNewChatClick()
            }
          )
        }
        MainTab.CALLS -> {
          CallsTab(
            calls = calls,
            onStartCall = onStartCall,
            onNewCallClick = onNewCallClick
          )
        }
      }
    }
  }
}
