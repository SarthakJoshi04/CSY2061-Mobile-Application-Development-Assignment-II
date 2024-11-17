// PostScreen.kt
package com.example.a4room.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.a4room.database.entity.Comment
import com.example.a4room.database.entity.Post
import com.example.a4room.viewmodel.CommentViewModel
import com.example.a4room.viewmodel.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(navController: NavHostController, postViewModel: PostViewModel, commentViewModel: CommentViewModel, sessionUserEmail: String, sessionUserName: String, postId: Int){

    // Fetch the selected post
    val allPosts by postViewModel.allPost.observeAsState(emptyList())
    // Filter posts to only show the selected post
    val selectedPost = allPosts.filter { it.postId == postId }

    // Fetch comments for the selected post
    val allComments by commentViewModel.allComments.observeAsState(emptyList())
    val postedComments = allComments.filter { it.postId == postId }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Posts",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        )
                        IconButton(onClick = {
                            navController.navigate("home/$sessionUserEmail/$sessionUserName")
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (allPosts.isEmpty()) {
                Text(
                    text = "Post not found",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleLarge
                )
            } else {
                LazyColumn {
                    items(selectedPost) { post ->
                        SelectedPostItem(post)

                        Text(
                            text = "Comments",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 15.dp, top = 20.dp)
                        )
                        // Show no comments if there aren't any comments made on the post
                        if (postedComments.isEmpty()) {
                            Text(
                                text = "No comments available.",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                            )
                        } else {
                            // Display comments if available
                            postedComments.forEach { comment ->
                                CommentItem(comment = comment)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SelectedPostItem(post: Post){
    Column {
        Text(
            text = "${post.userName} posted about ${post.subject}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = post.content,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
fun CommentItem(comment: Comment) {
    Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp)) {
        Text(
            text = "${comment.userName} commented",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = comment.comment,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}