//CommentRepo.kt
package com.example.a4room.database.repo

import androidx.lifecycle.LiveData
import com.example.a4room.database.dao.CommentDao
import com.example.a4room.database.entity.Comment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CommentRepo(private val commentDao: CommentDao) {
    suspend fun insertComment(comment: Comment){
        withContext(Dispatchers.IO){
            commentDao.insertComment(comment)
        }
    }
    fun getAllComments(): LiveData<List<Comment>> {
        return commentDao.getAllComments()
    }
}