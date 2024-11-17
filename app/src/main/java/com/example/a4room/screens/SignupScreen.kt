//SignupScreen.kt
package com.example.a4room.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.a4room.R
import com.example.a4room.checkEmailFormat
import com.example.a4room.database.entity.User
import com.example.a4room.isIdenticalPassword
import com.example.a4room.toProperCase
import com.example.a4room.viewmodel.UserViewModel
import java.util.Calendar

@Composable
fun SignupScreen(navController: NavHostController, userViewModel: UserViewModel){
    var fullName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible  by remember { mutableStateOf(false) }
    var isEmailFormatValid by remember { mutableStateOf(true) }
    var isPasswordIdentical by remember { mutableStateOf(true) }

    val isFormValid = fullName.isNotBlank() && dateOfBirth.isNotBlank() && userEmail.isNotBlank() &&
            userPassword.isNotBlank() && confirmPassword.isNotBlank() && isEmailFormatValid && isPasswordIdentical

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    // Collect insertion status from the ViewModel
    val insertionStatus by userViewModel.insertionStatus.collectAsStateWithLifecycle()

    // Show Toast message when insertion status is updated
    LaunchedEffect(insertionStatus) {
        when (insertionStatus) {
            is UserViewModel.Result.Success -> {
                Toast.makeText(context, "The user has been added. Login to continue.", Toast.LENGTH_SHORT).show()
                // Reset the form
                fullName = ""
                dateOfBirth = ""
                userEmail = ""
                userPassword = ""
                confirmPassword = ""
            }
            is UserViewModel.Result.Failure -> {
                Toast.makeText(context, "Registration failed. Try again", Toast.LENGTH_SHORT).show()
            }
            null -> Unit
        }
    }

    fun openDatePickerDialog(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(context, {_, selectedYear, selectedMonth, selectedDay ->
            dateOfBirth = "$selectedDay/${selectedMonth+1}/$selectedYear"
        },year, month, day).show()
    }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .statusBarsPadding()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "Image",
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(text = "Signup", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = fullName,
            onValueChange = {newValue -> fullName = toProperCase(newValue) },
            label = { Text(text = "Full Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = dateOfBirth,
            onValueChange = { dateOfBirth = it },
            label = { Text(text = "Date of Birth") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { openDatePickerDialog() },
            singleLine = true,
            trailingIcon = {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Calendar icon")
            }
        )
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
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                isPasswordIdentical = isIdenticalPassword(userPassword, it)
            },
            label = {Text(text = "Confirm Password")},
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
        if(!isPasswordIdentical){
            Text(
                text = "Please ensure the passwords are identical.",
                color = Color(0xFF800000),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 5.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                val newUser = User(
                    userEmail = userEmail,
                    fullName = fullName,
                    dob = dateOfBirth,
                    password = userPassword
                )
                userViewModel.insertUser(newUser)
            },
            modifier = Modifier
                .fillMaxWidth(),
            enabled = isFormValid
        ) {
            Text(text = "Sign Up")
        }
        Spacer(modifier = Modifier.height(20.dp))
        TextButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { navController.navigate("login") }) {
            Text(
                text = "Have an account? Login to continue.",
                color = Color(0xFF6F717A)
            )
        }
    }
}