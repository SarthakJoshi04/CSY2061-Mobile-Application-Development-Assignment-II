//UserDao.kt
package com.example.a4room.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.a4room.database.entity.User

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user:User)

    @Query("SELECT * FROM user WHERE userEmail = :userEmail LIMIT 1")
    fun getUserByEmail(userEmail: String): User?

    @Query("SELECT * FROM user")
    fun getAllUsers() : LiveData<List<User>>
}