package com.example.onetoone.registrationScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.onetoone.models.LoginModel

@Composable
fun registrationScreen(navController: NavController, loginModel: LoginModel?){
    Box(
        modifier = Modifier.fillMaxSize()
            .clickable { navController.navigate("loginScreen") },
        contentAlignment = Alignment.Center,
    ){
        Text(text = "Email :"+loginModel?.email+"\nPassword :"+loginModel?.password)
    }
}