package com.example.onetoone.models

data class UserData(
    val userID: String? = null,
    val userName: String? = null ,
    val email: String? = null,
    val password: String? = null,
    val phoneNo: String? = null,
    val rooms: Map<String, Messages>? = null
)
