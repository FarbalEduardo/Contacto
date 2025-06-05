package com.farbalapps.contactos.ui

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.farbalapps.contactos.databinding.ActCreateEventBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateEvent : AppCompatActivity() {
    private lateinit var mBinding: ActCreateEventBinding
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActCreateEventBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupTimePickers()
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