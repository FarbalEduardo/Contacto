package com.farbalapps.contactos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.farbalapps.contactos.databinding.FragCreateContactBinding

class CreateContact : Fragment() {

    private var mActivity: MainActivity? = null
    private lateinit var mBinding: FragCreateContactBinding
    private var isExpanded = false // Estado inicial: campos ocultos

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as? MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inicializar el Binding
        mBinding = FragCreateContactBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
    override fun onDestroy() {
        mActivity= null
        super.onDestroy()
    }
}