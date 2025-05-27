package com.example.onetoone.homeScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onetoone.dataStoreforSaveUserPref.UserDataPref
import com.example.onetoone.models.ChatRoom
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
class HomeViewmodel @Inject constructor(val repository: Repository,val userDataPref: UserDataPref): ViewModel() {

    val allMemberLiveData : StateFlow<Response<List<UserData>>>
        get() = repository.allMemberMutableLiveData

    val chatRoomLiveData : StateFlow<Response<List<Messages>?>>
        get() = repository.chatRoomDataMutableState

    private val _currentUserID = MutableStateFlow<String>("")
    val currentUserID : StateFlow<String>
        get() = _currentUserID

    private val _currentUserName = MutableStateFlow<String>("")
    val currentUserName : StateFlow<String>
        get() = _currentUserName

    init {
        viewModelScope.launch {
            userDataPref.userIDFlow.collectLatest {
                _currentUserID.value = it!!
            }
        }

        viewModelScope.launch {
            userDataPref.userNameFlow.collectLatest {
                _currentUserName.value = it!!
            }
        }
    }

    fun getAllMembers(currentUserID: String){
        viewModelScope.launch(Dispatchers.IO){
            repository.getAllMemberFromFirebase(currentUserID)
        }
    }

    fun createChatRoom(receiverID: String, sender: String, senderName:String, receiverName:String){
        viewModelScope.launch {
            repository.createChatRoomForMemberList(receiverID,sender,senderName,receiverName)
        }
    }

}