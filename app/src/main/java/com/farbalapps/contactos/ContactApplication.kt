package com.farbalapps.contactos

import android.app.Application
import androidx.room.Room

class ContactApplication : Application() {

    companion object {

        lateinit var database: ContactDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            ContactDatabase::class.java,
            "contact_database"
        ).build()
    }
}