package com.example.reserve

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class RequestReservationActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var backButton: ImageButton
    private lateinit var reserveButton: Button
    private lateinit var checkBox: CheckBox
    private lateinit var dateSelectButton: Button
    private lateinit var timeSelect: Spinner
    private lateinit var buildingName: TextView
    private lateinit var roomName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_reservation)

        backButton = findViewById(R.id.backButton)
        reserveButton = findViewById(R.id.reserveButton)
        checkBox = findViewById(R.id.checkBox)
        dateSelectButton = findViewById(R.id.dateSelect)
        timeSelect = findViewById(R.id.timeSelect)
        buildingName = findViewById(R.id.buildingName)
        roomName = findViewById(R.id.roomName)

        val building = getIntent().extras?.getString("building")
        val room = getIntent().extras?.getString("room")

        if (building != null) buildingName.text = building
        if (room != null) roomName.text = room

        reserveButton.isEnabled = false
        reserveButton.setBackgroundColor(Color.GRAY)

        val fragmentManager = supportFragmentManager

        val today = MaterialDatePicker.todayInUtcMilliseconds()
        var dow = ""

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
            dateSelectButton.text = datePicker.headerText
            val dateInMillis = datePicker.selection!!
            val daysArray =
                arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
            val calendar: Calendar = Calendar.getInstance()
            calendar.time = Date(dateInMillis)
            val day: Int = calendar.get(Calendar.DAY_OF_WEEK)
            dow = daysArray[day]
        }

        dateSelectButton.setOnClickListener {
            datePicker.show(fragmentManager, "DATE_PICKER")
        }

        timeSelect.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.Times))
        val timeSelectAdapter = ArrayAdapter.createFromResource(this, R.array.Times, android.R.layout.simple_spinner_item)
            timeSelectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timeSelect.adapter = timeSelectAdapter

        backButton.setOnClickListener {
            val intent = Intent(this, ExpandedCardActivity::class.java).apply {
                // TODO: add putExtras
                putExtra("building", building)
                putExtra("room", room)
            }
            startActivity(intent)
        }

        reserveButton.setOnClickListener {
            // TODO: code cleanup eventually
            // preliminary implementation of storing time data in repository
            val roomObj = Room("ENG quad?", roomName.text.toString(), buildingName.text.toString(), timeSelect.selectedItem.toString(), dateSelectButton.text.toString(), dow)
            Repository.reservedRooms.add(roomObj)
            val roomKey = roomName.text.toString() + " - " + dateSelectButton.text.toString()
            var timeStr = timeSelect.selectedItem.toString()
            var hrInt = timeStr.replace(":00 AM", "").replace(":00 AM", "").toInt()
            if (timeStr.contains("AM") && timeStr.contains("12")) hrInt = 0
            else if (timeStr.contains("PM") && !timeStr.contains("12")) hrInt += 12

            var availableTimes : Array<Boolean>
            if (Repository.reservationTable.containsKey(roomKey)) {
                availableTimes = Repository.reservationTable[roomKey]!!
            } else {
                availableTimes = Array(24, { i -> true}) // false is unavailable (booked)
            }
            availableTimes[hrInt] = false
            Repository.reservationTable[roomKey] = availableTimes

            Log.d("RESERVED", "added")
            Log.d("RESERVED", Repository.reservedRooms.toString())
            Log.d("RESERVED", "KEY: " + roomKey)
            Log.d("RESERVED", "AVAILABILITIES: " + Repository.reservationTable[roomKey].contentToString())
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

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}