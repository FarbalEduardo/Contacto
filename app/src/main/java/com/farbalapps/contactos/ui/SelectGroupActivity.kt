package com.farbalapps.contactos.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.farbalapps.contactos.ContactApplication
import com.farbalapps.contactos.R
import com.farbalapps.contactos.databinding.ActSelectGroupBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.farbalapps.contactos.databinding.DialogCreateGroupBinding

class SelectGroupActivity : AppCompatActivity() {
    private lateinit var binding: ActSelectGroupBinding
    private var currentGroupFromContact: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActSelectGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Unificamos a "Not assigned" con N mayúscula
        currentGroupFromContact = intent.getStringExtra("current_group") ?: "Not assigned"

        loadExistingGroups()
        setupButtons()
    }

    // AGREGADO: Listener para el botón de crear grupo
    private fun setupButtons() {
        binding.btnBack.setOnClickListener { finish() }

        binding.createGroup.setOnClickListener {
            showCreateGroupDialog()
        }

        // AGREGADO: Listener para detectar selección en la lista
        binding.groupRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedRb = group.findViewById<RadioButton>(checkedId)
            // isPressed asegura que fue un toque real del usuario
            if (selectedRb != null && selectedRb.isPressed) {
                returnResult(selectedRb.text.toString())
            }
        }
    }

    // AGREGADO: Cargar grupos existentes
    private fun loadExistingGroups() {
        lifecycleScope.launch(Dispatchers.IO) {
            val contacts = ContactApplication.database.contactDao().getAllContacts()
            val dynamicGroups = contacts.mapNotNull { it.contact_group }
                .filter { it.isNotBlank() && it != "Not assigned" && it != "Emergency contacts" }
                .distinct()

            withContext(Dispatchers.Main) {
                binding.groupRadioGroup.removeAllViews()
                addRadioButtonToGroup("Not assigned")
                addRadioButtonToGroup("Emergency contacts")
                dynamicGroups.forEach { addRadioButtonToGroup(it) }
            }
        }
    }

    // AGREGADO: Agregar RadioButton a la lista de grupos
    private fun addRadioButtonToGroup(name: String) {
        val rb = RadioButton(this).apply {
            id = View.generateViewId()
            text = name
            textSize = 16f
            setPadding(24, 40, 24, 40)
            layoutParams = RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
            )
            // Comparación segura ignorando mayúsculas
            if (name.equals(currentGroupFromContact, ignoreCase = true)) {
                isChecked = true
            }
        }
        binding.groupRadioGroup.addView(rb)
    }

    // AGREGADO: Mostrar diálogo para crear nuevo grupo con binding
    private fun showCreateGroupDialog() {
        // 1. Inflar el binding del diálogo
        val dialogBinding = DialogCreateGroupBinding.inflate(layoutInflater)

        // 2. Crear el diálogo usando el binding
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Create new group")
            .setView(dialogBinding.root)
            .setPositiveButton("Create", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()

        // 3. Configurar el botón de acción
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val newGroupName = dialogBinding.editGroupName.text.toString().trim()

            if (newGroupName.isNotEmpty()) {
                returnResult(newGroupName)
                dialog.dismiss()
            } else {
                dialogBinding.editGroupName.error = "Name cannot be empty"
            }
        }
    }

    // AGREGADO: Devolver el nombre del grupo seleccionado
    private fun returnResult(name: String) {
        val intent = Intent().apply { putExtra("selected_group", name) }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}