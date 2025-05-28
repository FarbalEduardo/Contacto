package com.farbalapps.contactos.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.farbalapps.contactos.R
import com.farbalapps.contactos.databinding.ActSelectGroupBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SelectGroupActivity : AppCompatActivity() {
    private lateinit var binding: ActSelectGroupBinding
    private val groups = mutableListOf("Not assigned", "Emergency contacts")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActSelectGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupGroupList()
        setupCreateGroup()
    }

    private fun setupGroupList() {
        binding.groupRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            val selectedGroup = radioButton.text.toString()
            returnResult(selectedGroup)
        }
    }

    private fun setupCreateGroup() {
        binding.createGroup.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(this)
                .setTitle("Create new group")
                .setView(R.layout.dialog_create_group)
                .setPositiveButton("Create", null)
                .setNegativeButton("Cancel", null)
                .show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                setTextColor(resources.getColor(R.color.primary, theme))
                setOnClickListener {
                    val editText = dialog.findViewById<EditText>(R.id.edit_group_name)
                    val newGroupName = editText?.text?.toString() ?: ""
                    if (newGroupName.isNotEmpty()) {
                        groups.add(newGroupName)
                        val radioButton = RadioButton(this@SelectGroupActivity).apply {
                            text = newGroupName
                            layoutParams = RadioGroup.LayoutParams(
                                RadioGroup.LayoutParams.MATCH_PARENT,
                                RadioGroup.LayoutParams.WRAP_CONTENT
                            )
                            setPadding(32, 32, 32, 32)
                        }
                        binding.groupRadioGroup.addView(radioButton)
                        radioButton.isChecked = true
                        returnResult(newGroupName)
                        dialog.dismiss()
                    }
                }
            }

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).apply {
                setTextColor(resources.getColor(R.color.primary, theme))
            }

            dialog.window?.setBackgroundDrawableResource(R.color.white)
        }
    }

    private fun returnResult(selectedGroup: String) {
        val intent = Intent().apply {
            putExtra("selected_group", selectedGroup)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}


