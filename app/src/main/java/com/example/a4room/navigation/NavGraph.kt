//NavGraph.kt
package com.example.a4room.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.a4room.database.AppDatabase
import com.example.a4room.database.repo.CommentRepo
import com.example.a4room.database.repo.PostRepo
import com.example.a4room.database.repo.UserRepo
import com.example.a4room.screens.AddCommentScreen
import com.example.a4room.screens.AddPostScreen
import com.example.a4room.screens.AddStudentScreen
import com.example.a4room.screens.AdminScreen
import com.example.a4room.screens.HomeScreen
import com.example.a4room.screens.LoginScreen
import com.example.a4room.screens.PostScreen
import com.example.a4room.screens.ProfileScreen
import com.example.a4room.screens.SignupScreen
import com.example.a4room.viewmodel.CommentViewModel
import com.example.a4room.viewmodel.PostViewModel
import com.example.a4room.viewmodel.UserViewModel

@Composable
fun NavGraph(navController: NavHostController){
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val userRepo = UserRepo(database.userDao())
    val postRepo = PostRepo(database.postDao())
    val commentRepo = CommentRepo(database.commentDao())

    NavHost(navController = navController, startDestination = Routes.Login.routes) {
        // Login Screen
        composable(Routes.Login.routes) {
            val userViewModel = UserViewModel(userRepo)
            LoginScreen(navController, userViewModel = userViewModel)
        }
        // Signup Screen
        composable(Routes.Signup.routes){
            val userViewModel = UserViewModel(userRepo)
            SignupScreen(navController = navController, userViewModel = userViewModel)
        }
        // Admin Panel
        composable(Routes.Admin.routes){
            val userViewModel = UserViewModel(userRepo)
            AdminScreen(navController, userViewModel = userViewModel)
        }
        // Add Student
        composable(Routes.AddStudent.routes){
            val userViewModel = UserViewModel(userRepo)
            AddStudentScreen(navController, userViewModel)
        }
        // Home Screen
        composable(Routes.Home.routes,
            arguments = listOf(
                navArgument("userEmail"){ type = NavType.StringType },
                navArgument("fullName") { type = NavType.StringType }
            )
        ){
                backStackEntry ->
            val postViewModel = PostViewModel(postRepo)
            val sessionUserEmail = backStackEntry.arguments?.getString("userEmail") ?: ""
            val sessionUserName = backStackEntry.arguments?.getString("fullName") ?: ""
            HomeScreen(navController = navController, postViewModel,sessionUserEmail, sessionUserName)
        }
        // Add Post
        composable(Routes.AddPost.routes,
            arguments = listOf(
                navArgument("userEmail"){ type = NavType.StringType },
                navArgument("fullName") { type = NavType.StringType }
            )
        ){
                backStackEntry ->
                val userEmail = backStackEntry.arguments?.getString("userEmail") ?: ""
                val userName = backStackEntry.arguments?.getString("fullName") ?: ""
                val postViewModel = PostViewModel(postRepo)
                AddPostScreen(navController, postViewModel, userEmail, userName)
        }
        // Profile Screen
        composable(Routes.Profile.routes,
            arguments = listOf(
                navArgument("userEmail"){ type = NavType.StringType },
                navArgument("fullName") { type = NavType.StringType }
            )
        ){
            backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("userEmail") ?: ""
            val userName = backStackEntry.arguments?.getString("fullName") ?: ""
            val postViewModel = PostViewModel(postRepo)
            ProfileScreen(navController, postViewModel, userEmail, userName)
        }
        // Post Screen
        composable(Routes.Post.routes,
            arguments = listOf(
                navArgument("userEmail"){ type = NavType.StringType },
                navArgument("fullName") { type = NavType.StringType },
                navArgument("postId") { type = NavType.IntType }
            )
        ){
            backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("userEmail") ?: ""
            val userName = backStackEntry.arguments?.getString("fullName") ?: ""
            val postId = backStackEntry.arguments?.getInt("postId") ?: 0
            val postViewModel = PostViewModel(postRepo)
            val commentViewModel = CommentViewModel(commentRepo)
            PostScreen(navController, postViewModel, commentViewModel, userEmail, userName, postId)
        }
        // Add Comment
        composable(Routes.AddComment.routes,
            arguments = listOf(
                navArgument("userEmail"){ type = NavType.StringType },
                navArgument("fullName") { type = NavType.StringType },
                navArgument("postId") { type = NavType.IntType }
            )
        ){
            backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("userEmail") ?: ""
            val userName = backStackEntry.arguments?.getString("fullName") ?: ""
            val postId = backStackEntry.arguments?.getInt("postId") ?: 0
            val commentViewModel = CommentViewModel(commentRepo)
            AddCommentScreen(navController, commentViewModel, userEmail, userName, postId)
        }
    }
}