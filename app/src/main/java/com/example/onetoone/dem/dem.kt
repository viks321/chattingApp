package com.example.onetoone.dem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onetoone.R

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5C4DFF))
            .padding(16.dp)
    ) {
        ChatHeader()
        Spacer(modifier = Modifier.height(8.dp))
        ChatMessages()
        Spacer(modifier = Modifier.height(8.dp))
        ChatInputBar()
    }
}

@Composable
fun ChatHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.user_icon),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("Larry Machigo", color = Color.White, fontWeight = FontWeight.Bold)
                Text("Online", color = Color(0xFFD0CFFF), fontSize = 12.sp)
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

@Composable
fun ChatMessages() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ChatBubble("Hey 👋", isSender = false)
        ChatBubble("Are you available for a New UI Project", isSender = false)
        ChatBubble("Hello!", isSender = true)
        ChatBubble("yes, have some space for the new task", isSender = true)
        ChatBubble("Cool, should I share the details now?", isSender = false)
        ChatBubble("Yes Sure, please", isSender = true)
        ChatBubble("Great, here is the SOW of the Project", isSender = false)
        FileBubble("UI Brief.docx", "269.18 KB")
    }
}

@Composable
fun ChatBubble(message: String, isSender: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isSender) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isSender) Color.White else Color(0xFF786FFF),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(12.dp)
                .widthIn(max = 260.dp)
        ) {
            Text(
                text = message,
                color = if (isSender) Color.Black else Color.White
            )
        }
    }
}

@Composable
fun FileBubble(fileName: String, fileSize: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        Row(
            modifier = Modifier
                .background(Color(0xFF786FFF), RoundedCornerShape(20.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_save),
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(fileName, color = Color.White, fontWeight = FontWeight.SemiBold)
                Text(fileSize, color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = android.R.drawable.stat_sys_download_done),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
fun ChatInputBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(30.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_btn_speak_now),
            contentDescription = null,
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Ok. Let me check", color = Color.Gray, modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_send),
            contentDescription = null,
            tint = Color.Gray
        )
    }
}
