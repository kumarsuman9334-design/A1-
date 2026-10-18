package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.MainTab
import com.example.ui.ScreenDestination
import com.example.ui.WhatsAppViewModel
import com.example.ui.screens.ActiveCallScreen
import com.example.ui.screens.ChatConversationScreen
import com.example.ui.screens.ContactDetailScreen
import com.example.ui.screens.CreateStatusScreen
import com.example.ui.screens.MainTabsScreen
import com.example.ui.screens.NewChatBottomSheet
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StatusViewerScreen
import com.example.ui.theme.WhatsAppTheme

class MainActivity : ComponentActivity() {
  private val viewModel: WhatsAppViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      WhatsAppApp(viewModel = viewModel)
    }
  }
}

@Composable
fun WhatsAppApp(viewModel: WhatsAppViewModel) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  val selectedTab by viewModel.selectedTab.collectAsState()
  val selectedFilter by viewModel.selectedFilter.collectAsState()
  val filteredChats by viewModel.filteredChats.collectAsState()
  val statusUpdates by viewModel.statusUpdates.collectAsState()
  val callLogs by viewModel.callLogs.collectAsState()
  val allContacts by viewModel.allContacts.collectAsState()
  val isSearchActive by viewModel.isSearchActive.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  val isDarkMode by viewModel.isDarkMode.collectAsState()
  val userProfile by viewModel.userProfile.collectAsState()
  val showNewChatDialog by viewModel.showNewChatDialog.collectAsState()

  val darkTheme = isDarkMode ?: isSystemInDarkTheme()

  WhatsAppTheme(darkTheme = darkTheme) {
    Surface(modifier = Modifier.fillMaxSize()) {
      when (val screen = currentScreen) {
        is ScreenDestination.Main -> {
          MainTabsScreen(
            selectedTab = selectedTab,
            onSelectTab = { viewModel.selectTab(it) },
            chats = filteredChats,
            selectedFilter = selectedFilter,
            onSelectFilter = { viewModel.selectFilter(it) },
            statusUpdates = statusUpdates,
            calls = callLogs,
            isSearchActive = isSearchActive,
            searchQuery = searchQuery,
            onSearchQueryChange = { viewModel.setSearchQuery(it) },
            onSearchActiveChange = { viewModel.setSearchActive(it) },
            onChatClick = { chatId ->
              viewModel.navigateTo(ScreenDestination.Conversation(chatId))
            },
            onNewChatClick = { viewModel.setShowNewChatDialog(true) },
            onTogglePin = { viewModel.togglePin(it) },
            onToggleMute = { viewModel.toggleMute(it) },
            onToggleArchive = { viewModel.toggleArchive(it) },
            onDeleteChat = { viewModel.deleteChat(it) },
            onStatusClick = { statusId ->
              viewModel.markStatusViewed(statusId)
              viewModel.navigateTo(ScreenDestination.StatusViewer(statusId))
            },
            onAddStatusClick = {
              viewModel.navigateTo(ScreenDestination.CreateStatus)
            },
            onStartCall = { contactName, isVideo ->
              viewModel.startCall(contactName, isVideo)
            },
            onNewCallClick = { viewModel.setShowNewChatDialog(true) },
            onCameraClick = {
              viewModel.navigateTo(ScreenDestination.CreateStatus)
            },
            onSettingsClick = {
              viewModel.navigateTo(ScreenDestination.Settings)
            },
            onToggleTheme = { viewModel.toggleDarkMode() }
          )
        }

        is ScreenDestination.Conversation -> {
          val chat by viewModel.getChatById(screen.chatId).collectAsState()
          val messages by viewModel.getMessages(screen.chatId).collectAsState()
          val messageInput by viewModel.messageInput.collectAsState()
          val replyingTo by viewModel.replyingToMessage.collectAsState()
          val isRecordingVoice by viewModel.isRecordingVoice.collectAsState()
          val recordingDurationSec by viewModel.recordingDurationSec.collectAsState()

          ChatConversationScreen(
            chat = chat,
            messages = messages,
            messageInput = messageInput,
            onMessageInputChange = { viewModel.setMessageInput(it) },
            onSendMessage = { viewModel.sendTextMessage(screen.chatId) },
            replyingToMessage = replyingTo,
            onCancelReply = { viewModel.setReplyingTo(null) },
            isRecordingVoice = isRecordingVoice,
            recordingDurationSec = recordingDurationSec,
            onStartVoiceRecording = { viewModel.startVoiceRecording() },
            onStopVoiceRecording = { send ->
              viewModel.stopVoiceRecording(send = send, chatId = screen.chatId)
            },
            onSendPhoto = { viewModel.sendPhotoMessage(screen.chatId) },
            onSendLocation = { viewModel.sendLocationMessage(screen.chatId) },
            onReactionSelect = { msgId, reaction ->
              viewModel.setReaction(msgId, reaction)
            },
            onDeleteMessage = { msgId -> viewModel.deleteMessage(msgId) },
            onSetReplyingTo = { msg -> viewModel.setReplyingTo(msg) },
            onStartCall = { name, isVideo -> viewModel.startCall(name, isVideo) },
            onViewContact = { contactId ->
              viewModel.navigateTo(ScreenDestination.ContactInfo(contactId))
            },
            onBack = { viewModel.navigateBack() }
          )
        }

        is ScreenDestination.StatusViewer -> {
          val status = statusUpdates.find { it.id == screen.statusId }
          StatusViewerScreen(
            status = status,
            onReplyStatus = { reply ->
              // If reply to status, find chat or create chat and send
              status?.let {
                val targetChat = filteredChats.find { c -> c.contactName == it.contactName }
                if (targetChat != null) {
                  viewModel.sendMessage(targetChat.id, "Replied to status: $reply")
                }
              }
            },
            onClose = { viewModel.navigateBack() }
          )
        }

        is ScreenDestination.CreateStatus -> {
          CreateStatusScreen(
            onPostStatus = { text, bgHex ->
              viewModel.postStatus(text, bgHex)
            },
            onClose = { viewModel.navigateBack() }
          )
        }

        is ScreenDestination.ActiveCall -> {
          ActiveCallScreen(
            contactName = screen.contactName,
            isVideo = screen.isVideo,
            onEndCall = { duration ->
              viewModel.endCall(screen.contactName, screen.isVideo, duration)
            }
          )
        }

        is ScreenDestination.ContactInfo -> {
          val contact = allContacts.find { it.id == screen.contactId }
          ContactDetailScreen(
            contact = contact,
            onStartCall = { name, isVideo ->
              viewModel.startCall(name, isVideo)
            },
            onStartChat = { c ->
              viewModel.startChatWithContact(c)
            },
            onBack = { viewModel.navigateBack() }
          )
        }

        is ScreenDestination.Settings -> {
          SettingsScreen(
            userProfile = userProfile,
            onUpdateProfile = { name, about ->
              viewModel.updateUserProfile(name, about)
            },
            onToggleTheme = { viewModel.toggleDarkMode() },
            onBack = { viewModel.navigateBack() }
          )
        }
      }

      // New Chat Bottom Sheet
      if (showNewChatDialog) {
        NewChatBottomSheet(
          contacts = allContacts,
          onSelectContact = { contact ->
            viewModel.startChatWithContact(contact)
          },
          onAddNewContact = { name, phone, bio ->
            viewModel.addNewContactAndChat(name, phone, bio)
          },
          onDismiss = { viewModel.setShowNewChatDialog(false) }
        )
      }
    }
  }
}
