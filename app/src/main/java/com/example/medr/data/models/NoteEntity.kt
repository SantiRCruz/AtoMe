package com.example.medr.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val video:String = "",
    val title:String = ""
)
