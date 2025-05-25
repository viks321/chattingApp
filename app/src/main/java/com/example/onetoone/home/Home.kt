package com.example.onetoone.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.onetoone.R
import com.example.onetoone.homeScreen.HomeViewmodel
import com.example.onetoone.models.Messages
import com.example.onetoone.models.UserData
import com.example.onetoone.ui.theme.Hintgray

@Composable
fun Home(navController: NavController){

    val homeViewmodel : HomeViewmodel = hiltViewModel()
    val getAllMembers : State<List<UserData>> = homeViewmodel.allMemberLiveData.collectAsState()
    val currentUserID : State<String> = homeViewmodel.currentUserID.collectAsState()
    val currentUserName : State<String> = homeViewmodel.currentUserName.collectAsState()
    val chatRoomLiveData : State<List<Messages>?> = homeViewmodel.chatRoomLiveData.collectAsState()

    //Toast.makeText(navController.context,currentUserName.value,Toast.LENGTH_SHORT).show()

    LaunchedEffect(Unit) {
        homeViewmodel.getAllMembers(currentUserID.value)
    }

    Box(){

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {



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
                            fontFamily = FontFamily(Font(R.font.nunito_bold)),
                            fontSize = 16.sp,
                            modifier = Modifier.align(alignment = Alignment.CenterVertically)
                        )
                        Image(painter = painterResource(id = R.drawable.cu_user_demo),
                            contentDescription = "user",
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color.White, CircleShape)
                                .align(alignment = Alignment.CenterVertically)
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


                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF9F9F9))
                        .padding(vertical = 8.dp)
                ) {
                    items(getAllMembers.value) { chat ->
                        ChatListItem(chat,navController,homeViewmodel,currentUserID,currentUserName)
                    }
                }

            }

            /*LazyColumn() {
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

        }*/

        }
    }

}

@Composable
fun ChatListItem(
    chat: UserData,
    navController: NavController,
    homeViewmodel: HomeViewmodel,
    currentUserID: State<String>,
    currentUserName: State<String>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                homeViewmodel.createChatRoom(chat.userID.toString(),currentUserID.value,currentUserName.value,chat.userName.toString())
                /*val loginData =
                    LoginModel(chat.userID, chat.userName, chat.email, chat.password, chat.phoneNo)
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("loginData", loginData)
                navController.navigate("chatRoomScreen")*/
            }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.user_demo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .background(Color.White, CircleShape)
                .padding(2.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = chat.userName.toString(),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    color = Color.Black
                )
            }

            Text(
                text = "No message here...",
                fontSize = 14.sp,
                //color = if (chat.isTyping) Color(0xFF673AB7) else Color.Gray,
                maxLines = 1,
                fontFamily = FontFamily(Font(R.font.nunito_light)),
                overflow = TextOverflow.Ellipsis
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "00:00 AM",
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.nunito_light)),
                color = Color.Gray
            )

            /*if (chat.messageCount!! > 0) {
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .background(Color(0xFF673AB7), CircleShape)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = chat.messageCount.toString(),
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }*/
        }
    }
}