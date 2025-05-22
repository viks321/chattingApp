package com.example.onetoone.homeScreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.onetoone.R
import com.example.onetoone.lodingScreen.lodingScreen
import com.example.onetoone.models.LoginModel
import com.example.onetoone.repositary.Response
import com.example.onetoone.ui.theme.Hintgray
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun previewScreen(){

    homeScreen(rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun homeScreen(navController: NavController){

    val homeViewmodel : HomeViewmodel = hiltViewModel()
    val getAllMembers : State<List<LoginModel>> = homeViewmodel.allMemberLiveData.collectAsState()
    homeViewmodel.getAllMembers()

    Scaffold() {

        Box(modifier = Modifier.padding(it)){

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (getAllMembers.value.isEmpty()) {
                    //Loding Screen
                    lodingScreen()
                }

                Box {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.menu_icon),
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(alignment = Alignment.CenterVertically),
                                contentDescription = "menu"
                            )
                            Text(
                                text = "oneVone",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.align(alignment = Alignment.CenterVertically)
                            )
                            Image(painter = painterResource(id = R.drawable.user_icon),
                                contentDescription = "user",
                                modifier = Modifier
                                    .size(60.dp)
                                    .align(alignment = Alignment.CenterVertically)
                                    .clickable {
                                        navController.navigate("logoutScreen"){
                                            popUpTo("homeScreen") {
                                                inclusive = true // This removes the login screen from the back stack
                                            }
                                        }
                                    }
                            )
                        }

                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Hintgray)
                            .height(1.dp))

                    }
                }

                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)) {

                    LazyColumn() {
                            items(getAllMembers.value){

                                Card(
                                    onClick = {

                                        val loginData = LoginModel(it.userID, it.email,it.userName,it.password,it.phoneNo)
                                            navController.currentBackStackEntry
                                                ?.savedStateHandle
                                                ?.set("loginData", loginData)
                                        navController.navigate("chatRoomScreen")
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    elevation = CardDefaults.cardElevation(5.dp)
                                ) {

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                    ) {
                                        Image(painter = painterResource(id = R.drawable.user_icon), contentDescription = "user", modifier = Modifier
                                            .size(70.dp)
                                            .weight(3f)
                                            .align(alignment = Alignment.CenterVertically))
                                        Text(text = it.userName!!, fontSize = 20.sp, color = Color.Black, modifier = Modifier
                                            .align(alignment = Alignment.CenterVertically)
                                            .weight(7f))
                                    }

                                }
                            }
                    }

                }

            }
        }

    }

}