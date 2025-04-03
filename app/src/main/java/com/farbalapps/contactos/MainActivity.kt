package com.farbalapps.contactos

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
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

//        //Este Listener se agregara solo en la creacion de la activity.
//        supportFragmentManager.addOnBackStackChangedListener {
//            // Si el back stack está vacío, significa que estamos en la pantalla principal
//            hideSearchBar(supportFragmentManager.backStackEntryCount == 0)
//        }
//
        setSupportActionBar(findViewById(R.id.my_toolbar))
        setupNavigationController()

        mBinding.myToolbar.setOnMenuItemClickListener{ menuItem ->
            when (menuItem.itemId){
                R.id.action_add_contact -> {
                    findNavController(R.id.nav_host_fragment_activity_main)
                        .navigate(R.id.nav_calls)
                    true
                }
                else -> false
            }
        }


    }

    private fun setupNavigationController(){
        // Setup Navigation Controller
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        // Connect BottomNavigationView with NavController
        mBinding.bottomNavigation.setupWithNavController(navController)

    }

//    private fun launchCreateContactFragment(arg: Bundle? = null) {
//        val fragment = CreateContact()
//        //Se le pasa el Bundle si es que lo contiene.
//        if (arg != null) fragment.arguments = arg
//        //Ocultar el SearchBar
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.containerMain, fragment)
//            .addToBackStack(null)
//            .commit()
//    }

//    private fun setUpRecyclerView() {
//        mAdapter = ContactAdapter(mutableListOf(), this)
//        mLinearLayout = LinearLayoutManager(this)
//        mBinding.rvContacts.apply {
//            setHasFixedSize(true)
//            layoutManager = mLinearLayout
//            adapter = mAdapter
//        }
//    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_contact -> {
                findNavController(R.id.nav_host_fragment_activity_main)
                    .navigate(R.id.nav_calls)
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }
    override fun onClick(contactID: Long) {

    }

    override fun onCallClick(contactID: Long) {
    }

    override fun onMessageClick(contactID: Long) {
    }

    override fun onResume() {
        super.onResume()
        //Se asegura que la SearchBar se muestre al regresar a la activity
    //    hideSearchBar(supportFragmentManager.backStackEntryCount == 0)
    }
}