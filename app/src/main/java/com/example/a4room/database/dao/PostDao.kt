//PostDao.kt
package com.example.a4room.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.a4room.database.entity.Post

@Dao
interface PostDao {
    @Insert
    fun insertPost(post: Post)

    @Update
    fun updatePost(post: Post)

    @Delete
    fun deletePost(post: Post)

    @Query("SELECT * FROM post")
    fun getAllPosts() : LiveData<List<Post>>
}