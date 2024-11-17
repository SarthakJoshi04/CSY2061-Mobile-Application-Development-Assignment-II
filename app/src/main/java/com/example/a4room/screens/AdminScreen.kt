//AdminScreen.kt
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.a4room.R
import com.example.a4room.database.entity.User
import com.example.a4room.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(navController: NavHostController, userViewModel: UserViewModel) {

    val allUsers by userViewModel.allUsers.observeAsState(emptyList())

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
                            text = "Admin Panel",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.align(Alignment.CenterVertically)
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
                onClick = { navController.navigate("add_student") },
                modifier = Modifier.padding(10.dp)
            ) {
                Icon(Icons.Filled.AddCircle, contentDescription = "Add Student")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (allUsers.isEmpty()) {
                Text(
                    text = "Add users",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(allUsers) { user ->
                        UserItem(user = user, onEdit = { }, onDelete = {
                            userViewModel.deleteUser(user)
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(user: User, onEdit: () -> Unit, onDelete: () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.fullName,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = user.userEmail,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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