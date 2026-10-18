package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(
  @PrimaryKey val id: String,
  val name: String,
  val phone: String,
  val avatarUrl: String = "",
  val statusBio: String = "Hey there! I am using WhatsApp.",
  val isOnline: Boolean = false,
  val lastSeenText: String = "online"
)

@Entity(tableName = "chats")
data class ChatEntity(
  @PrimaryKey val id: String,
  val contactId: String,
  val contactName: String,
  val contactAvatar: String = "",
  val isGroup: Boolean = false,
  val lastMessage: String = "",
  val lastMessageTimestamp: Long = System.currentTimeMillis(),
  val unreadCount: Int = 0,
  val isPinned: Boolean = false,
  val isMuted: Boolean = false,
  val isArchived: Boolean = false,
  val isOnline: Boolean = false,
  val typingStatus: Boolean = false
)

enum class MessageType {
  TEXT, VOICE, IMAGE, LOCATION
}

enum class MessageStatus {
  SENDING, SENT, DELIVERED, READ
}

@Entity(tableName = "messages")
data class MessageEntity(
  @PrimaryKey val id: String,
  val chatId: String,
  val text: String,
  val timestamp: Long = System.currentTimeMillis(),
  val isOutgoing: Boolean = true,
  val status: MessageStatus = MessageStatus.READ,
  val type: MessageType = MessageType.TEXT,
  val mediaUrl: String? = null,
  val voiceDurationSec: Int = 0,
  val reaction: String? = null,
  val replyToText: String? = null
)

@Entity(tableName = "status_updates")
data class StatusEntity(
  @PrimaryKey val id: String,
  val contactName: String,
  val contactAvatar: String = "",
  val isMyStatus: Boolean = false,
  val timestamp: Long = System.currentTimeMillis(),
  val text: String = "",
  val imageUrl: String? = null,
  val backgroundColorHex: String = "#008069",
  val isViewed: Boolean = false
)

@Entity(tableName = "calls")
data class CallEntity(
  @PrimaryKey val id: String,
  val contactName: String,
  val contactAvatar: String = "",
  val timestamp: Long = System.currentTimeMillis(),
  val isVideo: Boolean = false,
  val isIncoming: Boolean = true,
  val isMissed: Boolean = false,
  val durationSec: Int = 0
)
