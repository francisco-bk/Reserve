package com.example.reserve

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker

class RequestReservationActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var reserveButton: Button
    private lateinit var checkBox: CheckBox
    private lateinit var startDate: Button
    private lateinit var endDate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_reservation)

        backButton = findViewById(R.id.backButton)
        reserveButton = findViewById(R.id.reserveButton)
        checkBox = findViewById(R.id.checkBox)
        startDate = findViewById(R.id.startDate)
        endDate = findViewById(R.id.endDate)

        reserveButton.isEnabled = false
        reserveButton.setBackgroundColor(Color.GRAY)

        val fragmentManager = supportFragmentManager

        val today = MaterialDatePicker.todayInUtcMilliseconds()

        val calendarConstraints = CalendarConstraints.Builder()
            .setStart(today)
            .setEnd(today + DateUtils.YEAR_IN_MILLIS)
            // .setValidator() // TODO: Add date validator

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(today)
            .setCalendarConstraints(calendarConstraints.build())
            .build()



        datePicker.addOnPositiveButtonClickListener {
            startDate.text = datePicker.headerText
            endDate.text = datePicker.headerText
        }

        startDate.setOnClickListener {
            datePicker.show(fragmentManager, "DATE_PICKER")
        }

        endDate.setOnClickListener {
            datePicker.show(fragmentManager, "DATE_PICKER")
        }

        backButton.setOnClickListener {
            val intent = Intent(this, ExpandedCardActivity::class.java).apply {
                // TODO: add putExtras
            }
            startActivity(intent)
        }

        reserveButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                // TODO: add putExtras
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