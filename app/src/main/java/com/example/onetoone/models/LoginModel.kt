package com.example.onetoone.models

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginModel(
    val userName: String? = null ,
    val email: String? = null,
    val password: String? = null,
    val phoneNo: String? = null
) : Parcelable
