package com.example.onetoone.repositary

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class Repository @Inject constructor(val context: Context,val auth: FirebaseAuth) {

    val _registationMutableLiveData = MutableLiveData<Boolean>()
    val registationLiveData : LiveData<Boolean>
        get() = _registationMutableLiveData

    val _loginMutableLiveData = MutableLiveData<Boolean>()
    val loginLiveData : LiveData<Boolean>
        get() = _loginMutableLiveData


    suspend fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    //mutableLiveData.value = it.exception?.message
                    //CallActivity.launch(context,HomeActivity::class.java)
                    _loginMutableLiveData.value = it.isSuccessful
                    Toast.makeText(context, "Login successfully", Toast.LENGTH_SHORT).show()
                } else {
                    _loginMutableLiveData.value = it.isSuccessful
                    Toast.makeText(context, "Login failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    suspend fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _registationMutableLiveData.value = it.isSuccessful
                    Toast.makeText(context, "Registered successfully", Toast.LENGTH_SHORT).show()
                } else {
                    _registationMutableLiveData.value = it.isSuccessful
                    Toast.makeText(context, "Registration failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}