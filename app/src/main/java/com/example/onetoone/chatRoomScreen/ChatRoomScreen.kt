package com.example.onetoone.chatRoomScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.onetoone.R
import com.example.onetoone.models.ChatRoom
import com.example.onetoone.models.LoginModel
import com.example.onetoone.models.Message
import com.example.onetoone.models.Messages
import com.example.onetoone.ui.theme.Hintgray


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun previewScreen(){
    chatRoomScreen(rememberNavController())
}

@Composable
fun chatRoomScreen(navController: NavController) {

    val chatRoomViewmodel : ChatRoomViewmodel = hiltViewModel()
    val roomDataMessages : State<List<Messages>?> = chatRoomViewmodel.roomDataMessages.collectAsState()
    val senderID : State<String> = chatRoomViewmodel.senderID.collectAsState()
    val senderName : State<String> = chatRoomViewmodel.userName.collectAsState()

    Log.d("VikasData",roomDataMessages.value.toString())

    val loginData = navController
        .previousBackStackEntry
        ?.savedStateHandle
        ?.get<LoginModel>("loginData")

        //Toast.makeText(navController.context,loginData?.userID.toString(),Toast.LENGTH_LONG).show()
    LaunchedEffect(Unit) {
        chatRoomViewmodel.getMessageData(senderID.value,loginData?.userID.toString())
    }

    Scaffold {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize()){

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF5C4DFF))
                    .padding(16.dp)
            ) {
                /*Box {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Image(painter = painterResource(id = R.drawable.back_icon),
                                contentDescription = "user",
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(alignment = Alignment.CenterVertically)
                                    .clickable { navController.navigate("homeScreen") }
                            )
                            Text(
                                text = "oneVone",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .align(alignment = Alignment.CenterVertically)
                            )
                            Image(painter = painterResource(id = R.drawable.user_icon),
                                contentDescription = "user",
                                modifier = Modifier
                                    .size(60.dp)
                                    .align(alignment = Alignment.CenterVertically)
                            )
                        }

                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Hintgray)
                            .height(1.dp))
                    }
                }*/
                ChatHeader(navController,loginData)
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier.weight(1f)
                ) {

                    Log.d("FirebaseChat", "userID with: ${roomDataMessages.value.toString()}")

                    val messageList = mutableListOf<Message>()
                    for (value in roomDataMessages.value!!){
                        value.messages?.forEach { (msgId, msg) ->
                            messageList.add(msg)
                        }
                    }
                    val sortedMessages = messageList.sortedBy {
                        val time = it.senderMessage?.timestamp ?: it.receiverMessage?.timestamp ?: 0L
                        time as Long
                    }

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(sortedMessages){
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            )
                            {
                                if(it.senderMessage?.message!=null){
                                    senderView(it.senderMessage?.message.toString())
                                }
                                else if(it.receiverMessage?.message!=null)
                                {
                                    reciverView(it.receiverMessage?.message.toString())
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                ChatInputBar(roomDataMessages,loginData,chatRoomViewmodel,senderID.value,senderName)

                /*Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {

                    val state = remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = state.value,
                        onValueChange = {
                        state.value = it
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(text = "Type here.....", color = Hintgray)},
                        shape = RoundedCornerShape(10.dp),
                        maxLines = 3,
                        trailingIcon = { Icon(
                            painter = painterResource(id = R.drawable.send_icon),
                            contentDescription = "send",
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    chatRoomViewmodel.createChatRoom(
                                        ChatRoom(state.value),
                                        loginData?.userID!!,
                                        senderID.value
                                    )
                                    state.value = ""
                                }
                        )},
                    )
                }*/
            }

        }
    }

}

@Composable
fun senderView(s: String) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(12.dp)
                .widthIn(max = 260.dp)
        ) {
            Text(
                text = s,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.nunito_medium)),
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun reciverView(s: String) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = Color(0xFF443ea8),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(12.dp)
                .widthIn(max = 260.dp)
        ) {
            Text(
                text = s,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.nunito_medium)),
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun ChatHeader(navController: NavController, loginData: LoginModel?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(painter = painterResource(id = R.drawable.back_icon),
                contentDescription = "user",
                tint = Color.White,
                modifier = Modifier
                    .size(30.dp)
                    .align(alignment = Alignment.CenterVertically)
                    .clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Image(
                painter = painterResource(id = R.drawable.user_icon),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    loginData?.userName.toString(),
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    fontSize = 15.sp
                )
                Text("Online", color = Color(0xFFD0CFFF), fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_medium)))
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(onClick = { }) {
                Icon(painter = painterResource(id = android.R.drawable.ic_menu_camera), contentDescription = null, tint = Color.White)
            }
            IconButton(onClick = { }) {
                Icon(painter = painterResource(id = android.R.drawable.ic_menu_call), contentDescription = null, tint = Color.White)
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatMessages(roomDataMessages: State<List<Messages>?>) {

    Box(
        //modifier = Modifier.weight(1f)
    ) {

        Log.d("FirebaseChat", "userID with: ${roomDataMessages.value.toString()}")

        val messageList = mutableListOf<Message>()
        for (value in roomDataMessages.value!!){
            value.messages?.forEach { (msgId, msg) ->
                messageList.add(msg)
            }
        }
        val sortedMessages = messageList.sortedBy {
            val time = it.senderMessage?.timestamp ?: it.receiverMessage?.timestamp ?: 0L
            time as Long
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(sortedMessages){
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                )
                {
                    if(it.senderMessage?.message!=null){
                        senderView(it.senderMessage?.message.toString())
                    }
                    else if(it.receiverMessage?.message!=null)
                    {
                        reciverView(it.receiverMessage?.message.toString())
                    }
                }
            }
        }
    }
}

@Composable
fun ChatInputBar(
    roomDataMessages: State<List<Messages>?>,
    loginData: LoginModel?,
    chatRoomViewmodel: ChatRoomViewmodel,
    senderID: String,
    senderName: State<String>
) {
    val state = remember { mutableStateOf("") }

        Row(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(40.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = R.drawable.message_icon),
                contentDescription = null,
                tint = Color.Gray
            )
            TextField(
                value = state.value,
                onValueChange = {
                    state.value = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                colors = androidx.compose.material3.TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent, // Remove grey background
                    focusedIndicatorColor = Color.Transparent, // Remove underline when focused
                    unfocusedIndicatorColor = Color.Transparent, // Remove underline when not focused
                    disabledIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .weight(1f)
                    .background(color = Color.White),
                placeholder = { Text(text = "Type here.....", color = Hintgray,fontFamily = FontFamily(Font(R.font.nunito_black)),
                    fontSize = 13.sp)},
                maxLines = 3,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .background(Color(0xFF443ea8), RoundedCornerShape(40.dp))
                    .padding(15.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.send_icon),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.clickable {
                        chatRoomViewmodel.createChatRoom(
                            ChatRoom(state.value),
                            loginData?.userID!!,
                            senderID,
                            senderName.value,
                            loginData.userName!!
                        )
                        state.value = ""
                    }
                )
            }
        }
}
