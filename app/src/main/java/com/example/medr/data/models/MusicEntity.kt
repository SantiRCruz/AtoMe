package com.example.medr.data.models

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MusicEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val music: String = "",
    val title: String = ""
)
