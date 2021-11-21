package com.example.reserve

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import android.widget.*
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker


class RequestReservationActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var reserveButton: Button
    private lateinit var checkBox: CheckBox
    private lateinit var dateButton: Button
    private lateinit var timeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_reservation)

        backButton = findViewById(R.id.backButton)
        reserveButton = findViewById(R.id.reserveButton)
        checkBox = findViewById(R.id.checkBox)
        dateButton = findViewById(R.id.dateButton)
        timeButton = findViewById(R.id.timeButton)

        reserveButton.isEnabled = false
        reserveButton.setBackgroundColor(Color.GRAY)

        val fragmentManager = supportFragmentManager

        val today = MaterialDatePicker.todayInUtcMilliseconds()

        val calendarConstraints = CalendarConstraints.Builder()
            .setStart(today)
            .setEnd(today + DateUtils.YEAR_IN_MILLIS)
            .setValidator(DateValidatorPointForward.from(today))

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(today)
            .setCalendarConstraints(calendarConstraints.build())
            .build()

        datePicker.addOnPositiveButtonClickListener {
            dateButton.text = datePicker.headerText
        }

        dateButton.setOnClickListener {
            datePicker.show(fragmentManager, "DATE_PICKER")
        }

        timeButton.setOnClickListener {
        }

        backButton.setOnClickListener {
            val intent = Intent(this, ExpandedCardActivity::class.java).apply {
            }
            startActivity(intent)
        }

        reserveButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
            }
            startActivity(intent)
        }

        checkBox.setOnClickListener {
            if (checkBox.isChecked) {
                reserveButton.isEnabled = true
                reserveButton.setBackgroundColor(Color.RED)
            } else {
                reserveButton.isEnabled = false
                reserveButton.setBackgroundColor(Color.GRAY)
            }
        }
    }
}