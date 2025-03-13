package com.farbalapps.contactos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(private var contacts: List<ContactEntity>, private val listener: OnclickListener):
    RecyclerView.Adapter<ContactAdapter.ViewHolder>()     {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapter.ViewHolder {

        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_contact, parent, false)
        return ViewHolder(view)


    }

    override fun onBindViewHolder(holder: ContactAdapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }
}