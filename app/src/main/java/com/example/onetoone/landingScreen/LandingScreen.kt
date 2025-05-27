package com.example.onetoone.landingScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.onetoone.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun previewScreen(){

    landingScreen(rememberNavController())
}

@Composable
fun landingScreen(navController: NavController) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize().background(Color(0xFF2A2A95))
    ) {
        Image(painter = painterResource(id = R.drawable.onevone_logo), contentDescription = "splash")

        LaunchedEffect(Unit) {
            CoroutineScope(Dispatchers.Main).launch {
                delay(3000)
                navController.navigate("loginScreen")
            }
        }
    }

}
