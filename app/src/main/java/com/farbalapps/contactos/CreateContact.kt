package com.farbalapps.contactos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.farbalapps.contactos.databinding.FragCreateContactBinding


class CreateContact : Fragment() {
    private var mActivity: MainActivity?= null
    private lateinit  var mBinding: FragCreateContactBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_create_contact, container, false)
    }





}