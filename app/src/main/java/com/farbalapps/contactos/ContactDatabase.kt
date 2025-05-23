package com.farbalapps.contactos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.farbalapps.contactos.intarfaces.ContactDao
import com.farbalapps.contactos.model.ContactEntity

@Database(entities = [ContactEntity::class], version = 2)
abstract class ContactDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    companion object {
        @Volatile
        private var INSTANCE: ContactDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Crear tabla temporal
                database.execSQL(
                    "CREATE TABLE contacts_temp (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "name TEXT NOT NULL, " +
                    "nickname TEXT NOT NULL DEFAULT 'N/A', " +
                    "phone TEXT NOT NULL, " +
                    "photo BLOB, " +
                    "email TEXT NOT NULL DEFAULT '', " +
                    "contact_group TEXT NOT NULL DEFAULT 'No group', " +
                    "work_info TEXT NOT NULL DEFAULT 'Not specified', " +
                    "work_phone TEXT NOT NULL DEFAULT 'Not available', " +
                    "work_email TEXT NOT NULL DEFAULT 'Not available')"
                )
                
                // Copiar datos
                database.execSQL(
                    "INSERT INTO contacts_temp (id, name, nickname, phone, email, contact_group, " +
                    "work_info, work_phone, work_email) " +
                    "SELECT id, name, nickname, phone, email, contact_group, " +
                    "work_info, work_phone, work_email FROM contacts"
                )
                
                // Eliminar tabla antigua
                database.execSQL("DROP TABLE contacts")
                
                // Renombrar tabla temporal
                database.execSQL("ALTER TABLE contacts_temp RENAME TO contacts")
            }
        }

        fun getDatabase(context: Context): ContactDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContactDatabase::class.java,
                    "contact_database"
                )
                .addMigrations(MIGRATION_1_2)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}