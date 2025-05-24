package com.example.onetoone.chatRoomScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onetoone.dataStoreforSaveUserPref.UserDataPref
import com.example.onetoone.models.ChatRoom
import com.example.onetoone.models.LoginModel
import com.example.onetoone.models.Message
import com.example.onetoone.models.Messages
import com.example.onetoone.models.UserData
import com.example.onetoone.repositary.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewmodel @Inject constructor(val repository: Repository,val userDataPref: UserDataPref):ViewModel() {

    val _userName = MutableStateFlow<String>("")
    val userName : StateFlow<String>
        get() = _userName

    val createRoomLiveStateFlow : StateFlow<Boolean>
        get() = repository.createRoomMutableStateFlow

    val roomDataMessages : StateFlow<List<Messages>?>
        get() = repository.reciverMessageMutableState

    private val _senderId = MutableStateFlow<String>("")
    val senderID : StateFlow<String>
        get() = _senderId

    init {
        viewModelScope.launch {
            userDataPref.userIDFlow.collectLatest {
                _senderId.value = it!!
            }
        }
        viewModelScope.launch {
            userDataPref.userNameFlow.collectLatest {
                _userName.value = it!!
            }
        }
    }

    fun createChatRoom(chatRoom: ChatRoom,receiverID: String,sender: String,senderName:String,receiverName:String){
        viewModelScope.launch {
            repository.createChatRoomFirebase(receiverID,sender,chatRoom,senderName,receiverName)
        }
    }

    fun getMessageData (sendID: String,roomID: String){
        viewModelScope.launch {
            repository.getMessagesFromFirebase(sendID,roomID)
        }
    }
}