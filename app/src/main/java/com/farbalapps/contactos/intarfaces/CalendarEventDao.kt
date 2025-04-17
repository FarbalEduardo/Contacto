package com.farbalapps.contactos.intarfaces

import androidx.room.*
import com.farbalapps.contactos.CalendarEventEntity

@Dao
interface CalendarEventDao {
    @Insert
    suspend fun insertEvent(event: CalendarEventEntity)

    @Update
    suspend fun updateEvent(event: CalendarEventEntity)

    @Delete
    suspend fun deleteEvent(event: CalendarEventEntity)

    @Query("SELECT * FROM calendar_events WHERE date >= :startDate AND date < :endDate")
    suspend fun getEventsForDateRange(startDate: Long, endDate: Long): List<CalendarEventEntity>

    @Query("SELECT * FROM calendar_events WHERE contactId = :contactId")
    suspend fun getEventsForContact(contactId: Int): List<CalendarEventEntity>

    @Query("SELECT * FROM calendar_events WHERE date = :date")
    suspend fun getEventsForDate(date: Long): List<CalendarEventEntity>
}