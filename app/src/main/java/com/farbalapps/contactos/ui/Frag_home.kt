package com.farbalapps.contactos.ui

import android.app.Activity.RESULT_OK
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
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.farbalapps.contactos.model.ContactEntity
import com.farbalapps.contactos.databinding.DialogContactDetailBinding
import com.google.android.material.snackbar.Snackbar
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.NavHostFragment
import com.farbalapps.contactos.R
import java.io.ByteArrayInputStream
import androidx.activity.OnBackPressedCallback
class Frag_home : Fragment() {
    private lateinit var mBinding: FragHomeBinding
    private lateinit var mContactAdapter: ContactAdapter
    private var allContacts: List<ContactEntity> = emptyList()
    private var searchJob: kotlinx.coroutines.Job? = null

    private val createOrEditLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            loadContacts()
        }
    }

    companion object {
        private const val EDIT_CONTACT_REQUEST = 2
        private const val CREATE_CONTACT_REQUEST = 1
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
        createContact()

        //Manejo del retroceso de la search view
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Si el SearchView está visible, lo ocultamos
                if (mBinding.searchViewM3.isShowing) {
                    mBinding.searchViewM3.hide()
                } else {
                    // Si no, dejamos que el sistema siga su curso (atrás normal)
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                }
            }
        })
    }


    private fun setupRecyclerView() {
        mContactAdapter = ContactAdapter { contact ->
            showContactDialog(contact)
        }
        mContactAdapter.setHasStableIds(true)
        mBinding.rvContacts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mContactAdapter
            setHasFixedSize(true)
            itemAnimator = null
            setItemViewCacheSize(20)
        }
        mBinding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mContactAdapter
            setHasFixedSize(true)
            itemAnimator = null
        }
    }
    private fun createContact(){
        mBinding.fabAddContact.setOnClickListener {
            val intent = Intent(requireContext(), CreateContacts::class.java)
            createOrEditLauncher.launch(intent)
        }
    }


     fun showContactDialog(contact: ContactEntity) {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogContactDetailBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
    
        with(dialogBinding) {
            tvName.text = contact.name
            tvPhone.text = contact.phone
    
            contact.photo?.let { photoBytes ->
                try {
                    val bitmap = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.size)

                    backgroundImage.setImageBitmap(bitmap)
                    ciProfileImage.setImageBitmap(bitmap)
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
                val intent = Intent(requireContext(), CreateContacts::class.java)
                intent.putExtra(CreateContacts.EXTRA_CONTACT_ID, contact.id)
                createOrEditLauncher.launch(intent)
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
        with(mBinding) {


            // VINCULACIÓN: Esto hace que al tocar la SearchBar se abra la SearchView automáticamente
            searchViewM3.setupWithSearchBar(searchBarM3)


            // ACCESO AL TEXTO: Accedemos al editText interno del componente
            searchViewM3.editText.addTextChangedListener { editable ->
                val query = editable.toString()

                // Reutilizamos tu lógica de Debounce (Job)
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    kotlinx.coroutines.delay(250)
                    filterContacts(query)
                }
            }

            // OPCIONAL: Escuchar cuando el usuario cierra la búsqueda
            searchViewM3.addTransitionListener { _, _, newState ->
                when (newState) {
                    com.google.android.material.search.SearchView.TransitionState.SHOWING -> {
                        // Ocultamos el FAB y el BottomNav del Activity
                        fabAddContact.hide()
                        (activity as? MainActivity)?.setBottomNavigationVisibility(false)
                    }
                    com.google.android.material.search.SearchView.TransitionState.HIDDEN -> {
                        // Mostramos el FAB y el BottomNav del Activity
                        fabAddContact.show()
                        (activity as? MainActivity)?.setBottomNavigationVisibility(true)
                        filterContacts("")
                    }
                    else -> {}

                }
            }
        }
    }

    private fun filterContacts(query: String?) {
        val filteredContacts = if (query.isNullOrEmpty()) {
            allContacts
        } else {
            allContacts.filter { contact ->
                contact.name.contains(query, true) ||
                        contact.phone.contains(query, true)
            }
        }
        with(mBinding) {
            if (filteredContacts.isEmpty() && !query.isNullOrEmpty()) {
                if (layoutEmptyState.visibility == View.GONE) {
                    layoutEmptyState.alpha = 0f
                    layoutEmptyState.visibility = View.VISIBLE
                    layoutEmptyState.animate().alpha(1f).setDuration(300).start()
                }
                rvSearchResults.visibility = View.GONE
            } else {
                layoutEmptyState.visibility = View.GONE
                rvSearchResults.visibility = View.VISIBLE
            }
        }
        mContactAdapter.submitList(filteredContacts)
    }

    fun loadContacts() {
        lifecycleScope.launch(Dispatchers.IO) {
            val contacts = ContactApplication.database.contactDao().getAllContacts()
            withContext(Dispatchers.Main) {
                allContacts = contacts
                if (allContacts.isEmpty()) {
                    mBinding.layoutInitialEmpty.visibility = View.VISIBLE
                    mBinding.rvContacts.visibility = View.GONE
                    mBinding.searchBarM3.visibility = View.GONE
                } else {
                    mBinding.layoutInitialEmpty.visibility = View.GONE
                    mBinding.rvContacts.visibility = View.VISIBLE
                    mBinding.searchBarM3.visibility = View.VISIBLE
                    mContactAdapter.submitList(allContacts)
                }

            }
        }

    }

    override fun onResume() {
        mBinding.fabAddContact.show()
        super.onResume()
    }

}
