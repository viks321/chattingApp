package com.example.onetoone.models

data class Message(
    val senderMessage: ChatRoom? = null,
    val receiverMessage: ChatRoom? = null
)
