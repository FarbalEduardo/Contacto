package com.farbalapps.contactos

import androidx.room.Database
import androidx.room.RoomDatabase
import com.farbalapps.contactos.intarfaces.ContactDao

@Database (entities = [ContactEntity::class], version =1)
abstract class ContactDatabase : RoomDatabase () {
    abstract fun contactDao(): ContactDao

}