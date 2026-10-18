package com.example.data

import com.example.data.dao.ChatDao
import com.example.data.model.CallEntity
import com.example.data.model.ChatEntity
import com.example.data.model.ContactEntity
import com.example.data.model.MessageEntity
import com.example.data.model.MessageStatus
import com.example.data.model.MessageType
import com.example.data.model.StatusEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

class WhatsAppRepository(
  private val chatDao: ChatDao,
  private val repositoryScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
  val allChats: Flow<List<ChatEntity>> = chatDao.getAllChats()
  val allStatusUpdates: Flow<List<StatusEntity>> = chatDao.getAllStatusUpdates()
  val allCalls: Flow<List<CallEntity>> = chatDao.getAllCalls()
  val allContacts: Flow<List<ContactEntity>> = chatDao.getAllContacts()

  suspend fun initializeSampleDataIfEmpty() {
    val existingChats = chatDao.getAllChats().first()
    if (existingChats.isNotEmpty()) return

    val sampleContacts = listOf(
      ContactEntity(
        id = "c1",
        name = "Sarah Connor",
        phone = "+1 555-0199",
        statusBio = "Working from home ☕",
        isOnline = true,
        lastSeenText = "online"
      ),
      ContactEntity(
        id = "c2",
        name = "Android Dev Team",
        phone = "+1 555-0100",
        statusBio = "Building the future of mobile 🚀",
        isOnline = true,
        lastSeenText = "5 members online"
      ),
      ContactEntity(
        id = "c3",
        name = "Alex Rivera",
        phone = "+1 555-0142",
        statusBio = "Can't talk, WhatsApp only",
        isOnline = true,
        lastSeenText = "online"
      ),
      ContactEntity(
        id = "c4",
        name = "Mom ❤️",
        phone = "+1 555-0112",
        statusBio = "Family first ❤️",
        isOnline = false,
        lastSeenText = "last seen today at 09:30 AM"
      ),
      ContactEntity(
        id = "c5",
        name = "David Miller",
        phone = "+1 555-0188",
        statusBio = "At the gym 🏋️‍♂️",
        isOnline = false,
        lastSeenText = "last seen yesterday at 11:45 PM"
      ),
      ContactEntity(
        id = "c6",
        name = "Tech Community",
        phone = "+1 555-0177",
        statusBio = "Daily tech news and discussions",
        isOnline = true,
        lastSeenText = "128 members"
      ),
      ContactEntity(
        id = "c7",
        name = "Emma Watson",
        phone = "+1 555-0164",
        statusBio = "Traveling around the world ✈️",
        isOnline = true,
        lastSeenText = "online"
      )
    )
    chatDao.insertContacts(sampleContacts)

    val now = System.currentTimeMillis()
    val min = 60 * 1000L
    val hour = 60 * min

    val sampleChats = listOf(
      ChatEntity(
        id = "chat_1",
        contactId = "c1",
        contactName = "Sarah Connor",
        isGroup = false,
        lastMessage = "Hey! Did you check out the new design system?",
        lastMessageTimestamp = now - 2 * min,
        unreadCount = 2,
        isPinned = true,
        isOnline = true
      ),
      ChatEntity(
        id = "chat_2",
        contactId = "c2",
        contactName = "Android Dev Team",
        isGroup = true,
        lastMessage = "Alex: Kotlin 2.2 release notes look incredible!",
        lastMessageTimestamp = now - 15 * min,
        unreadCount = 5,
        isPinned = true,
        isOnline = true
      ),
      ChatEntity(
        id = "chat_3",
        contactId = "c3",
        contactName = "Alex Rivera",
        isGroup = false,
        lastMessage = "🎤 Voice message (0:24)",
        lastMessageTimestamp = now - 45 * min,
        unreadCount = 0,
        isPinned = false,
        isOnline = true
      ),
      ChatEntity(
        id = "chat_4",
        contactId = "c4",
        contactName = "Mom ❤️",
        isGroup = false,
        lastMessage = "Call me when you're free, dear! ❤️",
        lastMessageTimestamp = now - 3 * hour,
        unreadCount = 0,
        isPinned = false,
        isOnline = false
      ),
      ChatEntity(
        id = "chat_5",
        contactId = "c5",
        contactName = "David Miller",
        isGroup = false,
        lastMessage = "See you tomorrow morning at 8:00 AM",
        lastMessageTimestamp = now - 7 * hour,
        unreadCount = 0,
        isPinned = false,
        isOnline = false
      ),
      ChatEntity(
        id = "chat_6",
        contactId = "c6",
        contactName = "Tech Community",
        isGroup = true,
        lastMessage = "Jordan: Jetpack Compose has made UI development so fast",
        lastMessageTimestamp = now - 12 * hour,
        unreadCount = 0,
        isPinned = false,
        isOnline = true
      ),
      ChatEntity(
        id = "chat_7",
        contactId = "c7",
        contactName = "Emma Watson",
        isGroup = false,
        lastMessage = "Just arrived in Tokyo! The cherry blossoms are breathtaking 🌸",
        lastMessageTimestamp = now - 24 * hour,
        unreadCount = 0,
        isPinned = false,
        isOnline = true
      )
    )
    chatDao.insertChats(sampleChats)

    // Sample Messages for Chat 1 (Sarah)
    val sarahMessages = listOf(
      MessageEntity(
        id = "m1_1",
        chatId = "chat_1",
        text = "Hi! How is the project coming along?",
        timestamp = now - 25 * min,
        isOutgoing = false,
        status = MessageStatus.READ
      ),
      MessageEntity(
        id = "m1_2",
        chatId = "chat_1",
        text = "Going great! Just polishing up the WhatsApp interface.",
        timestamp = now - 20 * min,
        isOutgoing = true,
        status = MessageStatus.READ,
        reaction = "🚀"
      ),
      MessageEntity(
        id = "m1_3",
        chatId = "chat_1",
        text = "That sounds awesome. Are voice notes and status stories ready too?",
        timestamp = now - 10 * min,
        isOutgoing = false,
        status = MessageStatus.READ
      ),
      MessageEntity(
        id = "m1_4",
        chatId = "chat_1",
        text = "Yes, full audio recording and playback waveforms!",
        timestamp = now - 8 * min,
        isOutgoing = true,
        status = MessageStatus.READ
      ),
      MessageEntity(
        id = "m1_5",
        chatId = "chat_1",
        text = "Voice message demo",
        timestamp = now - 5 * min,
        isOutgoing = true,
        type = MessageType.VOICE,
        voiceDurationSec = 18,
        status = MessageStatus.READ
      ),
      MessageEntity(
        id = "m1_6",
        chatId = "chat_1",
        text = "Hey! Did you check out the new design system?",
        timestamp = now - 2 * min,
        isOutgoing = false,
        status = MessageStatus.READ,
        reaction = "❤️"
      )
    )
    chatDao.insertMessages(sarahMessages)

    // Sample Messages for Chat 2 (Dev Team)
    val devTeamMessages = listOf(
      MessageEntity(
        id = "m2_1",
        chatId = "chat_2",
        text = "Welcome to the Sprint Planning meeting everyone! 📱",
        timestamp = now - 2 * hour,
        isOutgoing = false,
        status = MessageStatus.READ
      ),
      MessageEntity(
        id = "m2_2",
        chatId = "chat_2",
        text = "We are on track for today's release.",
        timestamp = now - 1 * hour,
        isOutgoing = true,
        status = MessageStatus.READ,
        reaction = "👍"
      ),
      MessageEntity(
        id = "m2_3",
        chatId = "chat_2",
        text = "Alex: Kotlin 2.2 release notes look incredible!",
        timestamp = now - 15 * min,
        isOutgoing = false,
        status = MessageStatus.READ
      )
    )
    chatDao.insertMessages(devTeamMessages)

    // Sample Messages for Chat 3 (Alex)
    val alexMessages = listOf(
      MessageEntity(
        id = "m3_1",
        chatId = "chat_3",
        text = "Hey man! Check out this quick audio update:",
        timestamp = now - 50 * min,
        isOutgoing = false,
        status = MessageStatus.READ
      ),
      MessageEntity(
        id = "m3_2",
        chatId = "chat_3",
        text = "Project roadmap review",
        timestamp = now - 45 * min,
        isOutgoing = false,
        type = MessageType.VOICE,
        voiceDurationSec = 24,
        status = MessageStatus.READ
      )
    )
    chatDao.insertMessages(alexMessages)

    // Sample Status Updates
    val sampleStatusUpdates = listOf(
      StatusEntity(
        id = "status_1",
        contactName = "Sarah Connor",
        timestamp = now - 35 * min,
        text = "Golden hour by the coast! 🌅 Loving the tranquility today.",
        backgroundColorHex = "#E65100",
        isViewed = false
      ),
      StatusEntity(
        id = "status_2",
        contactName = "Alex Rivera",
        timestamp = now - 2 * hour,
        text = "New sprint, new possibilities! 🚀 Let's code some magic.",
        backgroundColorHex = "#1E88E5",
        isViewed = false
      ),
      StatusEntity(
        id = "status_3",
        contactName = "Mom ❤️",
        timestamp = now - 4 * hour,
        text = "Fresh lavender blooms in the garden today 🌸🌿",
        backgroundColorHex = "#43A047",
        isViewed = false
      ),
      StatusEntity(
        id = "status_4",
        contactName = "David Miller",
        timestamp = now - 8 * hour,
        text = "PR hit today: 120kg bench press! 💪 Keep grinding.",
        backgroundColorHex = "#6A1B9A",
        isViewed = true
      ),
      StatusEntity(
        id = "status_5",
        contactName = "Emma Watson",
        timestamp = now - 14 * hour,
        text = "Tokyo by night! Neon wonderland 🏮✨",
        backgroundColorHex = "#D81B60",
        isViewed = true
      )
    )
    chatDao.insertStatusList(sampleStatusUpdates)

    // Sample Calls
    val sampleCalls = listOf(
      CallEntity(
        id = "call_1",
        contactName = "Sarah Connor",
        timestamp = now - 20 * min,
        isVideo = false,
        isIncoming = true,
        isMissed = false,
        durationSec = 245
      ),
      CallEntity(
        id = "call_2",
        contactName = "Alex Rivera",
        timestamp = now - 2 * hour,
        isVideo = true,
        isIncoming = false,
        isMissed = false,
        durationSec = 120
      ),
      CallEntity(
        id = "call_3",
        contactName = "Mom ❤️",
        timestamp = now - 5 * hour,
        isVideo = false,
        isIncoming = true,
        isMissed = true,
        durationSec = 0
      ),
      CallEntity(
        id = "call_4",
        contactName = "Emma Watson",
        timestamp = now - 1 * (24 * hour),
        isVideo = true,
        isIncoming = false,
        isMissed = false,
        durationSec = 612
      )
    )
    chatDao.insertCalls(sampleCalls)
  }

  fun getMessages(chatId: String): Flow<List<MessageEntity>> = chatDao.getMessagesForChat(chatId)
  fun getChatById(chatId: String): Flow<ChatEntity?> = chatDao.getChatById(chatId)

  suspend fun sendMessage(
    chatId: String,
    text: String,
    type: MessageType = MessageType.TEXT,
    voiceDurationSec: Int = 0,
    replyToText: String? = null
  ) {
    val messageId = UUID.randomUUID().toString()
    val now = System.currentTimeMillis()
    val message = MessageEntity(
      id = messageId,
      chatId = chatId,
      text = text,
      timestamp = now,
      isOutgoing = true,
      status = MessageStatus.SENT,
      type = type,
      voiceDurationSec = voiceDurationSec,
      replyToText = replyToText
    )
    chatDao.insertMessage(message)

    val lastMsgSummary = when (type) {
      MessageType.VOICE -> "🎤 Voice message ($voiceDurationSec s)"
      MessageType.IMAGE -> "📷 Photo"
      MessageType.LOCATION -> "📍 Location"
      MessageType.TEXT -> text
    }
    chatDao.updateChatLastMessage(chatId, lastMsgSummary, now, 0)

    // Trigger auto-reply from contact after short delay for realistic WhatsApp experience!
    repositoryScope.launch {
      delay(1200)
      chatDao.insertMessage(message.copy(status = MessageStatus.DELIVERED))
      delay(800)
      chatDao.insertMessage(message.copy(status = MessageStatus.READ))

      // Contact typing delay
      delay(1200)
      val replyText = generateContextualReply(text, type)
      val replyMsg = MessageEntity(
        id = UUID.randomUUID().toString(),
        chatId = chatId,
        text = replyText,
        timestamp = System.currentTimeMillis(),
        isOutgoing = false,
        status = MessageStatus.READ
      )
      chatDao.insertMessage(replyMsg)
      chatDao.updateChatLastMessage(chatId, replyText, replyMsg.timestamp, 0)
    }
  }

  private fun generateContextualReply(userMsg: String, type: MessageType): String {
    val lower = userMsg.lowercase()
    return when {
      type == MessageType.VOICE -> "Awesome voice note! Heard you loud and clear 🎧👍"
      lower.contains("hello") || lower.contains("hi") || lower.contains("hey") ->
        "Hey! Great to hear from you. How are things going?"
      lower.contains("how are you") || lower.contains("how's it going") ->
        "I'm doing great, thanks for asking! What are you working on today?"
      lower.contains("status") ->
        "I just posted a new status story! Go check the Updates tab 🌟"
      lower.contains("call") ->
        "Sure, feel free to tap the call button at the top whenever you're ready! 📞"
      lower.contains("photo") || lower.contains("picture") ->
        "Love it! Looking crisp and clean ✨"
      lower.contains("what") || lower.contains("why") || lower.contains("when") ->
        "That's an interesting question! Let's catch up on that shortly."
      lower.contains("thanks") || lower.contains("thank you") ->
        "You're very welcome! Always here 😊"
      else -> {
        val genericReplies = listOf(
          "Got it! That sounds really interesting 👍",
          "Totally agree with you on that!",
          "Nice! Let me check that and get right back to you.",
          "Super cool! Thanks for sharing 🙌",
          "Sounds like a plan! Let's do it 🚀"
        )
        genericReplies.random()
      }
    }
  }

  suspend fun setReaction(messageId: String, reaction: String?) {
    chatDao.setMessageReaction(messageId, reaction)
  }

  suspend fun deleteMessage(messageId: String) {
    chatDao.deleteMessage(messageId)
  }

  suspend fun clearUnread(chatId: String) {
    chatDao.clearUnreadCount(chatId)
  }

  suspend fun togglePin(chatId: String) {
    chatDao.togglePinChat(chatId)
  }

  suspend fun toggleMute(chatId: String) {
    chatDao.toggleMuteChat(chatId)
  }

  suspend fun toggleArchive(chatId: String) {
    chatDao.toggleArchiveChat(chatId)
  }

  suspend fun deleteChat(chatId: String) {
    chatDao.deleteChat(chatId)
  }

  suspend fun markStatusViewed(statusId: String) {
    chatDao.markStatusViewed(statusId)
  }

  suspend fun postStatus(text: String, bgColorHex: String = "#008069") {
    val status = StatusEntity(
      id = UUID.randomUUID().toString(),
      contactName = "My Status",
      isMyStatus = true,
      timestamp = System.currentTimeMillis(),
      text = text,
      backgroundColorHex = bgColorHex,
      isViewed = true
    )
    chatDao.insertStatus(status)
  }

  suspend fun recordCall(contactName: String, isVideo: Boolean, isIncoming: Boolean, durationSec: Int) {
    val call = CallEntity(
      id = UUID.randomUUID().toString(),
      contactName = contactName,
      timestamp = System.currentTimeMillis(),
      isVideo = isVideo,
      isIncoming = isIncoming,
      isMissed = durationSec == 0 && isIncoming,
      durationSec = durationSec
    )
    chatDao.insertCall(call)
  }

  suspend fun createNewChat(contact: ContactEntity): String {
    val existing = chatDao.getAllChats().first().find { it.contactId == contact.id }
    if (existing != null) return existing.id

    val newChatId = "chat_${UUID.randomUUID().toString().take(8)}"
    val chat = ChatEntity(
      id = newChatId,
      contactId = contact.id,
      contactName = contact.name,
      contactAvatar = contact.avatarUrl,
      isGroup = false,
      lastMessage = "Started a new conversation",
      lastMessageTimestamp = System.currentTimeMillis(),
      unreadCount = 0,
      isOnline = contact.isOnline
    )
    chatDao.insertOrUpdateChat(chat)
    return newChatId
  }

  suspend fun addNewContact(name: String, phone: String, bio: String): ContactEntity {
    val contact = ContactEntity(
      id = "c_${UUID.randomUUID().toString().take(6)}",
      name = name,
      phone = phone,
      statusBio = bio.ifBlank { "Hey there! I am using WhatsApp." },
      isOnline = true,
      lastSeenText = "online"
    )
    chatDao.insertContact(contact)
    return contact
  }
}
