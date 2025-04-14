package com.farbalapps.contactos

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.farbalapps.contactos.databinding.ItemContactBinding


class ContactAdapter(
    private var contacts: List<ContactEntity>,
    private val onContactClick: (ContactEntity) -> Unit = {}
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemContactBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(contact: ContactEntity) {
            binding.contactName.text = contact.name
            binding.contactPhone.text = contact.phone
            
            contact.photo?.let { photoBytes ->
                val bitmap = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.size)
                binding.contactAvatar.setImageBitmap(bitmap)
            } ?: binding.contactAvatar.setImageResource(R.drawable.ic_person) // imagen por defecto
            
            binding.root.setOnClickListener {
                onContactClick(contact)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContactBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount(): Int = contacts.size

    fun updateContacts(newContacts: List<ContactEntity>) {
        contacts = newContacts
        notifyDataSetChanged()
    }
}