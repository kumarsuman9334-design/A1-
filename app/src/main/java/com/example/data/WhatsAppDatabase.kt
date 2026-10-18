package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.ChatDao
import com.example.data.model.CallEntity
import com.example.data.model.ChatEntity
import com.example.data.model.ContactEntity
import com.example.data.model.MessageEntity
import com.example.data.model.StatusEntity

@Database(
  entities = [
    ChatEntity::class,
    MessageEntity::class,
    StatusEntity::class,
    CallEntity::class,
    ContactEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class WhatsAppDatabase : RoomDatabase() {
  abstract fun chatDao(): ChatDao

  companion object {
    @Volatile
    private var INSTANCE: WhatsAppDatabase? = null

    fun getDatabase(context: Context): WhatsAppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          WhatsAppDatabase::class.java,
          "whatsapp_database"
        ).fallbackToDestructiveMigration().build()
        INSTANCE = instance
        instance
      }
    }
  }
}
