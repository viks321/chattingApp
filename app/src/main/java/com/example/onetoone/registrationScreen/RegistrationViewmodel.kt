package com.example.onetoone.registrationScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegistrationViewmodel: ViewModel() {

    var userNameSate by mutableStateOf("")
    var userNameError by mutableStateOf(false)

    var userEmailSate by mutableStateOf("")
    var userEmailError by mutableStateOf(false)

    var userPasswordSate by mutableStateOf("")
    var userPasswordError by mutableStateOf(false)

    var userPhoneSate by mutableStateOf("")
    var userPhoneError by mutableStateOf(false)

    private val _errorValidationMutableLiveData = MutableLiveData<String>()
    val errorValidationMutableLiveData : LiveData<String>
        get() = _errorValidationMutableLiveData

    fun isValidation(): Boolean{
        _errorValidationMutableLiveData.value = if (userNameSate.isBlank()){
            "Enter full name"
        }
        else if(userEmailSate.isBlank()){
            "Enter email"
        }
        else if(userPhoneSate.isBlank()){
            "Enter phone number"
        }
        else if(userPasswordSate.isBlank()){
            "Enter password"
        }
        else{
            null
        }
        return _errorValidationMutableLiveData.value == null
    }
}