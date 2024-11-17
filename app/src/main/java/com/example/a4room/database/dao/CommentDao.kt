//CommentDao.kt
package com.example.a4room.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.a4room.database.entity.Comment

@Dao
interface CommentDao {
    @Insert
    fun insertComment(comment: Comment)

    @Query("SELECT * FROM comment")
    fun getAllComments() : LiveData<List<Comment>>
}