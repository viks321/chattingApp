package com.example.onetoone.models

data class Messages(
    val userID: String? = null,
    val userName: String? = null ,
    val lastMessage: String? = null,
    val lastMessageTime: String? = null,
    val messageCount: Int? = null,
    val isActive: Int? = null,
    val messages: Map<String, Message>? = null
)
