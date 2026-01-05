package com.farbalapps.contactos

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.farbalapps.contactos.databinding.ItemContactBinding
import com.farbalapps.contactos.model.ContactEntity
import java.io.ByteArrayInputStream


class ContactAdapter(
    private val onContactClick: (ContactEntity) -> Unit = {}
) : ListAdapter<ContactEntity, ContactAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ContactEntity>() {
            override fun areItemsTheSame(oldItem: ContactEntity, newItem: ContactEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ContactEntity, newItem: ContactEntity): Boolean =
                oldItem == newItem
        }
    }

    inner class ViewHolder(private val binding: ItemContactBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(contact: ContactEntity) {
            binding.contactName.text = contact.name
            binding.contactPhone.text = contact.phone
            
            contact.photo?.let { photoBytes ->
                Glide.with(binding.contactAvatar)
                    .load(photoBytes)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(RequestOptions().centerCrop())
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(binding.contactAvatar)
            } ?: run {
                binding.contactAvatar.setImageResource(R.drawable.ic_person)
            }
            
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
        holder.bind(getItem(position))
    }

    override fun getItemId(position: Int): Long = getItem(position).id
}
