//AddPostScreen.kt
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
import androidx.navigation.NavHostController
import com.example.a4room.database.entity.Post
import com.example.a4room.toProperCase
import com.example.a4room.viewmodel.PostViewModel

@Composable
fun AddPostScreen(navController: NavHostController, postViewModel: PostViewModel, sessionUserEmail: String,sessionUserName: String){

    var postSubject by remember { mutableStateOf("") }
    var postContent by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val isFormValid = postContent.isNotBlank() && postSubject.isNotBlank()

    // Collect insertion status from the ViewModel
    val insertionStatus by postViewModel.insertionStatus.collectAsStateWithLifecycle()

    // Show Toast message when insertion status is updated
    LaunchedEffect(insertionStatus) {
        when (insertionStatus) {
            is PostViewModel.Result.Success -> {
                Toast.makeText(context, "The post has been added.", Toast.LENGTH_SHORT).show()
                // Reset the form
                postSubject = ""
                postContent = ""
            }
            is PostViewModel.Result.Failure -> {
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
    ){
        Text(text = "Add Post", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = postSubject,
            onValueChange = {newValue -> postSubject = toProperCase(newValue) },
            label = { Text(text = "Subject") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = postContent,
            onValueChange = { postContent = it },
            label = { Text(text = "What is happening?!") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                val newPost = Post(
                    userName= sessionUserName,
                    subject = postSubject,
                    content = postContent,
                    likeCount = 0
                )
                postViewModel.insertPost(newPost)
            },
            modifier = Modifier
                .fillMaxWidth(),
            enabled = isFormValid
        ) {
            Text(text = "Post in 4Room")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { navController.navigate("home/$sessionUserEmail/$sessionUserName") },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Back")
        }
    }
}