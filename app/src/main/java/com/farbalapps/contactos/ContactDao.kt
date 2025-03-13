package com.farbalapps.contactos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ContactDao {

        @Insert
        fun insertContact(contact: ContactEntity)

        @Update
        fun updateContact(contact: ContactEntity)

        @Delete
        fun deleteContact(contact: ContactEntity)

        @Query("SELECT * FROM contacts")
        fun getAllContacts(): List<ContactEntity>

        @Query("SELECT * FROM contacts WHERE id = :id")
        fun getContactById(id: Int): ContactEntity

}