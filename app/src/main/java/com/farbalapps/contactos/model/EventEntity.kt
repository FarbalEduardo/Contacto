package com.farbalapps.contactos.model

data class EventEntity(
    val id: Long = 0,
    val title: String,
    val description: String,
    val date: Long,
    val time: String
)