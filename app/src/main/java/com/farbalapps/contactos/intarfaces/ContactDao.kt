package com.farbalapps.contactos.intarfaces

import androidx.room.*
import com.farbalapps.contactos.model.ContactEntity

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactEntity)

    @Update
    suspend fun updateContact(contact: ContactEntity)

    @Delete
    suspend fun deleteContact(contact: ContactEntity)

    @Query("SELECT * FROM contacts ORDER BY name ASC")
    suspend fun getAllContacts(): List<ContactEntity>

    @Query("SELECT * FROM contacts WHERE is_favorite = 1 ORDER BY name ASC")
    suspend fun getFavoriteContacts(): List<ContactEntity>

    @Query("UPDATE contacts SET is_favorite = :isFavorite WHERE id = :contactId")
    suspend fun updateFavoriteStatus(contactId: Long, isFavorite: Boolean)

    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getContactById(id: Int): ContactEntity

    @Query("SELECT * FROM contacts WHERE name LIKE '%' || :searchQuery || '%' OR phone LIKE '%' || :searchQuery || '%'")
    suspend fun searchContacts(searchQuery: String): List<ContactEntity>

    @Query("DELETE FROM contacts")
    suspend fun deleteAllContacts()

    @Query("UPDATE contacts SET contact_group = '' WHERE contact_group = :groupName")
    suspend fun removeGroup(groupName: String)

    @Query("UPDATE contacts SET contact_group = 'Not Assigned' WHERE contact_group = :groupName")
    suspend fun moveContactsToNoAssigned(groupName: String)
}