//CommentViewModel.kt
package com.example.a4room.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a4room.database.entity.Comment
import com.example.a4room.database.repo.CommentRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommentViewModel(private val commentRepo: CommentRepo): ViewModel() {
    // Expose LiveData from the repository so the UI can observe it
    val allComments: LiveData<List<Comment>> = commentRepo.getAllComments()

    // StateFlow to track the status of user insertion
    private val _insertionStatus = MutableStateFlow<Result?>(null)
    val insertionStatus: StateFlow<Result?> = _insertionStatus

    fun insertComment(comment: Comment){
        viewModelScope.launch {
            try {
                commentRepo.insertComment(comment)
                _insertionStatus.value = Result.Success
            } catch (e: Exception) {
                _insertionStatus.value = Result.Failure(e)
            }
        }
    }

    sealed class Result {
        object Success : Result()
        data class Failure(val exception: Exception) : Result()
    }
}