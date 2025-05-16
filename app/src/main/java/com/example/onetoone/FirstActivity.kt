package com.example.onetoone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import com.example.onetoone.ui.theme.Yellow
import java.lang.Error

class FirstActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PreviewApp()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewApp(){

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
                    .weight(4f),
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
                    .weight(6f)
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
                    loginFormButtonView()

                }
            }
        }
    }
}

@Composable
fun loginFormButtonView(){

    var stateEmail = remember { mutableStateOf("") }
    var statePassword = remember { mutableStateOf("") }

    var isErrorEmail = remember { mutableStateOf(false) }
    var isErrorPassword = remember { mutableStateOf(false) }

    var errorMessage = remember { mutableStateOf("") }

    //Email Textfield
    OutlinedTextField(
        value = stateEmail.value,
        onValueChange ={
            stateEmail.value = it
            isErrorEmail.value = it.isBlank()
        },
        label = { Text(text = "Email") },
        placeholder = { Text(text = "example@gmail.com")},
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = isErrorEmail.value,
        colors = OutlinedTextFieldDefaults.colors(focusedLeadingIconColor = Yellow, focusedBorderColor = Yellow, focusedTextColor = Yellow, focusedLabelColor = Yellow, cursorColor = Yellow, unfocusedTextColor = Hintgray, unfocusedBorderColor = Hintgray, unfocusedLabelColor = Hintgray)
    )

    //Password Textfield
    var passwordVisible by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = statePassword.value,
        onValueChange ={
            statePassword.value = it
            isErrorPassword.value = it.isBlank()
        },
        label = { Text(text = "Password") },
        placeholder = { Text(text = "Password")},
        isError = isErrorPassword.value,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        leadingIcon = { Icon(painter = painterResource(id = R.drawable.baseline_password_24), contentDescription = null)},
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        colors = OutlinedTextFieldDefaults.colors(focusedLeadingIconColor = Yellow, focusedBorderColor = Yellow, focusedTextColor = Yellow, focusedLabelColor = Yellow, cursorColor = Yellow, unfocusedTextColor = Hintgray, unfocusedBorderColor = Hintgray, unfocusedLabelColor = Hintgray)
    )

    Text(text = "FORGOT PASSWORD", Modifier.fillMaxWidth(), color = Yellow, textAlign = TextAlign.End, fontSize = 12.sp)

    Button(
        onClick = {
            isErrorEmail.value = stateEmail.value.isBlank()
            isErrorPassword.value = statePassword.value.isBlank()
            if(isErrorEmail.value && isErrorPassword.value)
            {
                errorMessage.value = "Please fill all data"
            }
            else if(isErrorEmail.value)
            {
                errorMessage.value = "Enter email"
            }
            else if(isErrorPassword.value)
            {
                errorMessage.value = "Enter password"
            }
            else
            {
                errorMessage.value = "Done"
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .size(50.dp),
        enabled = true,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Yellow)
    ) {
        Text(text = "Login")
    }

    errorTextview(errorMessage = errorMessage.value)

}

@Composable
fun errorTextview(errorMessage: String){
    
    Text(
        text = errorMessage,
        Modifier.fillMaxWidth(),
        color = Color.Red,
        textAlign = TextAlign.Center,
        fontSize = 12.sp
    )
}
