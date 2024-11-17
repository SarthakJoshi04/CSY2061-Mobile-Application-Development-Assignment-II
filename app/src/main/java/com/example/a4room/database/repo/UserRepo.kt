//UserRepo.kt
package com.example.a4room.database.repo

import androidx.lifecycle.LiveData
import com.example.a4room.database.dao.UserDao
import com.example.a4room.database.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepo(private val userDao: UserDao) {
    suspend fun insertUser(user: User){
        withContext(Dispatchers.IO){
            userDao.insertUser(user)
        }
    }
    suspend fun updateUser(user: User){
        withContext(Dispatchers.IO){
            userDao.updateUser(user)
        }
    }
    suspend fun deleteUser(user: User){
        withContext(Dispatchers.IO){
            userDao.deleteUser(user)
        }
    }

    suspend fun getUserByEmail(userEmail: String): User? {
        return withContext(Dispatchers.IO) {
            userDao.getUserByEmail(userEmail)
        }
    }

    fun getAllUsers(): LiveData<List<User>> {
        return userDao.getAllUsers()
    }
}