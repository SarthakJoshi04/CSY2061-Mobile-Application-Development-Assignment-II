package com.example.a4room

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.a4room.navigation.NavGraph
import com.example.a4room.ui.theme._4RoomTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _4RoomTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }
}

// Check if the email format is correct
fun checkEmailFormat(email: String): Boolean {
    return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

// Compare the passwords to check if its the same
fun isIdenticalPassword(password: String, confirmPassword: String): Boolean{
    return password.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword
}

// Return a new string with the first character of each word in uppercase.
fun toProperCase(input: String): String {
    return input.split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
}