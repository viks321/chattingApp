package com.example.onetoone.models

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginModel(val email: String,val password: String) : Parcelable
