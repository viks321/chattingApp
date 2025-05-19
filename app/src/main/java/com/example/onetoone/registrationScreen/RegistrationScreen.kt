package com.example.onetoone.registrationScreen

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.onetoone.R
import com.example.onetoone.models.LoginModel
import com.example.onetoone.ui.theme.Cardbacground
import com.example.onetoone.ui.theme.DarkBackgroun
import com.example.onetoone.ui.theme.Hintgray
import com.example.onetoone.ui.theme.Yellow

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun screenPreview(){
    registrationScreen(rememberNavController(),LoginModel("",""), registrationViewmodel = RegistrationViewmodel())
}

@Composable
fun registrationScreen(navController: NavController, loginModel: LoginModel?,registrationViewmodel: RegistrationViewmodel){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = DarkBackgroun),
        contentAlignment = Alignment.Center,
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Create Account",
                    color = Yellow,
                    fontSize = 30.sp
                )
            }
            Card(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .weight(7f),
                colors = CardDefaults.cardColors(containerColor = Cardbacground)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "CREATE",
                        color = Hintgray,
                        fontSize = 15.sp,
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                    )
                    userNameTextfield(registrationViewmodel)
                }
            }
        }
        /*Text(
            text = "Email :"+loginModel?.email+"\nPassword :"+loginModel?.password,
            color = Color.White,
            modifier = Modifier.clickable {
                navController.navigate("loginScreen")
            }
        )*/


    }
}

@Composable
fun userNameTextfield(registrationViewmodel: RegistrationViewmodel){

    var errorMessage = remember { mutableStateOf("") }

    OutlinedTextField(
        value = registrationViewmodel.userNameSate,
        onValueChange = {
        registrationViewmodel.userNameSate = it
            registrationViewmodel.userNameError = it.isBlank()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        placeholder = { Text(text = "Enter full name")},
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Yellow, focusedBorderColor = Yellow, cursorColor = Yellow, unfocusedTextColor = Hintgray, unfocusedBorderColor = Hintgray, unfocusedLabelColor = Hintgray, focusedLabelColor = Yellow, focusedLeadingIconColor = Yellow, unfocusedLeadingIconColor = Hintgray),
        isError = registrationViewmodel.userNameError,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(
            text = "User Name",
            fontSize = 13.sp
        )},
        leadingIcon = { Icon(painter = painterResource(id = R.drawable.user_icon), contentDescription = null)}
    )
    OutlinedTextField(
        value = registrationViewmodel.userEmailSate,
        onValueChange = {
            registrationViewmodel.userEmailSate = it
            registrationViewmodel.userEmailError = it.isBlank()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
        placeholder = { Text(text = "example@gmail.com")},
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Yellow, focusedBorderColor = Yellow, cursorColor = Yellow, unfocusedTextColor = Hintgray, unfocusedBorderColor = Hintgray, unfocusedLabelColor = Hintgray, focusedLabelColor = Yellow, focusedLeadingIconColor = Yellow, unfocusedLeadingIconColor = Hintgray),
        isError = registrationViewmodel.userEmailError,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(
            text = "Enter email",
            fontSize = 13.sp
        )},
        leadingIcon = { Icon(painter = painterResource(id = R.drawable.email_icon), contentDescription = null)}
    )
    OutlinedTextField(
        value = registrationViewmodel.userPasswordSate,
        onValueChange = {
            registrationViewmodel.userPasswordSate = it
            registrationViewmodel.userPasswordError = it.isBlank()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        placeholder = { Text(text = "Enter password")},
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Yellow, focusedBorderColor = Yellow, cursorColor = Yellow, unfocusedTextColor = Hintgray, unfocusedBorderColor = Hintgray, unfocusedLabelColor = Hintgray, focusedLabelColor = Yellow, focusedLeadingIconColor = Yellow, unfocusedLeadingIconColor = Hintgray),
        isError = registrationViewmodel.userPasswordError,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(
            text = "Password",
            fontSize = 13.sp
        )},
        leadingIcon = { Icon(painter = painterResource(id = R.drawable.baseline_password_24), contentDescription = null)}
    )
    OutlinedTextField(
        value = registrationViewmodel.userPhoneSate,
        onValueChange = {
            registrationViewmodel.userPhoneSate = it
            registrationViewmodel.userPhoneError = it.isBlank()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        placeholder = { Text(text = "Enter phone number")},
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Yellow, focusedBorderColor = Yellow, cursorColor = Yellow, unfocusedTextColor = Hintgray, unfocusedBorderColor = Hintgray, unfocusedLabelColor = Hintgray, focusedLabelColor = Yellow, focusedLeadingIconColor = Yellow, unfocusedLeadingIconColor = Hintgray),
        isError = registrationViewmodel.userPhoneError,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(
            text = "Phone no",
            fontSize = 13.sp
        )},
        leadingIcon = { Icon(painter = painterResource(id = R.drawable.phone_icon), contentDescription = null)}
    )
    Button(
        onClick = {
            if (!registrationViewmodel.isValidation()){
                errorMessage.value = registrationViewmodel.errorValidationMutableLiveData.value.toString()
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
        Text(text = "Create account")
    }


        errorTextview(errorMessage.value)
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