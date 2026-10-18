package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CallEntity
import com.example.data.model.ChatEntity
import com.example.data.model.ContactEntity
import com.example.data.model.MessageEntity
import com.example.data.model.StatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
  @Query("SELECT * FROM chats ORDER BY isPinned DESC, lastMessageTimestamp DESC")
  fun getAllChats(): Flow<List<ChatEntity>>

  @Query("SELECT * FROM chats WHERE id = :chatId LIMIT 1")
  fun getChatById(chatId: String): Flow<ChatEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateChat(chat: ChatEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertChats(chats: List<ChatEntity>)

  @Query("UPDATE chats SET lastMessage = :lastMessage, lastMessageTimestamp = :timestamp, unreadCount = :unreadCount WHERE id = :chatId")
  suspend fun updateChatLastMessage(chatId: String, lastMessage: String, timestamp: Long, unreadCount: Int)

  @Query("UPDATE chats SET unreadCount = 0 WHERE id = :chatId")
  suspend fun clearUnreadCount(chatId: String)

  @Query("UPDATE chats SET isPinned = NOT isPinned WHERE id = :chatId")
  suspend fun togglePinChat(chatId: String)

  @Query("UPDATE chats SET isMuted = NOT isMuted WHERE id = :chatId")
  suspend fun toggleMuteChat(chatId: String)

  @Query("UPDATE chats SET isArchived = NOT isArchived WHERE id = :chatId")
  suspend fun toggleArchiveChat(chatId: String)

  @Query("DELETE FROM chats WHERE id = :chatId")
  suspend fun deleteChat(chatId: String)

  // Messages
  @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp ASC")
  fun getMessagesForChat(chatId: String): Flow<List<MessageEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMessage(message: MessageEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMessages(messages: List<MessageEntity>)

  @Query("UPDATE messages SET reaction = :reaction WHERE id = :messageId")
  suspend fun setMessageReaction(messageId: String, reaction: String?)

  @Query("DELETE FROM messages WHERE id = :messageId")
  suspend fun deleteMessage(messageId: String)

  // Status updates
  @Query("SELECT * FROM status_updates ORDER BY timestamp DESC")
  fun getAllStatusUpdates(): Flow<List<StatusEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertStatus(status: StatusEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertStatusList(statuses: List<StatusEntity>)

  @Query("UPDATE status_updates SET isViewed = 1 WHERE id = :statusId")
  suspend fun markStatusViewed(statusId: String)

  // Calls
  @Query("SELECT * FROM calls ORDER BY timestamp DESC")
  fun getAllCalls(): Flow<List<CallEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCall(call: CallEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCalls(calls: List<CallEntity>)

  @Query("DELETE FROM calls")
  suspend fun clearCallLog()

  // Contacts
  @Query("SELECT * FROM contacts ORDER BY name ASC")
  fun getAllContacts(): Flow<List<ContactEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertContact(contact: ContactEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertContacts(contacts: List<ContactEntity>)
}
