package com.example.onetoone.models

data class RoomModel(
    val receiverMessage: Map<String, ChatRoom>? = null,
    val senderMessage: Map<String, ChatRoom>? = null
)