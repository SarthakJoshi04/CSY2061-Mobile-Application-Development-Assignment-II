//Post.kt
package com.example.a4room.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post")
data class Post(
    @PrimaryKey(autoGenerate = true) val postId: Int = 0,
    val userName: String,
    val subject: String,
    val content: String,
    val likeCount: Int
)