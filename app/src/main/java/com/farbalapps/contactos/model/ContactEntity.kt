package com.farbalapps.contactos.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "nickname", defaultValue = "N/A") var nickname: String = "",
    @ColumnInfo(index = true) var phone: String,
    @ColumnInfo(typeAffinity = ColumnInfo.Companion.BLOB)
    var photo: ByteArray?,
    @ColumnInfo(name = "email", index = true) var email: String = "",
    @ColumnInfo(name = "contact_group", defaultValue = "No group") var contact_group: String = "No group",
    @ColumnInfo(name = "work_info", defaultValue = "Not specified") var workInfo: String = "",
    @ColumnInfo(name = "work_phone", defaultValue = "Not available") var workPhone: String = "",
    @ColumnInfo(name = "work_email", defaultValue = "Not available") var workEmail: String = "",
    @ColumnInfo(name = "is_favorite", defaultValue = "0") var isFavorite: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContactEntity
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}