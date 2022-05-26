package com.example.medr.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.medr.data.models.NoteEntity

@Dao
interface NoteDao {
    @Query("SELECT * FROM noteentity")
    suspend fun getNotes():List<NoteEntity>

    @Insert(onConflict = REPLACE)
    suspend fun saveNote(noteEntity: NoteEntity):Long

    @Delete
    suspend fun deleteNote(noteEntity: NoteEntity):Int
}