package com.farbalapps.contactos.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.farbalapps.contactos.R
import com.farbalapps.contactos.databinding.ActCreateEventBinding
import com.farbalapps.contactos.databinding.ActivityMainBinding

class CreateEvent : AppCompatActivity() {
    private lateinit var mBinding: ActCreateEventBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActCreateEventBinding.inflate(layoutInflater)
        setContentView(mBinding.root)



    }
}