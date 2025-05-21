package com.example.onetoone.chatRoomScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onetoone.dataStoreforSaveUserPref.UserDataPref
import com.example.onetoone.models.ChatRoom
import com.example.onetoone.models.RoomModel
import com.example.onetoone.repositary.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewmodel @Inject constructor(val repository: Repository,val userDataPref: UserDataPref):ViewModel() {

    val createRoomLiveStateFlow : StateFlow<Boolean>
        get() = repository.createRoomMutableStateFlow

    val roomDataMessages : StateFlow<List<ChatRoom>?>
        get() = repository.roomDataMutableState

    var senderID = ""

    init {
        viewModelScope.launch {
            userDataPref.userIDFlow.collectLatest {
                senderID = it!!
            }
        }

        viewModelScope.launch {
            repository.getMessagesFromFirebase(senderID)
        }
    }

    fun createChatRoom(chatRoom: ChatRoom,receiverID: String){
        viewModelScope.launch {
            repository.createChatRoomFirebase(receiverID,senderID,chatRoom)
        }
    }
}