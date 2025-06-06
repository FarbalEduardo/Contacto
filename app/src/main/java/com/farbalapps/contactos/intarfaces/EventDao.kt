package com.farbalapps.contactos.intarfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.farbalapps.contactos.model.EventEntity
@Dao
interface EventDao {
    @Query("SELECT * FROM events WHERE date >= :startDate AND date < :endDate")
    suspend fun getEventsForDay(startDate: Long, endDate: Long): List<EventEntity>

    @Insert
    suspend fun insertEvent(event: EventEntity)

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Delete
    suspend fun deleteEvent(event: EventEntity)

    }