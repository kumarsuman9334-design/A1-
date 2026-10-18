package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.WhatsAppDatabase
import com.example.data.WhatsAppRepository
import com.example.data.model.CallEntity
import com.example.data.model.ChatEntity
import com.example.data.model.ContactEntity
import com.example.data.model.MessageEntity
import com.example.data.model.MessageType
import com.example.data.model.StatusEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainTab {
  CHATS, UPDATES, COMMUNITIES, CALLS
}

enum class ChatFilter {
  ALL, UNREAD, FAVORITES, GROUPS
}

sealed class ScreenDestination {
  object Main : ScreenDestination()
  data class Conversation(val chatId: String) : ScreenDestination()
  data class StatusViewer(val statusId: String) : ScreenDestination()
  object CreateStatus : ScreenDestination()
  data class ActiveCall(val contactName: String, val isVideo: Boolean) : ScreenDestination()
  data class ContactInfo(val contactId: String) : ScreenDestination()
  object Settings : ScreenDestination()
}

data class UserProfile(
  val name: String = "John Doe",
  val phone: String = "+1 555-0101",
  val about: String = "Hey there! I am using WhatsApp.",
  val avatarUrl: String = ""
)

class WhatsAppViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: WhatsAppRepository

  init {
    val database = WhatsAppDatabase.getDatabase(application)
    repository = WhatsAppRepository(database.chatDao())
    viewModelScope.launch {
      repository.initializeSampleDataIfEmpty()
    }
  }

  // Navigation State
  private val _currentScreen = MutableStateFlow<ScreenDestination>(ScreenDestination.Main)
  val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

  // Tabs & Filters
  private val _selectedTab = MutableStateFlow(MainTab.CHATS)
  val selectedTab: StateFlow<MainTab> = _selectedTab.asStateFlow()

  private val _selectedFilter = MutableStateFlow(ChatFilter.ALL)
  val selectedFilter: StateFlow<ChatFilter> = _selectedFilter.asStateFlow()

  // Search State
  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _isSearchActive = MutableStateFlow(false)
  val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()

  // Dark Theme Mode (null = system default, true = dark, false = light)
  private val _isDarkMode = MutableStateFlow<Boolean?>(null)
  val isDarkMode: StateFlow<Boolean?> = _isDarkMode.asStateFlow()

  // User Profile
  private val _userProfile = MutableStateFlow(UserProfile())
  val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

  // Active Chat Message Input State
  private val _messageInput = MutableStateFlow("")
  val messageInput: StateFlow<String> = _messageInput.asStateFlow()

  private val _replyingToMessage = MutableStateFlow<MessageEntity?>(null)
  val replyingToMessage: StateFlow<MessageEntity?> = _replyingToMessage.asStateFlow()

  // Voice Note Recording Simulator
  private val _isRecordingVoice = MutableStateFlow(false)
  val isRecordingVoice: StateFlow<Boolean> = _isRecordingVoice.asStateFlow()

  private val _recordingDurationSec = MutableStateFlow(0)
  val recordingDurationSec: StateFlow<Int> = _recordingDurationSec.asStateFlow()

  private var recordingJob: Job? = null

  // Contact Picker BottomSheet
  private val _showNewChatDialog = MutableStateFlow(false)
  val showNewChatDialog: StateFlow<Boolean> = _showNewChatDialog.asStateFlow()

  // Data Flows
  val allContacts: StateFlow<List<ContactEntity>> = repository.allContacts.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptyList()
  )

  val allChats: StateFlow<List<ChatEntity>> = repository.allChats.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptyList()
  )

  // Filtered Chats
  val filteredChats: StateFlow<List<ChatEntity>> = combine(
    repository.allChats,
    _selectedFilter,
    _searchQuery
  ) { chats, filter, query ->
    chats.filter { chat ->
      val matchesFilter = when (filter) {
        ChatFilter.ALL -> !chat.isArchived
        ChatFilter.UNREAD -> chat.unreadCount > 0 && !chat.isArchived
        ChatFilter.FAVORITES -> chat.isPinned && !chat.isArchived
        ChatFilter.GROUPS -> chat.isGroup && !chat.isArchived
      }
      val matchesQuery = query.isBlank() ||
          chat.contactName.contains(query, ignoreCase = true) ||
          chat.lastMessage.contains(query, ignoreCase = true)
      matchesFilter && matchesQuery
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val statusUpdates: StateFlow<List<StatusEntity>> = repository.allStatusUpdates.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptyList()
  )

  val callLogs: StateFlow<List<CallEntity>> = repository.allCalls.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptyList()
  )

  fun getMessages(chatId: String): StateFlow<List<MessageEntity>> =
    repository.getMessages(chatId).stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

  fun getChatById(chatId: String): StateFlow<ChatEntity?> =
    repository.getChatById(chatId).stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      null
    )

  // Navigation actions
  fun navigateTo(screen: ScreenDestination) {
    _currentScreen.value = screen
    if (screen is ScreenDestination.Conversation) {
      viewModelScope.launch {
        repository.clearUnread(screen.chatId)
      }
    }
  }

  fun navigateBack() {
    _currentScreen.value = ScreenDestination.Main
    _replyingToMessage.value = null
    _messageInput.value = ""
    stopVoiceRecording(send = false)
  }

  fun selectTab(tab: MainTab) {
    _selectedTab.value = tab
    _searchQuery.value = ""
  }

  fun selectFilter(filter: ChatFilter) {
    _selectedFilter.value = filter
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setSearchActive(active: Boolean) {
    _isSearchActive.value = active
    if (!active) _searchQuery.value = ""
  }

  fun toggleDarkMode() {
    _isDarkMode.value = !(_isDarkMode.value ?: false)
  }

  fun setMessageInput(text: String) {
    _messageInput.value = text
  }

  fun setReplyingTo(message: MessageEntity?) {
    _replyingToMessage.value = message
  }

  fun setShowNewChatDialog(show: Boolean) {
    _showNewChatDialog.value = show
  }

  fun updateUserProfile(name: String, about: String) {
    _userProfile.value = _userProfile.value.copy(name = name, about = about)
  }

  // Voice recording simulation
  fun startVoiceRecording() {
    _isRecordingVoice.value = true
    _recordingDurationSec.value = 0
    recordingJob?.cancel()
    recordingJob = viewModelScope.launch {
      while (_isRecordingVoice.value) {
        delay(1000)
        _recordingDurationSec.value += 1
      }
    }
  }

  fun stopVoiceRecording(send: Boolean, chatId: String? = null) {
    val duration = _recordingDurationSec.value
    recordingJob?.cancel()
    _isRecordingVoice.value = false
    _recordingDurationSec.value = 0

    if (send && chatId != null && duration > 0) {
      viewModelScope.launch {
        repository.sendMessage(
          chatId = chatId,
          text = "Voice message",
          type = MessageType.VOICE,
          voiceDurationSec = duration
        )
      }
    }
  }

  // Message Actions
  fun sendTextMessage(chatId: String) {
    val text = _messageInput.value.trim()
    if (text.isEmpty()) return

    val replyPreview = _replyingToMessage.value?.text
    _messageInput.value = ""
    _replyingToMessage.value = null

    viewModelScope.launch {
      repository.sendMessage(
        chatId = chatId,
        text = text,
        type = MessageType.TEXT,
        replyToText = replyPreview
      )
    }
  }

  fun sendMessage(chatId: String, text: String) {
    if (text.isBlank()) return
    viewModelScope.launch {
      repository.sendMessage(
        chatId = chatId,
        text = text,
        type = MessageType.TEXT
      )
    }
  }

  fun sendPhotoMessage(chatId: String, caption: String = "Check this out! 📸") {
    viewModelScope.launch {
      repository.sendMessage(
        chatId = chatId,
        text = caption,
        type = MessageType.IMAGE
      )
    }
  }

  fun sendLocationMessage(chatId: String) {
    viewModelScope.launch {
      repository.sendMessage(
        chatId = chatId,
        text = "Current Location: Silicon Valley, CA 📍",
        type = MessageType.LOCATION
      )
    }
  }

  fun setReaction(messageId: String, reaction: String?) {
    viewModelScope.launch {
      repository.setReaction(messageId, reaction)
    }
  }

  fun deleteMessage(messageId: String) {
    viewModelScope.launch {
      repository.deleteMessage(messageId)
    }
  }

  fun togglePin(chatId: String) {
    viewModelScope.launch { repository.togglePin(chatId) }
  }

  fun toggleMute(chatId: String) {
    viewModelScope.launch { repository.toggleMute(chatId) }
  }

  fun toggleArchive(chatId: String) {
    viewModelScope.launch { repository.toggleArchive(chatId) }
  }

  fun deleteChat(chatId: String) {
    viewModelScope.launch { repository.deleteChat(chatId) }
  }

  fun postStatus(text: String, bgColorHex: String) {
    if (text.isBlank()) return
    viewModelScope.launch {
      repository.postStatus(text, bgColorHex)
    }
  }

  fun markStatusViewed(statusId: String) {
    viewModelScope.launch {
      repository.markStatusViewed(statusId)
    }
  }

  fun startCall(contactName: String, isVideo: Boolean) {
    navigateTo(ScreenDestination.ActiveCall(contactName, isVideo))
  }

  fun endCall(contactName: String, isVideo: Boolean, durationSec: Int) {
    viewModelScope.launch {
      repository.recordCall(
        contactName = contactName,
        isVideo = isVideo,
        isIncoming = false,
        durationSec = durationSec
      )
    }
    navigateBack()
  }

  fun startChatWithContact(contact: ContactEntity) {
    viewModelScope.launch {
      val chatId = repository.createNewChat(contact)
      _showNewChatDialog.value = false
      navigateTo(ScreenDestination.Conversation(chatId))
    }
  }

  fun addNewContactAndChat(name: String, phone: String, bio: String) {
    viewModelScope.launch {
      val contact = repository.addNewContact(name, phone, bio)
      val chatId = repository.createNewChat(contact)
      _showNewChatDialog.value = false
      navigateTo(ScreenDestination.Conversation(chatId))
    }
  }
}
