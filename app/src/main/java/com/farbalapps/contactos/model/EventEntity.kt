package com.farbalapps.contactos.model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val date: Long,
    val startTime: String,
    val endTime: String,
    val isAllDay: Boolean =false

)