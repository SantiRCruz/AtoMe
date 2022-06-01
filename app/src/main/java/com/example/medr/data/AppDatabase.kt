package com.example.medr.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.medr.data.local.ImageDao
import com.example.medr.data.local.MusicDao
import com.example.medr.data.local.NoteDao
import com.example.medr.data.models.ImageEntity
import com.example.medr.data.models.MusicEntity
import com.example.medr.data.models.NoteEntity

@Database(entities = [ImageEntity::class, MusicEntity::class, NoteEntity::class], version = 2)
abstract class AppDatabase:RoomDatabase() {

    abstract fun ImageDao(): ImageDao
    abstract fun MusicDao():MusicDao
    abstract fun NoteDao():NoteDao

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getDB(context: Context):AppDatabase{
            INSTANCE = INSTANCE?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "general_table"
            ).build()
            return INSTANCE!!
        }
    }
}