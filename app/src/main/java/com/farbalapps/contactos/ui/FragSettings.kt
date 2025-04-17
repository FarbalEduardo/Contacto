package com.farbalapps.contactos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.farbalapps.contactos.R
import com.farbalapps.contactos.databinding.FragSettingsBinding


class FragSettings : Fragment() {

    private lateinit var mBinding: FragSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mBinding = FragSettingsBinding.inflate(inflater, container, false)
        return mBinding.root
    }



}