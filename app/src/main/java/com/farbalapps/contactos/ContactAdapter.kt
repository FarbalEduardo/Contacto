package com.farbalapps.contactos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.farbalapps.contactos.databinding.ItemContactBinding


class ContactAdapter(private var contacts: List<ContactEntity>) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemContactBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(contact: ContactEntity) {
            binding.contactName.text = contact.name
            binding.contactPhone.text = contact.phone
            // Si tienes una imagen, puedes cargarla aquí
            contact.photo?.let { photoResId ->
                binding.contactAvatar.setImageResource(photoResId)
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