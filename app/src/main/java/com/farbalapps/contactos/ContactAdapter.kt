package com.farbalapps.contactos

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.farbalapps.contactos.databinding.ItemContactBinding
import com.farbalapps.contactos.model.ContactEntity
import java.io.ByteArrayInputStream


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
                try {
                    val bitmap = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.size)
                    val exif = ExifInterface(ByteArrayInputStream(photoBytes))
                    
                    val orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL
                    )
                    
                    val matrix = Matrix()
                    when (orientation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                        ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                    }
                    
                    val rotatedBitmap = if (matrix.isIdentity) {
                        bitmap
                    } else {
                        Bitmap.createBitmap(
                            bitmap, 0, 0,
                            bitmap.width, bitmap.height,
                            matrix, true
                        )
                    }
                    
                    binding.contactAvatar.setImageBitmap(rotatedBitmap)
                } catch (e: Exception) {
                    binding.contactAvatar.setImageResource(R.drawable.ic_person)
                }
            } ?: binding.contactAvatar.setImageResource(R.drawable.ic_person)
            
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