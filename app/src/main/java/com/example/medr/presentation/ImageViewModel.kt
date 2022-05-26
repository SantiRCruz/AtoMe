package com.example.medr.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.medr.core.Result
import com.example.medr.data.local.ImageDao
import com.example.medr.data.models.ImageEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class ImageViewModel(private val dao : ImageDao):ViewModel() {

    fun fetchImages():StateFlow<Result<List<ImageEntity>>> = flow {
        kotlin.runCatching {
            dao.getImages()
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
    fun saveImage(imageEntity: ImageEntity):StateFlow<Result<Long>> = flow {
        kotlin.runCatching {
            dao.insertImage(imageEntity)
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
    fun deleteImage(imageEntity: ImageEntity):StateFlow<Result<Int>> = flow {
        kotlin.runCatching {
            dao.deleteImage(imageEntity)
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
class ImageViewModelFactory(private val dao : ImageDao):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ImageDao::class.java).newInstance(dao)
    }
}