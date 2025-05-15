package com.example.onetoone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onetoone.ui.theme.Cardbacground
import com.example.onetoone.ui.theme.DarkBackgroun
import com.example.onetoone.ui.theme.Hintgray
import com.example.onetoone.ui.theme.OneToOneTheme
import com.example.onetoone.ui.theme.Purple40
import com.example.onetoone.ui.theme.Yellow
import java.time.format.FormatStyle

class FirstActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            preview()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun preview(){

    Box(
        Modifier
            .background(color = DarkBackgroun)
            .padding(20.dp)) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f),
                contentAlignment = Alignment.Center

            ) {
                Text(
                    text = "Welcome Back!",
                    color = Yellow,
                    fontStyle = FontStyle.Normal,
                    fontSize = 30.sp,
                )
            }
            Card(
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Cardbacground),
                modifier = Modifier
                    .weight(7f)
                    .fillMaxWidth()
            )
            {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                    Text(text = "Create Account", fontSize = 25.sp, color = Hintgray, fontWeight = FontWeight.Thin)
                    Text(text = "Login", fontSize = 20.sp, color = Hintgray, fontWeight = FontWeight.Bold)
                    textFieldEmail("Email","example@gmail.com")
                    textFeldPassword("Password","password")
                    Button(
                        onClick = { "Hello vikas" },
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(50.dp),
                        enabled = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Yellow)
                    ) {
                        Text(text = "Login")
                    }
                }
            }
        }
    }
}

@Composable
fun textFieldEmail(hint: String,placeholderData: String)
{
    var state = remember { mutableStateOf("") }

    OutlinedTextField(
        value = state.value,
        onValueChange ={ state.value = it},
        label = { Text(text = hint) },
        placeholder = { Text(text = placeholderData)},
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Yellow, focusedTextColor = Yellow, focusedLabelColor = Yellow, cursorColor = Yellow, unfocusedTextColor = Hintgray, unfocusedBorderColor = Hintgray, unfocusedLabelColor = Hintgray)
        )
}

@Composable
fun textFeldPassword(hint: String,placeholderData: String){

    var state = remember { mutableStateOf("") }
    var passwordVisible by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = state.value,
        onValueChange ={ state.value = it},
        label = { Text(text = hint) },
        placeholder = { Text(text = placeholderData)},
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Yellow, focusedTextColor = Yellow, focusedLabelColor = Yellow, cursorColor = Yellow, unfocusedTextColor = Hintgray, unfocusedBorderColor = Hintgray, unfocusedLabelColor = Hintgray)
    )
}
