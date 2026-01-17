package com.farbalapps.contactos.ui

import android.os.Bundle
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.farbalapps.contactos.databinding.FragGroupBinding
import com.farbalapps.contactos.adapter.GroupAdapter
import com.farbalapps.contactos.adapter.GroupAdapter.Group
import com.farbalapps.contactos.ContactApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragGroup : Fragment() {
    private var mBinding: FragGroupBinding? = null
    private val binding get() = mBinding!!
    private lateinit var groupAdapter: GroupAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Groups"
        
        setupRecyclerView()
        loadGroups()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

    private fun setupRecyclerView() {
        groupAdapter = GroupAdapter { groupName ->
            showDeleteConfirmationDialog(groupName)
        }
        binding.groupsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = groupAdapter
        }
    }

    private fun loadGroups() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val allContacts = ContactApplication.database.contactDao().getAllContacts()

                // 1. Definimos los nombres exactos de tus grupos fijos
                val fixedGroupNames = listOf("Emergency contacts", "Not assigned")

                // 2. Agrupamos los contactos que vienen de la DB
                val groupedMap = allContacts.groupBy { it.contact_group }

                // 3. Creamos la lista final
                val finalGroups = mutableListOf<GroupAdapter.Group>()

                // Agregamos los FIJOS primero (estén vacíos o no)
                fixedGroupNames.forEach { name ->
                    finalGroups.add(GroupAdapter.Group(
                        name = name,
                        contacts = groupedMap[name] ?: emptyList()
                    ))
                }

                // Agregamos los demás grupos CREADOS por el usuario
                // Filtramos para no repetir los fijos y no agregar grupos con nombres vacíos o "No group"
                groupedMap.forEach { (name, contacts) ->
                    if (name !in fixedGroupNames && name.isNotBlank() && name != "No group") {
                        finalGroups.add(GroupAdapter.Group(name = name, contacts = contacts))
                    }
                }

                withContext(Dispatchers.Main) {
                    groupAdapter.updateGroups(finalGroups)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showDeleteConfirmationDialog(groupName: String) {
        val fixedGroups = listOf("No Emergency Contacts", "No Assigned")

        if (groupName in fixedGroups) {
            // Opcional: Mostrar un mensaje diciendo que no se puede borrar
            return
        }

        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Grupo")
            .setMessage("¿Estás seguro de que deseas eliminar el grupo '$groupName'?")
            .setPositiveButton("Eliminar") { _, _ ->
                deleteGroup(groupName)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteGroup(groupName: String) {
        // Usamos lifecycleScope si estás en un Activity/Fragment

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // 1. Eliminación en hilo de fondo
                ContactApplication.database.contactDao().removeGroup(groupName)

                // 2. Volvemos al hilo principal para actualizar la UI
                withContext(Dispatchers.Main) {
                    loadGroups()
                    // Opcional: Toast.makeText(context, "Eliminado", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    // Informar al usuario si hubo un error
                }
            }
        }
    }

//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                ContactApplication.database.contactDao().removeGroup(groupName)
//                loadGroups()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }


}