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


class ContactAdapter(private var contacts: List<ContactEntity>, private val listener: OnclickListener):
    RecyclerView.Adapter<ContactAdapter.ViewHolder>()     {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapter.ViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_contact, parent, false)
        return ViewHolder(view)


    }

    override fun onBindViewHolder(holder: ContactAdapter.ViewHolder, position: Int) {

        val contact =contacts[position]
        with ( holder){
            setListener(contact)

            binding.tvnameContact.text = contact.name
            binding.itemContactos
            contact.photo?.let { photoId ->
                Glide.with(binding.root.context)
                    .load(photoId) // Carga la imagen del ID de recurso
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .centerCrop() // ajusta la imagen al centro
                    .into(binding.shimageContact) // Asigna la imagen al ShapeableImageView
            } ?: run {
                // Si no hay foto, asigna una por defecto.
                Glide.with(binding.root.context)
                    .load(R.drawable.ic_call) // Utiliza una imagen por defecto
                    .into(binding.shimageContact) // Asigna la imagen al ShapeableImageView
            }
        }

    }

    override fun getItemCount(): Int =contacts.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val binding= ItemContactBinding.bind(itemView)

        fun setListener(contactEntity: ContactEntity){
            binding.root.setOnClickListener {
               // listener.onClick(contactEntity)
            }

        }
    }
}