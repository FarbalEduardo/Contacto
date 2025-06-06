package com.farbalapps.contactos.ui

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.farbalapps.contactos.EventDatabase
import com.farbalapps.contactos.databinding.ActCreateEventBinding
import com.farbalapps.contactos.intarfaces.EventDao
import com.farbalapps.contactos.model.EventEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateEvent : AppCompatActivity() {
    private lateinit var mBinding: ActCreateEventBinding
    private val calendar = Calendar.getInstance()
    private lateinit var db : EventDatabase
    private lateinit var eventDao: EventDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActCreateEventBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        db
        setupTimePickers()
    }

    private fun saveEvent() {
        val event = EventEntity(
            title = mBinding.titleEditText.text.toString(),
            date = calendar.timeInMillis,
            startTime = setupTimePickers().toString(),
            endTime = setupTimePickers().toString(),
            isAllDay = mBinding.allDaySwitch.isChecked,
            description = mBinding.notesText.text.toString()
        )
    }
    private fun setupTimePickers() {
        mBinding.startTimeText.setOnClickListener {
            showTimePickerDialog(mBinding.startTimeText)
        }

        mBinding.endTimeText.setOnClickListener {
            showTimePickerDialog(mBinding.endTimeText)
        }
    }

    private fun showTimePickerDialog(textView: TextView) {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            updateLabel(textView)
        }

        TimePickerDialog(
            this,
            timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false // false for 12-hour format with AM/PM
        ).show()
    }

    private fun updateLabel(textView: TextView) {
        val format = "h:mm a"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        textView.text = sdf.format(calendar.time)
    }
}