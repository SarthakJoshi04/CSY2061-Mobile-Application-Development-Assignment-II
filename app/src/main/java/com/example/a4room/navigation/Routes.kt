//Routes.kt
package com.example.a4room.navigation

sealed class Routes(val routes: String){
    object Login: Routes("login") // Login
    object Signup: Routes("signup") // Signup
    object Admin: Routes ("admin") //Admin
    object AddStudent: Routes("add_student") //Add Student
    object Home: Routes("home/{userEmail}/{fullName}") // Home
    object AddPost: Routes("add_post/{userEmail}/{fullName}") // Add Post
    object Profile: Routes("profile/{userEmail}/{fullName}") // Profile
    object Post: Routes("post/{userEmail}/{fullName}/{postId}") // View Post
    object AddComment: Routes("add_comment/{userEmail}/{fullName}/{postId}")
}
