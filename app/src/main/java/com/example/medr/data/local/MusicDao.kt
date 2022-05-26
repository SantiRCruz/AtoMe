package com.example.medr.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.medr.data.models.MusicEntity

@Dao
interface MusicDao{
    @Query("SELECT * FROM MusicEntity")
    suspend fun getMusic():List<MusicEntity>

    @Insert(onConflict = REPLACE)
    suspend fun saveMusic(musicEntity: MusicEntity):Long

    @Delete
    suspend fun deleteMusic(musicEntity: MusicEntity):Int
}