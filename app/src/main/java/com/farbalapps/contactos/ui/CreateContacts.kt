package com.farbalapps.contactos.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.farbalapps.contactos.ContactApplication
import com.farbalapps.contactos.model.ContactEntity
import com.farbalapps.contactos.R
import com.farbalapps.contactos.databinding.ActCreateContactsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.view.isVisible

class CreateContacts : AppCompatActivity() {
    private lateinit var mBinding: ActCreateContactsBinding
    private var selectedImageUri: Uri? = null
    private var isEditMode: Boolean = false
    private var editingContactId: Long? = null
    private var existingPhotoBytes: ByteArray? = null

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val SELECT_GROUP_REQUEST = 2
        const val EXTRA_CONTACT_ID = "extra_contact_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActCreateContactsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupButtons()
        setupImagePicker()
        mBinding.editSelectGroup.setOnClickListener {
            val intent = Intent(this, SelectGroupActivity::class.java)
            startActivityForResult(intent, SELECT_GROUP_REQUEST)
        }

        // Si viene en modo edición, cargar datos del contacto
        val contactId = intent.getLongExtra(EXTRA_CONTACT_ID, -1L)
        if (contactId > 0L) {
            isEditMode = true
            editingContactId = contactId
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val contact = ContactApplication.database.contactDao().getContactById(contactId.toInt())
                    withContext(Dispatchers.Main) {
                        // Rellenar los campos
                        mBinding.editName.setText(contact.name)
                        mBinding.editPhone.setText(contact.phone)
                        mBinding.editEmail.setText(contact.email)
                        mBinding.editSelectGroup.setText(contact.contact_group)
                        mBinding.editWorkInfo.setText(contact.workInfo)
                        mBinding.editWorkPhone.setText(contact.workPhone)
                        mBinding.editWorkEmail.setText(contact.workEmail)

                        existingPhotoBytes = contact.photo
                        contact.photo?.let { bytes ->
                            try {
                                mBinding.ciProfileImage.setImageBitmap(
                                    android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                )
                            } catch (_: Exception) {
                                // ignorar si falla render del bitmap
                            }
                        }

                        // Ajustar texto del botón guardar en modo edición
                        mBinding.btnSaveContact.text = "Actualizar"
                    }
                } catch (_: Exception) {
                    withContext(Dispatchers.Main) {
                        Snackbar.make(mBinding.root, "No se pudo cargar el contacto", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
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
        when (requestCode) {
            PICK_IMAGE_REQUEST -> {
                if (resultCode == RESULT_OK && data != null) {
                    selectedImageUri = data.data
                    mBinding.ciProfileImage.setImageURI(selectedImageUri)
                }
            }
            SELECT_GROUP_REQUEST -> {
                if (resultCode == RESULT_OK && data != null) {
                    val selectedGroup = data.getStringExtra("selected_group") ?: ""
                    mBinding.editSelectGroup.setText(selectedGroup)
                }
            }
        }
    }

    private fun setupButtons() {
        mBinding.btnSaveContact.setOnClickListener {
            saveContact()
        }

        mBinding.btnCancel.setOnClickListener {
            finish()
        }

        mBinding.btnExpandWorkInfo.setOnClickListener {
            val isExpanded = mBinding.containerWorkInfo.isVisible
            mBinding.containerWorkInfo.visibility = if (isExpanded) View.GONE else View.VISIBLE
            mBinding.btnExpandWorkInfo.setCompoundDrawablesWithIntrinsicBounds(
                0, 0, 0, if (isExpanded) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up
            )
            mBinding.btnExpandWorkInfo.text = if (isExpanded)
                "View more" else "Hide"
        }
    }

    private fun saveContact() {
        val name = mBinding.editName.text.toString()
        val phone = mBinding.editPhone.text.toString()
        val email = mBinding.editEmail.text.toString()
      //  val nickname = mBinding.editNickname.text.toString()
        val group = mBinding.editSelectGroup.text.toString()
        val workInfo = mBinding.editWorkInfo.text.toString()
        val workPhone = mBinding.editWorkPhone.text.toString()
        val workEmail = mBinding.editWorkEmail.text.toString()

        if (validateFields(name, phone)) {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val imageBytes = selectedImageUri?.let { uri ->
                        contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    } ?: existingPhotoBytes

                    val contact = ContactEntity(
                        id = if (isEditMode) (editingContactId ?: 0L) else 0L,
                        name = name,
                      //  nickname = nickname,
                        phone = phone,
                        photo = imageBytes,  // ByteArray de la imagen
                        email = email,
                        contact_group = group,
                        workInfo = workInfo,
                        workPhone = workPhone,
                        workEmail = workEmail
                    )

                    if (isEditMode) {
                        ContactApplication.database.contactDao().updateContact(contact)
                    } else {
                        ContactApplication.database.contactDao().insertContact(contact)
                    }
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