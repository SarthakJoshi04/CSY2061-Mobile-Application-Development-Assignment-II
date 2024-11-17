// ProfileScreen.kt
package com.example.a4room.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.a4room.database.entity.Post
import com.example.a4room.viewmodel.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, postViewModel: PostViewModel, sessionUserEmail: String, sessionUserName: String){

    // Fetch posts by the current user
    val allPosts by postViewModel.allPost.observeAsState(emptyList())
    // Filter posts to only show the current user's posts
    val userPosts = allPosts.filter { it.userName == sessionUserName }

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
                            text = "$sessionUserName's Profile",
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
                    text = "Post in 4Room",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleLarge
                )
            } else {
                LazyColumn {
                    items(userPosts) { post ->
                        UserPostItem(post = post, onEdit = { }, onDelete = {
                            postViewModel.deletePost(post)
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun UserPostItem(post: Post, onEdit: () -> Unit, onDelete: () -> Unit) {

    var showMenu by remember { mutableStateOf(false) }

     Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
         Row(
             verticalAlignment = Alignment.CenterVertically,
             modifier = Modifier.fillMaxWidth()
         ) {
             Column(
                 modifier = Modifier.weight(1f)
             ) {
                 Text(
                     text = "${post.userName} posted about ${post.subject}",
                     style = MaterialTheme.typography.bodyLarge,
                     fontWeight = FontWeight.Bold,
                     fontStyle = FontStyle.Italic
                 )
                 Text(
                     text = post.content,
                     style = MaterialTheme.typography.bodyMedium,
                     modifier = Modifier.padding(vertical = 8.dp)
                 )
             }
             IconButton(onClick = { showMenu = true }) {
                 Icon(Icons.Default.MoreVert, contentDescription = "Options")
             }
         }

         DropdownMenu(
             expanded = showMenu,
             onDismissRequest = { showMenu = false }
         ) {
             DropdownMenuItem(
                 text = { Text("Edit") },
                 onClick = {
                     showMenu = false
                     onEdit()
                 }
             )
             DropdownMenuItem(
                 text = { Text("Delete") },
                 onClick = {
                     showMenu = false
                     onDelete()
                 }
             )
         }
    }
}