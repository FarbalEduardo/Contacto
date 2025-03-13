package com.farbalapps.contactos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "nickname", defaultValue = "N/A") var nickname: String = "",
    @ColumnInfo(index = true) var phone: String,
    var photo: Int?,  // No es necesario @ColumnInfo(name = "photo")
    @ColumnInfo(name = "email", index = true) var email: String = "",
    @ColumnInfo(name = "contact_group", defaultValue = "No group") var contact_group: String = "No group",
    @ColumnInfo(name = "work_info", defaultValue = "Not specified") var workInfo: String = "",
    @ColumnInfo(name = "work_phone", defaultValue = "Not available") var workPhone: String = "",
    @ColumnInfo(name = "work_email", defaultValue = "Not available") var workEmail: String = ""
)
