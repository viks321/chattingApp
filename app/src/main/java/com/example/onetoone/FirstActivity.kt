package com.example.onetoone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.onetoone.chatRoomScreen.chatRoomScreen
import com.example.onetoone.homeScreen.homeScreen
import com.example.onetoone.models.LoginModel
import com.example.onetoone.loginScreen.LoginViewmodel
import com.example.onetoone.loginScreen.loginScreen
import com.example.onetoone.registrationScreen.RegistrationViewmodel
import com.example.onetoone.registrationScreen.registrationScreen
import com.example.onetoone.ui.theme.OneToOneTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
class FirstActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OneToOneTheme {
                // Your Compose UI starts here
               Surface(modifier = Modifier.fillMaxSize()) {
                   App()
               }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "loginScreen") {

        //Login screen
        composable(route = "loginScreen") {

            loginScreen(onClick = { loginData ->
                navController.currentBackStackEntry?.savedStateHandle?.set("loginData", loginData)
                navController.navigate("registrationScreen")
            },navController)
        }

        //Registration screen
        composable(route = "registrationScreen"){
           // val loginData = it.arguments!!.getParcelable<LoginModel>("loginData")
            val loginData = navController.previousBackStackEntry?.savedStateHandle?.get<LoginModel>("loginData")
            registrationScreen(navController,loginData)
        }

        //Home screen
        composable(route = "homeScreen"){
            homeScreen(navController)
        }

        //Chat room screen
        composable(route = "chatRoomScreen"){
            //val userData = navController.previousBackStackEntry?.savedStateHandle?.get<LoginModel>("loginData")
            chatRoomScreen(navController)
        }
    }
}