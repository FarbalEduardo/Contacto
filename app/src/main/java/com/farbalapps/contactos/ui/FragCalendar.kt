package com.farbalapps.contactos.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.farbalapps.contactos.adapter.EventAdapter
import com.farbalapps.contactos.databinding.FragCalendarBinding
import com.farbalapps.contactos.model.EventEntity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar

class FragCalendar : Fragment() {
    private lateinit var mBinding: FragCalendarBinding
    private lateinit var calendarView: CalendarView
    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var fabAddEvent: FloatingActionButton
    private val eventAdapter = EventAdapter()
    private val events = mutableListOf<EventEntity>()
    private var selectedDate: Long = 0

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
            adapter = eventAdapter
        }
        fabAddEvent = mBinding.fabAddEvent
        selectedDate = calendarView.date
    }

    private fun setupListeners() {
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            selectedDate = calendar.timeInMillis
            updateEventsList()
        }

        fabAddEvent.setOnClickListener {
            val intent = Intent(requireContext(), CreateEvent::class.java)
            startActivity(intent)
        }
    }

    private fun updateEventsList() {
        val calendar = Calendar.getInstance().apply { timeInMillis = selectedDate }
        val dayEvents = events.filter { event ->
            val eventCalendar = Calendar.getInstance().apply { timeInMillis = event.date }
            eventCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
            eventCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
            eventCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)
        }
        eventAdapter.updateEvents(dayEvents)
    }
}