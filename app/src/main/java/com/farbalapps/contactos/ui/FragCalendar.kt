package com.farbalapps.contactos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.farbalapps.contactos.databinding.FragCalendarBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FragCalendar : Fragment() {

    private lateinit var mBinding: FragCalendarBinding


    private lateinit var calendarView: CalendarView
    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var fabAddEvent: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mBinding = FragCalendarBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        calendarView = mBinding.calendarView
        eventsRecyclerView = mBinding.eventsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            // Aquí se configurará el adaptador
        }
        fabAddEvent = mBinding.fabAddEvent
    }

    private fun setupListeners() {
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Aquí se implementará la lógica para mostrar eventos del día seleccionado
        }

        fabAddEvent.setOnClickListener {
            // Aquí se implementará la lógica para agregar eventos
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}