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
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun preScreen(){
    //profileScreen(rememberNavController())
    ProfileScreen(rememberNavController())
}


@Composable
fun ProfileScreen(navController: NavController) {

    val profileViewmodel : ProfileViewmodel = hiltViewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Avatar Image
        Image(
            painter = painterResource(id = R.drawable.user_demo), // Replace with actual image
            contentDescription = "Profile Avatar",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFB48BFF))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Name
        Text(
            text = "Vikas Assudani",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF9333EA)
        )

        // Job Title
        Text(
            text = "Android Developer",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Info Fields
        ProfileField(label = "Your Email", value = "vikasassudani909@gmail.com", icon = Icons.Default.Email)
        ProfileField(label = "Phone Number", value = "+91 9876543210", icon = Icons.Default.Phone)
        ProfileField(label = "Website", value = "www.vikasassudani.com", icon = Icons.Default.Email)
        ProfileField(label = "Password", value = "************", icon = Icons.Default.Lock)

        Button(
            onClick = {
                profileViewmodel.logout()
                navController.navigate("loginScreen") {
                    popUpTo(0) { inclusive = true } // removes all backstack
                    launchSingleTop = true
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

@Composable
fun ProfileField(label: String, value: String, icon: ImageVector) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF4F4F4), shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = value,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

