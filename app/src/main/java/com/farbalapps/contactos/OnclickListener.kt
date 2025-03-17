package com.farbalapps.contactos

interface OnclickListener {

    fun onClick(contactID: Long)
    fun onCallClick(contactID: Long)
    fun onMessageClick(contactID: Long)

}