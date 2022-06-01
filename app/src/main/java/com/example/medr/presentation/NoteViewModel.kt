package com.example.medr.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.medr.core.Result
import com.example.medr.data.local.MusicDao
import com.example.medr.data.local.NoteDao
import com.example.medr.data.models.NoteEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class NoteViewModel(private val dao : NoteDao):ViewModel() {
    fun fetchNotes():StateFlow<Result<List<NoteEntity>>> = flow {
        kotlin.runCatching {
            dao.getNotes()
        }.onSuccess {
            emit(Result.Success(it))
        }.onFailure {
            emit(Result.Failure(Exception(it)))
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        Result.Loading()
    )
    fun saveNotes(noteEntity: NoteEntity):StateFlow<Result<Long>> = flow {
        kotlin.runCatching {
            dao.saveNote(noteEntity)
        }.onSuccess {
            emit(Result.Success(it))
        }.onFailure {
            emit(Result.Failure(Exception(it)))
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        Result.Loading()
    )
    fun deleteNotes(noteEntity: NoteEntity):StateFlow<Result<Int>> = flow {
        kotlin.runCatching {
            dao.deleteNote(noteEntity)
        }.onSuccess {
            emit(Result.Success(it))
        }.onFailure {
            emit(Result.Failure(Exception(it)))
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        Result.Loading()
    )
}
class NoteViewModelFactory(private val dao : NoteDao):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(NoteDao::class.java).newInstance(dao)
    }
}