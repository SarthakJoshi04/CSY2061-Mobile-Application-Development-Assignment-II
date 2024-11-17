//PostViewModel.kt
package com.example.a4room.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a4room.database.entity.Post
import com.example.a4room.database.repo.PostRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel(private val postRepo: PostRepo): ViewModel() {
    // Expose LiveData from the repository so the UI can observe it
    val allPost: LiveData<List<Post>> = postRepo.getAllPosts()

    // StateFlow to track the status of user insertion
    private val _insertionStatus = MutableStateFlow<Result?>(null)
    val insertionStatus: StateFlow<Result?> = _insertionStatus

    fun insertPost(post: Post){
        viewModelScope.launch {
            try {
                postRepo.insertPost(post)
                _insertionStatus.value = Result.Success
            } catch (e: Exception) {
                _insertionStatus.value = Result.Failure(e)
            }
        }
    }
    fun updatePost(post: Post){
        viewModelScope.launch {
            postRepo.updatePost(post)
        }
    }
    fun deletePost(post: Post){
        viewModelScope.launch {
            postRepo.deletePost(post)
        }
    }

    sealed class Result {
        object Success : Result()
        data class Failure(val exception: Exception) : Result()
    }
}