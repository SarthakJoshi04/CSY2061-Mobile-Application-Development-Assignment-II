//PostRepo.kt
package com.example.a4room.database.repo

import androidx.lifecycle.LiveData
import com.example.a4room.database.dao.PostDao
import com.example.a4room.database.entity.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostRepo(private val postDao: PostDao) {
    suspend fun insertPost(post: Post){
        withContext(Dispatchers.IO){
            postDao.insertPost(post)
        }
    }
    suspend fun updatePost(post: Post){
        withContext(Dispatchers.IO){
            postDao.updatePost(post)
        }
    }
    suspend fun deletePost(post: Post){
        withContext(Dispatchers.IO){
            postDao.deletePost(post)
        }
    }
    fun getAllPosts(): LiveData<List<Post>> {
        return postDao.getAllPosts()
    }
}