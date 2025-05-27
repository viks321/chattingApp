package com.example.onetoone.profileScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onetoone.dataStoreforSaveUserPref.UserDataPref
import com.example.onetoone.models.LoginModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewmodel @Inject constructor(val userDataPref: UserDataPref): ViewModel() {

    private val _isLogoutData = MutableStateFlow<Boolean>(false)
    val isLogoutData : StateFlow<Boolean>
        get() = _isLogoutData

    private val _profileData = MutableStateFlow<LoginModel>(LoginModel())
    val profileData : StateFlow<LoginModel>
        get() = _profileData

    init {
        viewModelScope.launch {
            val userID = userDataPref.userIDFlow.first()
            val userName = userDataPref.userNameFlow.first()
            val userEmail = userDataPref.userEmailFlow.first()
            val userPassword = userDataPref.userPasswordFlow.first()
            val userPhone = userDataPref.userPhoneFlow.first()

            val loginModel = LoginModel(userID, userName, userEmail, userPassword, userPhone)
            _profileData.emit(loginModel)
        }

    }

    fun logout(){
        viewModelScope.launch {
            userDataPref.logout()
            userDataPref.saveIslogin(false)
        }
        return
    }
}