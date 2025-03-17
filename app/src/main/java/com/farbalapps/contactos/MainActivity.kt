package com.farbalapps.contactos

import android.os.Bundle
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


        //mBinding!!.searchView.isIconified = false
        setUpRecyclerView()


        mBinding.tvAddcontact.setOnClickListener {
            launchCreateContactFragment()
        }

    }

    private fun launchCreateContactFragment( arg: Bundle? = null) {

        val fragment = CreateContact()

        if (arg != null) fragment.arguments = arg
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
        TODO("Not yet implemented")
    }

    override fun onMessageClick(contactID: Long) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()

    }



}