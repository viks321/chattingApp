package com.example.onetoone.repositary

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.onetoone.dataStoreforSaveUserPref.UserDataPref
import com.example.onetoone.models.ChatRoom
import com.example.onetoone.models.LoginModel
import com.example.onetoone.models.Message
import com.example.onetoone.models.Messages
import com.example.onetoone.models.UserData
import com.example.onetoone.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class Repository @Inject constructor(val context: Context,val auth: FirebaseAuth,val firebaseDatabase: FirebaseDatabase,val userDataPref: UserDataPref) {

    val _registationMutableLiveData = MutableLiveData<Response<Users>>()
    val registationLiveData : LiveData<Response<Users>>
        get() = _registationMutableLiveData

    val _loginMutableLiveData = MutableLiveData<Response<Boolean>>()
    val loginLiveData : LiveData<Response<Boolean>>
        get() = _loginMutableLiveData


    suspend fun loginUser(email: String, password: String) {

        _loginMutableLiveData.postValue(Response.Loading(null))

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    CoroutineScope(Dispatchers.IO).launch {
                        userDataPref.saveUserId(it.result.user?.uid!!)
                        userDataPref.saveIslogin(true)
                    }
                    //mutableLiveData.value = it.exception?.message
                    //CallActivity.launch(context,HomeActivity::class.java)
                    _loginMutableLiveData.postValue(Response.Success(it.isSuccessful))
                    Toast.makeText(context, "Login successfully", Toast.LENGTH_SHORT).show()
                } else {
                    _loginMutableLiveData.postValue(Response.Error(it.exception?.message.toString()))
                    Toast.makeText(context, "Login failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    suspend fun registerUser(userName: String,email: String,password: String,phone: String) {

        _registationMutableLiveData.postValue(Response.Loading(null))
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _registationMutableLiveData.postValue(Response.Success(Users(it.result.user?.uid,it.result.user?.email)))
                    CoroutineScope(Dispatchers.IO).launch {
                        userDataPref.saveUserId(it.result.user?.uid!!)
                        userDataPref.saveIslogin(true)
                        addMemberOnFirebase(it.result.user?.uid!!,userName, email, password, phone)
                    }
                    Toast.makeText(context, "Registered successfully", Toast.LENGTH_SHORT).show()
                } else {
                    _registationMutableLiveData.postValue(Response.Error(it.exception?.message.toString()))
                    Toast.makeText(context, "Registration failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private val _addMemberOnFirebase = MutableLiveData<Response<Boolean>>()
    val addMemberOnFirebase: LiveData<Response<Boolean>>
        get() = _addMemberOnFirebase

    suspend fun addMemberOnFirebase(userID: String,userName: String,email: String,password: String,phone: String){
        val userRef = firebaseDatabase.getReference("allMembers")
        val loginModel = LoginModel(userID,userName,email,password,phone)
        userRef.child("allUsers").child(userID).setValue(loginModel)
            .addOnSuccessListener {
            _addMemberOnFirebase.postValue(Response.Success(true))
            }
            .addOnFailureListener {
                _addMemberOnFirebase.postValue(Response.Error("Failed"))
            }
    }

    private val _allMemberMutableLiveData = MutableStateFlow<List<LoginModel>>(emptyList())
    val allMemberMutableLiveData: StateFlow<List<LoginModel>>
        get() = _allMemberMutableLiveData

    suspend fun getAllMemberFromFirebase(){
        val userRef = firebaseDatabase.getReference("allMembers").child("allUsers")
        userRef.get().addOnCompleteListener { snapsort ->
            val userList = snapsort.result.children.mapNotNull { it.getValue(LoginModel::class.java) }
            CoroutineScope(Dispatchers.IO).launch {
                _allMemberMutableLiveData.emit(userList)
            }
            Log.d("Firebase", "Users: $userList")
        }
            .addOnFailureListener {
                Log.e("Firebase", "Failed to read", it)
            }
    }

    private val _createRoomMutableStateFlow = MutableStateFlow<Boolean>(false)
    val createRoomMutableStateFlow : StateFlow<Boolean>
        get() = _createRoomMutableStateFlow

    suspend fun createChatRoomFirebase(receiverID: String, sernderID: String,chatRoom: ChatRoom){

        val userRef = firebaseDatabase.getReference("allMembers")
        //val senderMessage = ChatRoom("senderMessage",chatRoom.message)
        val senderMessage = mapOf(
            "senderMessage" to ChatRoom(chatRoom.message,ServerValue.TIMESTAMP)
        )
        //val receiverMessage = userRef.child("allUsers").child(chatRoom.userID!!).child("romms").child(chatroomID).child("receiverMessage")
        userRef.child("allUsers").child(sernderID).child("rooms").child(receiverID).child("messages").push().setValue(senderMessage)
            .addOnSuccessListener {
                CoroutineScope(Dispatchers.IO).launch {
                    _createRoomMutableStateFlow.emit(true)
                }

            }
            .addOnFailureListener {
                CoroutineScope(Dispatchers.IO).launch {
                    _createRoomMutableStateFlow.emit(false)
                }
            }

        val receiverMessage = mapOf(
            "receiverMessage" to ChatRoom(chatRoom.message,ServerValue.TIMESTAMP)
        )
        //val receiverMessage = ChatRoom("receiverMessage",chatRoom.message)
        //userRef.child("allUsers").child(receiverID).child("rooms").child(sernderID).child("receiverMessage").push().setValue(ChatRoom(chatRoom.message))
        userRef.child("allUsers").child(receiverID).child("rooms").child(sernderID).child("messages").push().setValue(receiverMessage)
            .addOnSuccessListener {
                CoroutineScope(Dispatchers.IO).launch {
                    _createRoomMutableStateFlow.emit(true)
                }

            }
            .addOnFailureListener {
                CoroutineScope(Dispatchers.IO).launch {
                    _createRoomMutableStateFlow.emit(false)
                }
            }

    }


    //getMessageData

    //sender
    private val _senderMessageMutableState = MutableStateFlow<List<ChatRoom>?>(emptyList())
    val senderMessageMutableState: StateFlow<List<ChatRoom>?>
        get() = _senderMessageMutableState

    private val _reciverMessageMutableState = MutableStateFlow<List<Messages>?>(emptyList())
    val reciverMessageMutableState: StateFlow<List<Messages>?>
        get() = _reciverMessageMutableState

    suspend fun getMessagesFromFirebase(userID: String){
        val userRef = firebaseDatabase.getReference("allMembers").child("allUsers").child(userID)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableListOf<Message>()
                var senderMsgList = mutableListOf<ChatRoom>()
                var receiverMsgList = mutableListOf<Messages>()

                /*for (child in snapshot.children) {
                    val chatMessage = child.getValue(Message::class.java)
                    if (chatMessage != null) {
                        messages.add(chatMessage)
                    }
                }*/

                //val userList = snapshot.children.mapNotNull { it.getValue(UserData::class.java) }
                val userList = snapshot.getValue(UserData::class.java)
                //Log.e("Firebase", "userData: ${snapshot.value.toString()}")
                /*for (i in userList.indices){*/
                    val rommsMap = userList?.rooms
                    if (rommsMap != null) {
                        for ((chatPartnerID, chatRoom) in rommsMap) {
                            Log.d("FirebaseChat", "Chat with: $chatPartnerID")

                            receiverMsgList.add(chatRoom)

                            // Receiver messages
                            /*chatRoom.receiverMessage?.forEach { (msgId, msg) ->
                                Log.d("FirebaseChat", "Receiver Message [$msgId]: ${msg.message}")
                                //receiverMsgList.add(ChatRoom(msg.message))
                            }*/

                            // Sender messages
                            /*chatRoom.senderMessage?.forEach { (msgId, msg) ->
                                Log.d("FirebaseChat", "Sender Message [$msgId]: ${msg.message}")
                                senderMsgList.add(ChatRoom(msg.message))
                            }*/

                            CoroutineScope(Dispatchers.IO).launch {
                                //_senderMessageMutableState.emit(senderMsgList)
                                _reciverMessageMutableState.emit(receiverMsgList)
                            }
                        }
                    } else {
                        Log.d("FirebaseChat", "No chat rooms found for this user.")
                    }
                /*}*/
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to read users", error.toException())
            }
        })

    }

}