package com.example.onetoone.models

data class RoomModel(
    val receiverMessage: List<ChatRoom>? = null,
    val senderMessage: List<ChatRoom>? = null
)