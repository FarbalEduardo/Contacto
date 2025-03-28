package com.farbalapps.contactos

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.farbalapps.contactos.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), OnclickListener, MainAux {

    private lateinit var mAdapter: ContactAdapter
    private lateinit var mLinearLayout: LinearLayoutManager
    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        //Este Listener se agregara solo en la creacion de la activity.
        supportFragmentManager.addOnBackStackChangedListener {
            // Si el back stack está vacío, significa que estamos en la pantalla principal
            hideSearchBar(supportFragmentManager.backStackEntryCount == 0)
        }

        setUpRecyclerView()

        mBinding.tvAddcontact.setOnClickListener {
            launchCreateContactFragment()
        }

        //Se asegura que la SearchBar se muestre al iniciar la activity
        hideSearchBar(true)
    }

    private fun launchCreateContactFragment(arg: Bundle? = null) {
        val fragment = CreateContact()
        //Se le pasa el Bundle si es que lo contiene.
        if (arg != null) fragment.arguments = arg
        //Ocultar el SearchBar
        hideSearchBar(false)
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerMain, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setUpRecyclerView() {
        mAdapter = ContactAdapter(mutableListOf(), this)
        mLinearLayout = LinearLayoutManager(this)
        mBinding.rvcontact.apply {
            setHasFixedSize(true)
            layoutManager = mLinearLayout
            adapter = mAdapter
        }
    }

    //Metodo modificado
    fun hideSearchBar(isVisible: Boolean) {
        mBinding.searchBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun onClick(contactID: Long) {
        Snackbar.make(
            mBinding.root,
            "Click en el contacto $contactID",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onCallClick(contactID: Long) {
    }

    override fun onMessageClick(contactID: Long) {
    }

    override fun onResume() {
        super.onResume()
        //Se asegura que la SearchBar se muestre al regresar a la activity
        hideSearchBar(supportFragmentManager.backStackEntryCount == 0)
    }
}