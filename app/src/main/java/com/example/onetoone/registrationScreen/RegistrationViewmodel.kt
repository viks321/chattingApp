package com.example.onetoone.registrationScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onetoone.dataStoreforSaveUserPref.UserDataPref
import com.example.onetoone.models.Users
import com.example.onetoone.repositary.Repository
import com.example.onetoone.repositary.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewmodel @Inject constructor(val repository: Repository,val userDataPref: UserDataPref): ViewModel() {

    var isLoding by mutableStateOf(false)
    var isLoadingData by mutableStateOf(false)
    var userID by mutableStateOf("")

    init {
        viewModelScope.launch {
            userDataPref.isLoginFlow.collectLatest {
                isLoadingData = it!!
            }
        }
        GlobalScope.launch {
            userDataPref.userIDFlow.collectLatest {
                userID = it!!
            }
        }
    }

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


    fun registerUserOnFirebase() {
        viewModelScope.launch(Dispatchers.IO){
            repository.registerUser(userEmailSate,userPasswordSate)
        }
    }
    val regidterOnFirebaseLiveData : LiveData<Response<Users>>
        get() = repository.registationLiveData



    fun isValidation(): Boolean{
        if (userNameSate.isBlank() && userEmailSate.isBlank() && userPhoneSate.isBlank() && userPasswordSate.isBlank())
        {
            _errorValidationMutableLiveData.value =  "Please fill all details"
            userNameError = userNameSate.isBlank()
            userEmailError = userEmailSate.isBlank()
            userPhoneError = userPhoneSate.isBlank()
            userPasswordError = userPasswordSate.isBlank()
        }
        else if (userNameSate.isBlank()){
            _errorValidationMutableLiveData.value =  "Enter full name"
            userNameError = userNameSate.isBlank()
        }
        else if(userEmailSate.isBlank()){
            _errorValidationMutableLiveData.value =  "Enter email"
            userEmailError = userEmailSate.isBlank()
        }
        else if(userPhoneSate.isBlank()){
            _errorValidationMutableLiveData.value = "Enter phone number"
            userPhoneError = userPhoneSate.isBlank()
        }
        else if(userPasswordSate.isBlank()){
            _errorValidationMutableLiveData.value = "Enter password"
            userPasswordError = userPasswordSate.isBlank()
        }
        else{
            _errorValidationMutableLiveData.value = null
        }
        return _errorValidationMutableLiveData.value == null
    }

    val addMemberLiveData: LiveData<Response<Boolean>>
        get() = repository.addMemberOnFirebase

    fun addMemberData(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMemberOnFirebase(userID,userNameSate,userEmailSate,userPasswordSate,userPhoneSate)
        }
    }
}