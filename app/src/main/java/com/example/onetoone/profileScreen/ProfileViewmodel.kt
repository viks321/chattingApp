package com.example.onetoone.profileScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onetoone.dataStoreforSaveUserPref.UserDataPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewmodel @Inject constructor(val userDataPref: UserDataPref): ViewModel() {

    private val _isLogoutData = MutableStateFlow<Boolean>(false)
    val isLogoutData : StateFlow<Boolean>
        get() = _isLogoutData

    fun logout(){
        viewModelScope.launch {
            userDataPref.logout()
        }
        return
    }
}