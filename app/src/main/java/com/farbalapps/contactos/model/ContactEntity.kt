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
        if (name != other.name) return false
        if (nickname != other.nickname) return false
        if (phone != other.phone) return false
        if (photo != null) {
            if (other.photo == null) return false
            if (!photo.contentEquals(other.photo)) return false
        } else if (other.photo != null) return false
        if (email != other.email) return false
        if (contact_group != other.contact_group) return false
        if (workInfo != other.workInfo) return false
        if (workPhone != other.workPhone) return false
        if (workEmail != other.workEmail) return false
        if (isFavorite != other.isFavorite) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + nickname.hashCode()
        result = 31 * result + phone.hashCode()
        result = 31 * result + (photo?.contentHashCode() ?: 0)
        result = 31 * result + email.hashCode()
        result = 31 * result + contact_group.hashCode()
        result = 31 * result + workInfo.hashCode()
        result = 31 * result + workPhone.hashCode()
        result = 31 * result + workEmail.hashCode()
        result = 31 * result + isFavorite.hashCode()
        return result
    }
}