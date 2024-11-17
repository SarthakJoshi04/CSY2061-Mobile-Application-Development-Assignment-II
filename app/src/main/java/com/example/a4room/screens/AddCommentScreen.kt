// AddCommentScreen.kt
package com.example.a4room.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.a4room.database.entity.Comment
import com.example.a4room.viewmodel.CommentViewModel

@Composable
fun AddCommentScreen(navController: NavController, commentViewModel: CommentViewModel, sessionUserEmail:String,sessionUserName: String, postId: Int){

    var commentContent by remember { mutableStateOf("") }

    val isFormValid = commentContent.isNotBlank()

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    // Collect insertion status from the ViewModel
    val insertionStatus by commentViewModel.insertionStatus.collectAsStateWithLifecycle()

    // Show Toast message when insertion status is updated
    LaunchedEffect(insertionStatus) {
        when (insertionStatus) {
            is CommentViewModel.Result.Success -> {
                Toast.makeText(context, "The comment has been added", Toast.LENGTH_SHORT).show()
                // Reset the form
                commentContent = ""
            }
            is CommentViewModel.Result.Failure -> {
                Toast.makeText(context, "An error occurred.", Toast.LENGTH_SHORT).show()
            }
            null -> Unit
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .statusBarsPadding()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Add Comment", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = commentContent,
            onValueChange = { commentContent = it },
            label = { Text(text = "What's your thoughts?") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
               val newComment = Comment(
                   userName = sessionUserName,
                   postId = postId,
                   comment = commentContent
               )
                commentViewModel.insertComment(newComment)
            },
            modifier = Modifier
                .fillMaxWidth(),
            enabled = isFormValid
        ) {
            Text(text = "Add Comment")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { navController.navigate("home/$sessionUserEmail/$sessionUserName") },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Back")
        }
    }
}