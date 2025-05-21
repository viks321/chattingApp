package com.example.onetoone.loginScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onetoone.dataStoreforSaveUserPref.UserDataPref
import com.example.onetoone.models.LoginModel
import com.example.onetoone.repositary.Repository
import com.example.onetoone.repositary.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewmodel @Inject constructor(val repository: Repository,val userDataPref : UserDataPref): ViewModel() {

    var isLoading by mutableStateOf(false)
    var isLoadingData by mutableStateOf(false)

    init {
        viewModelScope.launch {
            userDataPref.isLoginFlow.collectLatest {
                isLoadingData = it!!
            }
        }
    }

    private val _loginCradential = MutableLiveData<LoginModel>()
    val loginCradential: LiveData<LoginModel>
        get() = _loginCradential


    fun loginData(email: String,password: String) {
            val loginModel = LoginModel("","",email, password)
            _loginCradential.value = loginModel
    }

    var stateEmail by mutableStateOf("")
    var statePassword by  mutableStateOf("")

    var isErrorEmail by mutableStateOf(false)
    var isErrorPassword by  mutableStateOf(false)

    var errorMessage = MutableLiveData<String>()


    fun isValidation(): Boolean{
        if(stateEmail.isBlank() && statePassword.isBlank()){
            isErrorEmail = stateEmail.isBlank()
            isErrorPassword = statePassword.isBlank()
            errorMessage.value = "Please fill all details"
        }
        else if(stateEmail.isBlank()){
            isErrorEmail = stateEmail.isBlank()
            errorMessage.value = "Enter email"
        }
        else if(statePassword.isBlank()){
            isErrorPassword = statePassword.isBlank()
            errorMessage.value = "Enter password"
        }
        else{
            errorMessage.value = null
        }
        return errorMessage.value == null
    }

    val loginOnFirebase: LiveData<Response<Boolean>>
        get() = repository.loginLiveData

    fun loginFirebase(){
        viewModelScope.launch(Dispatchers.IO){
            repository.loginUser(stateEmail,statePassword)
        }
    }

}