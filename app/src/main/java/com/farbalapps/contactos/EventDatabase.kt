package com.farbalapps.contactos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.farbalapps.contactos.intarfaces.EventDao
import com.farbalapps.contactos.model.EventEntity

@Database(entities = [EventEntity::class], version = 1)
abstract class EventDatabase  : RoomDatabase() {
    abstract fun eventDao(): EventDao
    companion object {
        @Volatile
        private var INSTANCE: EventDatabase? = null

        fun getDatabase (context: Context): EventDatabase{

            return INSTANCE?: synchronized (this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EventDatabase::class.java,
                    "event_database"
                ).build()
                INSTANCE = instance
                instance

            }
        }
    }




}