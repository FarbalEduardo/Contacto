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

class Frag_home : Fragment() {
    private lateinit var mBinding: FragHomeBinding
    private lateinit var mContactAdapter: ContactAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mBinding = FragHomeBinding.inflate(inflater, container, false)


        return mBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inicializar el adaptador primero
        mContactAdapter = ContactAdapter(emptyList())
        setupRecyclerView()
        setupSearchView()
        loadContacts()
    }

    private fun setupRecyclerView() {
        mBinding.rvContacts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mContactAdapter
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
            val contacts = ContactApplication.Companion.database.contactDao().getAllContacts()
            val filteredContacts = if (query.isNullOrEmpty()) {
                contacts
            } else {
                contacts.filter { contact ->
                    contact.name.contains(query, true) ||
                            contact.phone.contains(query, true)
                }
            }
            withContext(Dispatchers.Main) {
                mContactAdapter = ContactAdapter(filteredContacts)
                mBinding.rvContacts.adapter = mContactAdapter
            }
        }
    }

    private fun loadContacts() {
        lifecycleScope.launch(Dispatchers.IO) {
            val contacts = ContactApplication.Companion.database.contactDao().getAllContacts()
            withContext(Dispatchers.Main) {
                mContactAdapter = ContactAdapter(contacts)
                mBinding.rvContacts.adapter = mContactAdapter
            }
        }
    }

}