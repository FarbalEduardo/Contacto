package com.farbalapps.contactos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.farbalapps.contactos.ContactAdapter
import com.farbalapps.contactos.ContactApplication
import com.farbalapps.contactos.databinding.FragHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.app.Dialog
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import androidx.appcompat.app.AppCompatActivity
import com.farbalapps.contactos.model.ContactEntity
import com.farbalapps.contactos.databinding.DialogContactDetailBinding
import com.google.android.material.snackbar.Snackbar
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import com.farbalapps.contactos.R
import java.io.ByteArrayInputStream

class Frag_home : Fragment() {
    private lateinit var mBinding: FragHomeBinding
    private lateinit var mContactAdapter: ContactAdapter
    companion object {
        private const val EDIT_CONTACT_REQUEST = 2
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Contact"
        mBinding = FragHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchView()
        loadContacts()
    }

    private fun setupRecyclerView() {
        mContactAdapter = ContactAdapter(emptyList()) { contact ->
            showContactDialog(contact)
        }
        mBinding.rvContacts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mContactAdapter
        }
    }

    private fun showContactDialog(contact: ContactEntity) {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogContactDetailBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
    
        with(dialogBinding) {
            tvName.text = contact.name
            tvPhone.text = contact.phone
    
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
                    backgroundImage.setImageBitmap(rotatedBitmap)
                    ciProfileImage.setImageBitmap(rotatedBitmap)
                } catch (e: Exception) {
                    ciProfileImage.setImageResource(R.drawable.ic_person)
                    backgroundImage.setImageResource(R.drawable.ic_person)
                }
            } ?: ciProfileImage.setImageResource(R.drawable.ic_person)
    
            btnCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = "tel:${contact.phone}".toUri()
                }
                startActivity(intent)
                dialog.dismiss()
            }
    
            btnMessage.setOnClickListener {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "smsto:${contact.phone}".toUri()
                }
                startActivity(intent)
                dialog.dismiss()
            }
            btnEdit.setOnClickListener {
                val intent = Intent(requireContext(), CreateContacts::class.java).apply {
                    putExtra(CreateContacts.EXTRA_CONTACT_ID, contact.id)
                }

                startActivityForResult(intent, EDIT_CONTACT_REQUEST)
                dialog.dismiss()

            }

            btnDelete.setOnClickListener {
                deleteContact(contact)
                dialog.dismiss()
            }

            ivFavorite.apply {
                setImageResource(if (contact.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border)
                setOnClickListener {
                    val newFavoriteStatus = !contact.isFavorite
                    lifecycleScope.launch(Dispatchers.IO) {
                        ContactApplication.database.contactDao().updateFavoriteStatus(contact.id, newFavoriteStatus)
                        contact.isFavorite = newFavoriteStatus
                        withContext(Dispatchers.Main) {
                            setImageResource(if (newFavoriteStatus) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border)
                            loadContacts()
                        }
                    }
                }
            }
        }
    
        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        }
    
        dialog.show()
    }

    private fun deleteContact(contact: ContactEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            ContactApplication.database.contactDao().deleteContact(contact)
            withContext(Dispatchers.Main) {
                loadContacts()
                Snackbar.make(mBinding.root, "Contacto eliminado", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSearchView() {
        mBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterContacts(newText)
                return true
            }
        })
    }

    private fun filterContacts(query: String?) {
        lifecycleScope.launch(Dispatchers.IO) {
            val contacts = ContactApplication.database.contactDao().getAllContacts()
            val filteredContacts = if (query.isNullOrEmpty()) {
                contacts
            } else {
                contacts.filter { contact ->
                    contact.name.contains(query, true) ||
                            contact.phone.contains(query, true)
                }
            }
            withContext(Dispatchers.Main) {
                mContactAdapter.updateContacts(filteredContacts)
            }
        }
    }

    fun loadContacts() {
        lifecycleScope.launch(Dispatchers.IO) {
            val contacts = ContactApplication.database.contactDao().getAllContacts()
            withContext(Dispatchers.Main) {
                mContactAdapter.updateContacts(contacts)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_CONTACT_REQUEST && resultCode == Activity.RESULT_OK) {
            loadContacts()
        }
    }
}