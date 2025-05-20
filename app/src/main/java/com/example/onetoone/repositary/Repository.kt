package com.example.onetoone.repositary

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class Repository @Inject constructor(val context: Context,val auth: FirebaseAuth) {

    val _registationMutableLiveData = MutableLiveData<Response<Boolean>>()
    val registationLiveData : LiveData<Response<Boolean>>
        get() = _registationMutableLiveData

    val _loginMutableLiveData = MutableLiveData<Response<Boolean>>()
    val loginLiveData : LiveData<Response<Boolean>>
        get() = _loginMutableLiveData


    suspend fun loginUser(email: String, password: String) {

        _loginMutableLiveData.postValue(Response.Loading(null))

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
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
                    _registationMutableLiveData.postValue(Response.Success(it.isSuccessful))
                    Toast.makeText(context, "Registered successfully", Toast.LENGTH_SHORT).show()
                } else {
                    _registationMutableLiveData.postValue(Response.Error(it.exception?.message.toString()))
                    Toast.makeText(context, "Registration failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}