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
import com.example.onetoone.models.UserActivity
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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

    private val _chatRoomDataMutableState = MutableStateFlow<Response<List<Messages>?>>(Response.Loading(null))
    val chatRoomDataMutableState: StateFlow<Response<List<Messages>?>>
        get() = _chatRoomDataMutableState

    private val _allMemberMutableLiveData = MutableStateFlow<Response<List<UserData>>>(Response.Loading(null))
    val allMemberMutableLiveData: StateFlow<Response<List<UserData>>>
        get() = _allMemberMutableLiveData

    suspend fun getAllMemberFromFirebase(currentUserID: String){

        _allMemberMutableLiveData.emit(Response.Loading(null))
        _chatRoomDataMutableState.emit(Response.Loading(null))

        Log.d("FirebaseVikasdvjjbjvbsdhjbvjhsdabvjbdvj", "Call vikas")

        var chatRoomList = mutableListOf<Messages>()

        val userRef = firebaseDatabase.getReference("allMembers").child("allUsers")
        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val userDataNEW = mutableListOf<UserData>()
                val myChatData = mutableListOf<UserData>()
                val chatUserList = snapshot.children.mapNotNull { it.getValue(UserData::class.java) }
                myChatData.addAll(chatUserList)

                for (i in chatUserList.indices){

                    if(chatUserList.get(i).userID.equals(currentUserID)){

                        val rommsMap = chatUserList.get(i).rooms
                        if (rommsMap != null) {
                            for ((chatPartnerID, chatRoom) in rommsMap) {
                                chatRoomList.add(chatRoom)
                            }

                            CoroutineScope(Dispatchers.IO).launch {
                                _chatRoomDataMutableState.emit(Response.Success(chatRoomList))
                            }

                            Log.d("FirebaseRoom", "Data: ${chatRoomList.toString()}")
                        } else {
                            Log.d("FirebaseChat", "No chat rooms found for this user.")
                        }

                    }
                }

                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(UserData::class.java)
                    val roomsMap = user?.rooms

                    // If user has no rooms or rooms does NOT contain the roomIdToExclude
                    if (roomsMap?.containsKey(currentUserID) == false || roomsMap == null) {
                        user?.let { userDataNEW.add(it) }
                    }

                    if (user?.userID.equals(currentUserID)) {
                        CoroutineScope(Dispatchers.IO).launch {
                            userDataPref.saveUserId(user?.userID.toString())
                            userDataPref.saveUsername(user?.userName.toString())
                            userDataPref.saveUserEmail(user?.email.toString())
                            userDataPref.saveUserPhone(user?.phoneNo.toString())
                            userDataPref.saveUserPassword(user?.password.toString())
                        }
                    }
                }

                CoroutineScope(Dispatchers.IO).launch {
                    userDataNEW.removeAll { it.userID == currentUserID }
                    _allMemberMutableLiveData.emit(Response.Success(userDataNEW))
                }

                /*if(!userDataNEW.isEmpty()){
                    CoroutineScope(Dispatchers.IO).launch {
                        userDataNEW.removeAll { it.userID == currentUserID }
                        _allMemberMutableLiveData.emit(userDataNEW)
                    }
                    //Log.d("FirebaseVikasYESRoom", "Users: ${userDataNEW}")

                }*/

            }

            override fun onCancelled(error: DatabaseError) {
                CoroutineScope(Dispatchers.IO).launch {
                    _allMemberMutableLiveData.emit(Response.Error(error.message.toString()))
                    _chatRoomDataMutableState.emit(Response.Error(error.message.toString()))
                }
                //Log.e("Firebase", "Failed to read", error.message.toString())
            }
        })
    }

    private val _createRoomMutableStateFlow = MutableStateFlow<Boolean>(false)
    val createRoomMutableStateFlow : StateFlow<Boolean>
        get() = _createRoomMutableStateFlow

    suspend fun createChatRoomFirebase(receiverID: String, sernderID: String,chatRoom: ChatRoom,receiverName:String,senderName: String){

        val lastMessagetime = SimpleDateFormat("hh:mm a", Locale.getDefault())
            .format(Date())

        val userRef = firebaseDatabase.getReference("allMembers")
        //val senderMessage = ChatRoom("senderMessage",chatRoom.message)
        val senderMessage = mapOf(
            "senderMessage" to ChatRoom(chatRoom.message,ServerValue.TIMESTAMP)
        )

        userRef.child("allUsers").child(sernderID).child("rooms").child(receiverID).child("userID").setValue(receiverID)
        userRef.child("allUsers").child(sernderID).child("rooms").child(receiverID).child("userName").setValue(senderName)
        /*userRef.child("allUsers").child(sernderID).child("rooms").child(receiverID).child("lastMessage").setValue("No message here..")
        userRef.child("allUsers").child(sernderID).child("rooms").child(receiverID).child("lastMessageTime").setValue("00:00 Am")*/
        userRef.child("allUsers").child(sernderID).child("rooms").child(receiverID).child("active").setValue(0)
        userRef.child("allUsers").child(sernderID).child("rooms").child(receiverID).child("messageCount").setValue(0)
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
        userRef.child("allUsers").child(receiverID).child("rooms").child(sernderID).child("userID").setValue(sernderID)
        userRef.child("allUsers").child(receiverID).child("rooms").child(sernderID).child("userName").setValue(receiverName)
        userRef.child("allUsers").child(receiverID).child("rooms").child(sernderID).child("lastMessage").setValue(chatRoom.message)
        userRef.child("allUsers").child(receiverID).child("rooms").child(sernderID).child("lastMessageTime").setValue(lastMessagetime)
        userRef.child("allUsers").child(receiverID).child("rooms").child(sernderID).child("active").setValue(0)
        userRef.child("allUsers").child(receiverID).child("rooms").child(sernderID).child("messageCount").setValue(1)
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

    suspend fun getMessagesFromFirebase(userID: String,roomID: String){
        val userRef = firebaseDatabase.getReference("allMembers").child("allUsers").child(userID)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableListOf<Messages>()
                var userData = mutableListOf<UserData>()
                var receiverMsgList = mutableListOf<Messages>()

                /*for (child in snapshot.children) {
                    val chatMessage = child.getValue(Message::class.java)
                    if (chatMessage != null) {
                        messages.add(chatMessage)
                    }
                }*/

                Toast.makeText(context,"Call vikas rao",Toast.LENGTH_LONG).show()
                //val userList = snapshot.children.mapNotNull { it.getValue(UserData::class.java) }
                val userList = snapshot.getValue(UserData::class.java)
                //Log.e("Firebase", "userData: ${snapshot.value.toString()}")
                /*for (i in userList.indices){*/
                    val rommsMap = userList?.rooms
                    if (rommsMap != null) {
                        for ((chatPartnerID, chatRoom) in rommsMap) {
                            Log.d("FirebaseChat", "Chat with: $chatPartnerID")
                            Log.d("FirebaseChat", "userID with: $roomID")

                            if (chatPartnerID.equals(roomID)){
                                receiverMsgList.add(chatRoom)
                            }

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

                        Log.d("FirebaseVikas", "Data: ${receiverMsgList.toString()}")
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


    suspend fun updateMessageCountONFirebase(curentUserID: String, roomID: String){
        val userRef = firebaseDatabase.getReference("allMembers").child("allUsers").child(curentUserID).child("rooms").child(roomID)


        val messageDetails = mapOf(
            "messageCount" to 0,
        )
        userRef.updateChildren(messageDetails)
            .addOnSuccessListener { snapshort ->
                //_addMemberOnFirebase.postValue(Response.Success(true))
            }
            .addOnFailureListener {
                //_addMemberOnFirebase.postValue(Response.Error("Failed"))
            }
    }


    suspend fun createChatRoomForMemberList(receiverID: String, sernderID: String,receiverName:String,senderName: String){

        val lastMessagetime = SimpleDateFormat("hh:mm a", Locale.getDefault())
            .format(Date())

        val userRef = firebaseDatabase.getReference("allMembers")
        //val senderMessage = ChatRoom("senderMessage",chatRoom.message)
        /*val senderMessage = mapOf(
            "senderMessage" to ChatRoom(chatRoom.message,ServerValue.TIMESTAMP)
        )*/

        val sernder = Messages(userID = receiverID, userName = senderName, lastMessage = "No message here...", lastMessageTime = "00:00 AM/PM", messageCount = 0,isActive = 0)
        /*userRef.child("allUsers").child(sernderID).child("rooms").child(receiverID).child("userID").setValue(sernderID)
        userRef.child("allUsers").child(sernderID).child("rooms").child(receiverID).child("userName").setValue(senderName)
        userRef.child("allUsers").child(sernderID).child("rooms").child(receiverID).child("lastMessage").setValue("No message here..")
        userRef.child("allUsers").child(sernderID).child("rooms").child(receiverID).child("lastMessageTime").setValue("00:00 Am")
        userRef.child("allUsers").child(sernderID).child("rooms").child(receiverID).child("isActive").setValue(0)
        userRef.child("allUsers").child(sernderID).child("rooms").child(receiverID).child("messageCount").setValue(0)*/
        //val receiverMessage = userRef.child("allUsers").child(chatRoom.userID!!).child("romms").child(chatroomID).child("receiverMessage")
        userRef.child("allUsers").child(sernderID).child("rooms").child(receiverID).setValue(sernder)
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

        /*val receiverMessage = mapOf(
            "receiverMessage" to ChatRoom(chatRoom.message,ServerValue.TIMESTAMP)
        )*/
        val receiverMessage = Messages(userID = sernderID, userName = receiverName, lastMessage = "No message here...", lastMessageTime = "00:00 AM/PM", messageCount = 0,isActive = 0)
        /*userRef.child("allUsers").child(receiverID).child("rooms").child(sernderID).child("userID").setValue(receiverID)
        userRef.child("allUsers").child(receiverID).child("rooms").child(sernderID).child("userName").setValue(receiverName)
        userRef.child("allUsers").child(receiverID).child("rooms").child(sernderID).child("lastMessage").setValue("No message here...")
        userRef.child("allUsers").child(receiverID).child("rooms").child(sernderID).child("lastMessageTime").setValue("00:00 AM/PM")
        userRef.child("allUsers").child(receiverID).child("rooms").child(sernderID).child("isActive").setValue(0)
        userRef.child("allUsers").child(receiverID).child("rooms").child(sernderID).child("messageCount").setValue(1)*/
        //userRef.child("allUsers").child(receiverID).child("rooms").child(sernderID).child("receiverMessage").push().setValue(ChatRoom(chatRoom.message))
        userRef.child("allUsers").child(receiverID).child("rooms").child(sernderID).setValue(receiverMessage)
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

}