package com.example.onetoone.loginScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.onetoone.R
import com.example.onetoone.lodingScreen.lodingScreen
import com.example.onetoone.models.LoginModel
import com.example.onetoone.repositary.Response
import com.example.onetoone.ui.theme.Cardbacground
import com.example.onetoone.ui.theme.DarkBackgroun
import com.example.onetoone.ui.theme.Hintgray
import com.example.onetoone.ui.theme.Yellow

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun screenPreview(){
    loginScreen(onClick = { model -> }, rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun loginScreen(onClick: (LoginModel)-> Unit,navController: NavController){

    val loginViewmodel : LoginViewmodel = hiltViewModel()

    Box(
        Modifier
            .background(color = DarkBackgroun)
            .fillMaxSize()
            ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            //LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
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
                    Text(
                        text = "Create Account",
                        fontSize = 25.sp,
                        color = Hintgray,
                        fontWeight = FontWeight.Thin,
                        modifier = Modifier.clickable {
                            loginViewmodel.loginData("","")
                            //errorMessage.value = "Email: "+loginViewmodel.loginCradential.value?.email + "\nPassword: "+loginViewmodel.loginCradential.value?.password
                            onClick(LoginModel("",loginViewmodel.loginCradential.value!!.email,loginViewmodel.loginCradential.value!!.password,""))
                        }
                    )
                    Text(text = "Login", fontSize = 20.sp, color = Hintgray, fontWeight = FontWeight.Bold)
                    loginFormButtonView(loginViewmodel,onClick,navController)

                }
            }
        }

        if (loginViewmodel.isLoading) {
            //Loding Screen
            lodingScreen()
        }

        loginViewmodel.loginOnFirebase.observeForever(Observer {
            when(it){

                is Response.Loading ->{
                    loginViewmodel.isLoading = true
                }
                is Response.Success ->{
                    if(loginViewmodel.loginOnFirebase.value?.data!!){
                        loginViewmodel.isLoading = false
                        navController.navigate("homeScreen")
                    }
                }
                is Response.Error ->{
                    loginViewmodel.isLoading = false
                }
            }
        })

    }

}

@Composable
fun loginFormButtonView(
    loginViewmodel: LoginViewmodel,
    onClick: (LoginModel) -> Unit,
    navController: NavController,
){

    var errorMessage = remember { mutableStateOf("") }

    //Email Textfield
    OutlinedTextField(
        value = loginViewmodel.stateEmail,
        onValueChange ={
            loginViewmodel.stateEmail = it
            loginViewmodel.isErrorEmail = it.isBlank()
        },
        label = { Text(text = "Email") },
        placeholder = { Text(text = "example@gmail.com") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = loginViewmodel.isErrorEmail,
        colors = OutlinedTextFieldDefaults.colors(focusedLeadingIconColor = Yellow, focusedBorderColor = Yellow, focusedTextColor = Yellow, focusedLabelColor = Yellow, cursorColor = Yellow, unfocusedTextColor = Hintgray, unfocusedBorderColor = Hintgray, unfocusedLabelColor = Hintgray)
    )

    //Password Textfield
    var passwordVisible by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = loginViewmodel.statePassword,
        onValueChange ={
            loginViewmodel.statePassword = it
            loginViewmodel.isErrorPassword = it.isBlank()
        },
        label = { Text(text = "Password") },
        placeholder = { Text(text = "Password") },
        isError = loginViewmodel.isErrorPassword,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        leadingIcon = { Icon(painter = painterResource(id = R.drawable.baseline_password_24), contentDescription = null) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        colors = OutlinedTextFieldDefaults.colors(focusedLeadingIconColor = Yellow, focusedBorderColor = Yellow, focusedTextColor = Yellow, focusedLabelColor = Yellow, cursorColor = Yellow, unfocusedTextColor = Hintgray, unfocusedBorderColor = Hintgray, unfocusedLabelColor = Hintgray)
    )

    Text(text = "FORGOT PASSWORD", Modifier.fillMaxWidth(), color = Yellow, textAlign = TextAlign.End, fontSize = 12.sp)

    Button(
        onClick = {
            if (!loginViewmodel.isValidation()){
                errorMessage.value = loginViewmodel.errorMessage.value.toString()
            }
            else
            {
                loginViewmodel.loginFirebase()
                //loginViewmodel.loginData(stateEmail.value,statePassword.value)
                //errorMessage.value = "Email: "+loginViewmodel.loginCradential.value?.email + "\nPassword: "+loginViewmodel.loginCradential.value?.password
                //onClick(LoginModel(loginViewmodel.loginCradential.value!!.email,loginViewmodel.loginCradential.value!!.password))
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

    if (!errorMessage.value.isBlank())
    {
        errorTextview(errorMessage = errorMessage.value)
    }
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
