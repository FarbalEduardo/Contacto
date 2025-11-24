package com.farbalapps.contactos

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room

class ContactApplication : Application() {
    companion object {
        lateinit var database: ContactDatabase
    }

    override fun onCreate() {
        super.onCreate()
        // Aplicar modo de tema preferido
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val mode = prefs.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(mode)
        database = Room.databaseBuilder(
            this,
            ContactDatabase::class.java,
            "contact_database"
        ).build()
    }
}