package com.example.onetoone.myAllChatScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onetoone.dataStoreforSaveUserPref.UserDataPref
import com.example.onetoone.models.LoginModel
import com.example.onetoone.models.Messages
import com.example.onetoone.models.UserData
import com.example.onetoone.repositary.Repository
import com.example.onetoone.repositary.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyAllChatViewmodel@Inject constructor(val repository: Repository, val userDataPref: UserDataPref): ViewModel() {

    val allMemberLiveData : StateFlow<Response<List<UserData>>>
        get() = repository.allMemberMutableLiveData

    val chatRoomLiveData : StateFlow<Response<List<Messages>?>>
        get() = repository.chatRoomDataMutableState

    private val _currentUserID = MutableStateFlow<String>("")
    val currentUserID : StateFlow<String>
        get() = _currentUserID

    init {
        viewModelScope.launch {
            userDataPref.userIDFlow.collectLatest {
                _currentUserID.value = it!!
            }
        }
    }

    fun getAllMembers(currentUserID: String){
        viewModelScope.launch(Dispatchers.IO){
            repository.getAllMemberFromFirebase(currentUserID)
        }
    }

}