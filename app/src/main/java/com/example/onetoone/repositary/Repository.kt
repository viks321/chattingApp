package com.example.onetoone.repositary

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.onetoone.dataStoreforSaveUserPref.UserDataPref
import com.example.onetoone.models.LoginModel
import com.example.onetoone.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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

    suspend fun registerUser(email: String, password: String) {

        _registationMutableLiveData.postValue(Response.Loading(null))
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _registationMutableLiveData.postValue(Response.Success(Users(it.result.user?.uid,it.result.user?.email)))
                    CoroutineScope(Dispatchers.IO).launch {
                        userDataPref.saveUserId(it.result.user?.uid!!)
                        userDataPref.saveIslogin(true)
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
        val loginModel = LoginModel(userName,email,password,phone)
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

    suspend fun createChatRoomFirebase(){
        val userRef = firebaseDatabase.getReference("allMembers")
        val chatroomID = UUID.randomUUID().toString()
        userRef.child("romms").child(chatroomID).setValue("data")
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