package com.example.onetoone.repositary

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.onetoone.dataStoreforSaveUserPref.UserDataPref
import com.example.onetoone.models.ChatRoom
import com.example.onetoone.models.LoginModel
import com.example.onetoone.models.RoomModel
import com.example.onetoone.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID
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
        //val receiverMessage = userRef.child("allUsers").child(chatRoom.userID!!).child("romms").child(chatroomID).child("receiverMessage")
        userRef.child("allUsers").child(sernderID).child("romms").child(receiverID).child("senderMessage").push().setValue(ChatRoom(chatRoom.message))
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

        /*val messageReceiverMap = mapOf(
            "message" to ChatRoom(chatRoom.receiverID,chatRoom.message)
        )*/

        userRef.child("allUsers").child(receiverID).child("romms").child(sernderID).child("receiverMessage").push().setValue(ChatRoom(chatRoom.message))
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
    private val _roomDataMutableState = MutableStateFlow<List<ChatRoom>?>(emptyList())
    val roomDataMutableState: StateFlow<List<ChatRoom>?>
        get() = _roomDataMutableState

    suspend fun getMessagesFromFirebase(userID: String){
        val userRef = firebaseDatabase.getReference("allMembers").child("allUsers").child("wnY3geG6odMXZTKB5IBq1Uf4sF53").child("romms").child("wnY3geG6odMXZTKB5IBq1Uf4sF53")

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val room = snapshot.getValue(RoomModel::class.java)

                CoroutineScope(Dispatchers.IO).launch {
                    _roomDataMutableState.emit(room?.senderMessage)
                }
                // Print all sender messages
                /*room?.senderMessage?.forEach { (key, msg) ->
                    Log.d("SenderMessage", "$key: ${msg.message}")
                }*/

                // Print all receiver messages
                /*room?.receiverMessage?.forEach { (key, msg) ->
                    Log.d("ReceiverMessage", "$key: ${msg.message}")
                }*/
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error: ${error.message}")
            }
        })

    }

}