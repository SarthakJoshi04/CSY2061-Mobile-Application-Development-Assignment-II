//UserViewModel.kt
package com.example.a4room.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a4room.database.entity.User
import com.example.a4room.database.repo.UserRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val userRepo: UserRepo): ViewModel() {

    // Expose LiveData from the repository so the UI can observe it
    val allUsers: LiveData<List<User>> = userRepo.getAllUsers()

    // StateFlow to track the status of user insertion
    private val _insertionStatus = MutableStateFlow<Result?>(null)
    val insertionStatus: StateFlow<Result?> = _insertionStatus

    fun insertUser(user: User){
        viewModelScope.launch {
            try {
                userRepo.insertUser(user)
                _insertionStatus.value = Result.Success
            } catch (e: Exception) {
                _insertionStatus.value = Result.Failure(e)
            }
        }
    }
    fun updateUser(user: User){
        viewModelScope.launch {
            userRepo.updateUser(user)
        }
    }
    fun deleteUser(user: User){
        viewModelScope.launch {
            userRepo.deleteUser(user)
        }
    }

    fun getUserByEmail(userEmail: String, callback: (User?) -> Unit) {
        viewModelScope.launch {
            val user = userRepo.getUserByEmail(userEmail)
            callback(user)
        }
    }

    sealed class Result {
        object Success : Result()
        data class Failure(val exception: Exception) : Result()
    }
}