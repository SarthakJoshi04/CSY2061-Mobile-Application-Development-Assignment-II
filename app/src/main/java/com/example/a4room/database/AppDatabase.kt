//AppDatabase.kt
package com.example.a4room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.a4room.database.dao.CommentDao
import com.example.a4room.database.dao.PostDao
import com.example.a4room.database.dao.UserDao
import com.example.a4room.database.entity.Comment
import com.example.a4room.database.entity.Post
import com.example.a4room.database.entity.User

@Database(entities = [User::class, Post::class, Comment::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao

    companion object {
        // Volatile instance to ensure changes to this property are visible to all threads
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Function to get the instance of the database
        fun getDatabase(context: android.content.Context): AppDatabase {
            // If the instance is already initialized, return it
            return INSTANCE ?: synchronized(this) {
                // Otherwise, build the database
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "4RoomDB"
                ).build()
                // Assign it to INSTANCE and return
                INSTANCE = instance
                instance
            }
        }
    }
}