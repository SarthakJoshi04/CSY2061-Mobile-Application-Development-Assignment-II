//HomeScreen.kt
package com.example.a4room.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.a4room.R
import com.example.a4room.database.entity.Post
import com.example.a4room.viewmodel.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, postViewModel: PostViewModel, sessionUserEmail: String, sessionUserName: String) {

    val allPosts by postViewModel.allPost.observeAsState(emptyList())

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
                            text = sessionUserName,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .clickable {
                                    navController.navigate("profile/$sessionUserEmail/$sessionUserName")
                                }
                        )
                        IconButton(onClick = {
                            navController.navigate("login") {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                            }
                        }) {
                            Icon(painter = painterResource(id = R.drawable.logout), contentDescription = "Logout")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_post/$sessionUserEmail/$sessionUserName")},
                modifier = Modifier.padding(10.dp)
            ) {
                Icon(Icons.Filled.AddCircle, contentDescription = "Add Post")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (allPosts.isEmpty()) {
                Text(
                    text = "Inaugurate by creating a post",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleLarge
                )
            } else {
                LazyColumn {
                    items(allPosts) { post ->
                        PostItem(navController, sessionUserEmail, sessionUserName, post = post, postViewModel = postViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun PostItem(navController: NavHostController, sessionUserEmail: String, sessionUserName: String, post: Post, postViewModel: PostViewModel) {

    var likeCount by remember { mutableIntStateOf(post.likeCount) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
            .clickable { navController.navigate("post/$sessionUserEmail/$sessionUserName/${post.postId}") }
    ) {
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
            Text(
                text = "$likeCount Likes",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        // Increment the like by 1
                        likeCount++

                        // Update the like count in the database
                        val updatedPost = post.copy(likeCount = likeCount)
                        postViewModel.updatePost(updatedPost)
                  },
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text("Like")
                }
                Button(
                    onClick = { navController.navigate("add_comment/$sessionUserEmail/$sessionUserName/${post.postId}") },
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text("Add Comment")
                }
            }
        }
    }
}