package com.example.onetoone.chatRoomScreen

import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.onetoone.R
import com.example.onetoone.models.ChatRoom
import com.example.onetoone.models.LoginModel
import com.example.onetoone.models.Message
import com.example.onetoone.models.Messages
import com.example.onetoone.ui.theme.Hintgray
import com.example.onetoone.ui.theme.Yellow


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

    //Toast.makeText(navController.context,senderID.value,Toast.LENGTH_LONG).show()

    Log.d("VikasData",roomDataMessages.value.toString())

    if (!senderID.value.isEmpty()){
        chatRoomViewmodel.getMessageData(senderID.value)
    }

    val loginData = navController
        .previousBackStackEntry
        ?.savedStateHandle
        ?.get<LoginModel>("loginData")

    Scaffold {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize()){

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Box {
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
                                    .clickable { navController.popBackStack() }
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
                }

                Box(
                    modifier = Modifier.weight(1f)
                ) {

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

                    LazyColumn() {
                        items(sortedMessages){
                            Column(
                                modifier = Modifier.fillMaxWidth(),
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

                Box(
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
                }
            }

        }
    }

}

@Composable
fun senderView(s: String) {

    Box(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .padding(50.dp, 0.dp, 0.dp, 0.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = s,
            fontSize = 15.sp,
            color = Yellow,
            modifier = Modifier
                .shadow(8.dp, shape = RoundedCornerShape(10.dp))
                .align(alignment = Alignment.CenterEnd)
                .padding(25.dp)
        )
    }
}

@Composable
fun reciverView(s: String) {

    Box(
        modifier = Modifier
            .padding(10.dp)
            .padding(0.dp, 0.dp, 50.dp, 0.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = s,
            fontSize = 15.sp,
            color = Color.Black,
            modifier = Modifier
                .shadow(8.dp, shape = RoundedCornerShape(10.dp))
                .padding(25.dp)
        )
    }
}
