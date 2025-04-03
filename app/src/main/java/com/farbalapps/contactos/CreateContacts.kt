package com.farbalapps.contactos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.farbalapps.contactos.databinding.ActCreateContactsBinding

class CreateContacts : AppCompatActivity() {
    private lateinit var mBinding: ActCreateContactsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActCreateContactsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.btnCancel.setOnClickListener {
            finish()

        }

    }


}