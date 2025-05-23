package com.farbalapps.contactos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.farbalapps.contactos.databinding.FragGroupBinding
import com.farbalapps.contactos.adapter.GroupAdapter
import com.farbalapps.contactos.model.ContactEntity
import com.farbalapps.contactos.adapter.GroupAdapter.Group
import com.farbalapps.contactos.ContactApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragGroup : Fragment() {
    private var _binding: FragGroupBinding? = null
    private val binding get() = _binding!!
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
        _binding = FragGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        groupAdapter = GroupAdapter()
        binding.groupsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = groupAdapter
        }
    }

    private fun loadGroups() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val contacts = ContactApplication.database.contactDao().getAllContacts()
                val groupedContacts = contacts.groupBy { it.contact_group }
                                                    .map { (groupName, contacts) ->
                    Group(name = groupName, contacts = contacts)
                }
                
                withContext(Dispatchers.Main) {
                    groupAdapter.updateGroups(groupedContacts)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Aquí podrías implementar un manejo de errores más sofisticado
            }
        }
    }

}