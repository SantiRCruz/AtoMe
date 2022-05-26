package com.example.medr.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.medr.data.models.ImageEntity

@Dao
interface ImageDao {
    @Query("SELECT * FROM imageentity")
    suspend fun getImages():List<ImageEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insertImage(imageEntity: ImageEntity):Long

    @Delete
    suspend fun deleteImage(imageEntity: ImageEntity):Int
}