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


        setUpRecyclerView()


        mBinding.tvAddcontact.setOnClickListener {
            launchCreateContactFragment()
        }

    }

    private fun launchCreateContactFragment( arg: Bundle? = null) {

        val fragment = CreateContact()

        if (arg != null) fragment.arguments = arg
        mBinding.searchBar.visibility = View.GONE
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


    override fun onClick(contactID: Long) {
        Snackbar.make(mBinding!!.root,
            "Click en el contacto $contactID",
            Snackbar.LENGTH_SHORT).show()
    }

    override fun onCallClick(contactID: Long) {

    }

    override fun onMessageClick(contactID: Long) {

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun hideSearchBar(isVisible: Boolean) {
        mBinding.searchBar.visibility = if (isVisible) View.VISIBLE else View.GONE

    }


}