package com.example.onetoone.loginScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onetoone.LoginModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewmodel: ViewModel() {

    private val _loginCradential = MutableLiveData<LoginModel>()
    val loginCradential: LiveData<LoginModel>
        get() = _loginCradential


    fun loginData(email: String,password: String) {
            val loginModel = LoginModel(email, password)
            _loginCradential.value = loginModel
    }
}