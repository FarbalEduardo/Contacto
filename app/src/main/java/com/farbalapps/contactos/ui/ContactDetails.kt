package com.farbalapps.contactos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.farbalapps.contactos.R

/**
 * A simple [androidx.fragment.app.Fragment] subclass.
 * Use the [ContactDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactDetails : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_contact_details, container, false)
    }

}