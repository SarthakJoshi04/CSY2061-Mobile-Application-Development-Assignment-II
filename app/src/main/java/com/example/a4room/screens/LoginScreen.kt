//LoginScreen.kt
package com.example.a4room.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.a4room.R
import com.example.a4room.checkEmailFormat
import com.example.a4room.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController, userViewModel: UserViewModel){
    var userEmail by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }
    var isPasswordVisible  by remember { mutableStateOf(false) }
    var isEmailFormatValid by remember { mutableStateOf(true) }

    // Checks if the form has been filled properly
    val isFormValid = userEmail.isNotBlank() && userPassword.isNotBlank() && isEmailFormatValid
    // Checks if the user is an admin
    val isAdmin = userEmail == "admin@app.com" && userPassword == "LETMEIN"

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column {
        Image(painter = painterResource(id = R.drawable.img),
            contentDescription = "Image",
            modifier = Modifier.size(300.dp).align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(text = "Login", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = userEmail,
            onValueChange = {
                userEmail = it
                isEmailFormatValid = checkEmailFormat(it)
            },
            label = {Text(text = "Email")},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        if(!isEmailFormatValid){
            Text(
                text = "Please include an '@' in the email address.",
                color = Color(0xFF800000),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 5.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = userPassword,
            onValueChange = {
                userPassword = it
            },
            label = {Text(text = "Password")},
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon ={
                val image =  if(isPasswordVisible) R.drawable.hide else R.drawable.visibile
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(painter = painterResource(id = image), contentDescription = if (isPasswordVisible) "Hide password" else "Show password")
                }
            },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                if(isAdmin){
                    navController.navigate("admin")
                } else {
                    coroutineScope.launch {
                        userViewModel.getUserByEmail(userEmail){ user ->
                            if(user == null){
                                Toast.makeText(context, "The user doesn't exist.", Toast.LENGTH_SHORT).show()
                            } else if (user.password == userPassword){
                                navController.navigate("home/${userEmail}/${user.fullName}")
                            } else {
                                Toast.makeText(context, "Incorrect password. Try again.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            },
            enabled = isFormValid
        ) {
            Text(text = "Login")
        }
        Spacer(modifier = Modifier.height(20.dp))
        TextButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { navController.navigate("signup") }) {
            Text(
                text = "Don't have an account? Signup here.",
                color = Color(0xFF6F717A)
            )
        }
    }
}