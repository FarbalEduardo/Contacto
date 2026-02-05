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
import androidx.core.graphics.scale

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

        editContact()


    }

    private fun editContact(){
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
        var group = mBinding.editSelectGroup.text.toString()
        // Si no se selecciona un grupo, asignar "Not assigned" por defecto
        if (group.isEmpty()) {
            group = "Not assigned"
        }
        val workInfo = mBinding.editWorkInfo.text.toString()
        val workPhone = mBinding.editWorkPhone.text.toString()
        val workEmail = mBinding.editWorkEmail.text.toString()

        if (validateFields(name, phone,group)) {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val imageBytes = selectedImageUri?.let { uri ->
                        processImage(uri)
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
    private fun processImage(uri: Uri): ByteArray? {
        return try {
            // 1. Abrir el InputStream para leer la imagen
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val originalBitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            if (originalBitmap == null) return null

            // 2. LEER LA ROTACIÓN (EXIF)
            // Necesitamos abrir un nuevo stream para no interferir con el anterior
            val exifInputStream = contentResolver.openInputStream(uri)
            val orientation = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                val exif = android.media.ExifInterface(exifInputStream!!)
                exif.getAttributeInt(android.media.ExifInterface.TAG_ORIENTATION, android.media.ExifInterface.ORIENTATION_NORMAL)
            } else {
                // Para versiones antiguas si fuera necesario
                android.media.ExifInterface.ORIENTATION_NORMAL
            }
            exifInputStream?.close()

            // 3. CORREGIR LA ROTACIÓN
            val matrix = android.graphics.Matrix()
            when (orientation) {
                android.media.ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                android.media.ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                android.media.ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            }

            // Crear el bitmap rotado
            val rotatedBitmap = android.graphics.Bitmap.createBitmap(
                originalBitmap, 0, 0, originalBitmap.width, originalBitmap.height, matrix, true
            )

            // 4. REDIMENSIONAR (Máximo 500px)
            val maxSize = 500
            val width = rotatedBitmap.width
            val height = rotatedBitmap.height
            val ratio = width.toFloat() / height.toFloat()

            val finalWidth: Int
            val finalHeight: Int
            if (width > height) {
                finalWidth = maxSize
                finalHeight = (maxSize / ratio).toInt()
            } else {
                finalHeight = maxSize
                finalWidth = (maxSize * ratio).toInt()
            }

            val scaledBitmap = android.graphics.Bitmap.createScaledBitmap(rotatedBitmap, finalWidth, finalHeight, true)

            // 5. COMPRIMIR
            val outputStream = java.io.ByteArrayOutputStream()
            scaledBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 70, outputStream)

            outputStream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    private fun validateFields(name: String, phone: String, group: String): Boolean {
        if (name.isEmpty()) {
            mBinding.editName.error = "El nombre es requerido"
            return false
        }
        if (phone.isEmpty()) {
            mBinding.editPhone.error = "El teléfono es requerido"
            return false
        }
        // El grupo puede estar vacío, se asignará "Not assigned" automáticamente
        return true
    }
}
