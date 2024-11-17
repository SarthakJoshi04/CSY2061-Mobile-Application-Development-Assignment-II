//User.kt
package com.example.a4room.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val userEmail: String,
    val fullName: String,
    val dob: String,
    val password: String
)