//Comment.kt
package com.example.a4room.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comment")
data class Comment(
    @PrimaryKey(autoGenerate = true) val commentId: Int = 0,
    val userName: String,
    val postId: Int,
    val comment: String
)