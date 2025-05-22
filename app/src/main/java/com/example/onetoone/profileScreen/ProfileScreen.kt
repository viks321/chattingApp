package com.example.onetoone.profileScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.onetoone.R
import com.example.onetoone.ui.theme.Cardbacground
import com.example.onetoone.ui.theme.Hintgray
import com.example.onetoone.ui.theme.LodingColor
import com.example.onetoone.ui.theme.Yellow


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun preScreen(){
    profileScreen(rememberNavController())
}

@Composable
fun profileScreen(navController: NavController) {

    val profileViewmodel : ProfileViewmodel = hiltViewModel()

    Box {
        Column {
            Box(
                Modifier
                    .weight(5f)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center)
            {
                Image(painter = painterResource(id = R.drawable.user_icon), contentDescription = "coverImage", modifier = Modifier.fillMaxSize())
                Box(
                    Modifier
                        .background(color = LodingColor)
                        .fillMaxSize()) {

                }
            }
            Box(
                Modifier
                    .background(color = Cardbacground)
                    .weight(5f)
                    .padding(20.dp)
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {

                Button(
                    onClick = {
                        profileViewmodel.logout()
                        navController.navigate("loginScreen"){
                            popUpTo("profileScreen") {
                                inclusive = true // This removes the login screen from the back stack
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(50.dp),
                    enabled = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Yellow)
                ) {
                    Text(text = "Logout")
                }

            }
        }
    }
}
