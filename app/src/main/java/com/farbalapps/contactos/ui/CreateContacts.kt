package com.farbalapps.contactos.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.farbalapps.contactos.ContactApplication
import com.farbalapps.contactos.ContactEntity
import com.farbalapps.contactos.databinding.ActCreateContactsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateContacts : AppCompatActivity() {
    private lateinit var mBinding: ActCreateContactsBinding
    private var selectedImageUri: Uri? = null

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActCreateContactsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupButtons()
        setupImagePicker()
    }

    private fun setupImagePicker() {
        mBinding.ciProfileImage.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            mBinding.ciProfileImage.setImageURI(selectedImageUri)
        }
    }

    private fun setupButtons() {
        mBinding.btnSaveContact.setOnClickListener {
            saveContact()
        }

        mBinding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun saveContact() {
        val name = mBinding.editName.text.toString()
        val phone = mBinding.editPhone.text.toString()
        val email = mBinding.editEmail.text.toString()
        val nickname = mBinding.editNickname.text.toString()
        val workInfo = mBinding.editWorkInfo.text.toString()
        val workPhone = mBinding.editWorkPhone.text.toString()
        val workEmail = mBinding.editWorkEmail.text.toString()

        if (validateFields(name, phone)) {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val imageBytes = selectedImageUri?.let { uri ->
                        contentResolver.openInputStream(uri)?.use {
                            it.readBytes()
                        }
                    }

                    val contact = ContactEntity(
                        id = 0L,  // Cambiado a 0L para Long
                        name = name,
                        nickname = nickname,
                        phone = phone,
                        photo = imageBytes,  // ByteArray de la imagen
                        email = email,
                        contact_group = "No group",
                        workInfo = workInfo,
                        workPhone = workPhone,
                        workEmail = workEmail
                    )

                    ContactApplication.database.contactDao().insertContact(contact)
                    withContext(Dispatchers.Main) {
                        setResult(RESULT_OK)
                        finish()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Snackbar.make(mBinding.root, "Error al guardar contacto", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun validateFields(name: String, phone: String): Boolean {
        if (name.isEmpty()) {
            mBinding.editName.error = "El nombre es requerido"
            return false
        }
        if (phone.isEmpty()) {
            mBinding.editPhone.error = "El teléfono es requerido"
            return false
        }
        return true
    }
}