package com.example.onetoone.models

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginModel(val userName: String,val email: String,val password: String,val phoneNo: String) : Parcelable
