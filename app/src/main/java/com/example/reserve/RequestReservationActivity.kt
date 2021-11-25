package com.example.reserve

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat


class RequestReservationActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var backButton: ImageButton
    private lateinit var reserveButton: Button
    private lateinit var checkBox: CheckBox
    private lateinit var dateSelectButton: Button
    private lateinit var timeSelectButton: Button
    private lateinit var buildingName: TextView
    private lateinit var roomName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_reservation)

        backButton = findViewById(R.id.backButton)
        reserveButton = findViewById(R.id.reserveButton)
        checkBox = findViewById(R.id.checkBox)
        dateSelectButton = findViewById(R.id.dateSelect)
        timeSelectButton = findViewById(R.id.timeSelect2)
        buildingName = findViewById(R.id.buildingName)
        roomName = findViewById(R.id.roomName)

        val building = intent.extras?.getString("building")
        val room = intent.extras?.getString("room")

        if (building != null) buildingName.text = building
        if (room != null) roomName.text = room

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

        val date = System.currentTimeMillis()
        val sdf = SimpleDateFormat("MMM d, yyyy")
        val sdf2 = SimpleDateFormat("EEE")
        val todayString = sdf.format(date)
        dateSelectButton.text = todayString
        var dow = sdf2.format(date)

        val sdfHour = SimpleDateFormat("h:00 aa")
        timeSelectButton.text = sdfHour.format(date + DateUtils.HOUR_IN_MILLIS)

        datePicker.addOnPositiveButtonClickListener {
            // find day of week
            dateSelectButton.text = datePicker.headerText
            val dateInMillis = datePicker.selection!! + DateUtils.DAY_IN_MILLIS
            val sdfDay = SimpleDateFormat("EEE")
            dow = sdfDay.format(dateInMillis)

            // grey out timeSelectButton if all times booked for that day
            val key = getReservationKey()
            if (Repository.reservationTable.containsKey(key)) {
                val availableTimes = Repository.reservationTable[key]!!
                var hasOpenTimeSlot = false
                var i = 0
                while (!hasOpenTimeSlot) { // check if room has open time slot for that day
                    if (availableTimes[i]) {
                        hasOpenTimeSlot = true
                        val sdfHour = SimpleDateFormat("h:00 aa")
                        if (todayString.equals(dateSelectButton.text)) {
                            timeSelectButton.text = sdfHour.format(date + DateUtils.HOUR_IN_MILLIS)
                        } else {
                            timeSelectButton.text = sdfHour.format(dateInMillis)
                        }
                    }
                    i++
                }
                if (!hasOpenTimeSlot) {
                    timeSelectButton.text = "All times booked for this day!"
                    timeSelectButton.isEnabled = false
                    timeSelectButton.setTextColor(Color.GRAY)
                }
            } else {
                timeSelectButton.text = "12:00 AM"
                timeSelectButton.isEnabled = true
            }
        }

        dateSelectButton.setOnClickListener {
            datePicker.show(fragmentManager, "DATE_PICKER")
        }

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
            val roomObj = Room("ENG quad?", roomName.text.toString(), buildingName.text.toString(),
                timeSelectButton.text.toString(), dateSelectButton.text.toString(), dow)
            Repository.reservedRooms.add(roomObj)
            val roomKey = getReservationKey()
            var timeStr = timeSelectButton.text.toString()
            var hrInt = timeStr.replace(":00 AM", "").replace(":00 PM", "").toInt()
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

        timeSelectButton.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)

            bottomSheetDialog.setContentView(R.layout.dialog_time_selection)

            var timeButtons= arrayOf<Button>(
                bottomSheetDialog.findViewById(R.id.am12)!!,
                bottomSheetDialog.findViewById(R.id.am1) !!,
                bottomSheetDialog.findViewById(R.id.am2) !!,
                bottomSheetDialog.findViewById(R.id.am3) !!,
                bottomSheetDialog.findViewById(R.id.am4) !!,
                bottomSheetDialog.findViewById(R.id.am5) !!,
                bottomSheetDialog.findViewById(R.id.am6) !!,
                bottomSheetDialog.findViewById(R.id.am7) !!,
                bottomSheetDialog.findViewById(R.id.am8) !!,
                bottomSheetDialog.findViewById(R.id.am9) !!,
                bottomSheetDialog.findViewById(R.id.am10)!!,
                bottomSheetDialog.findViewById(R.id.am11)!!,
                bottomSheetDialog.findViewById(R.id.pm12)!!,
                bottomSheetDialog.findViewById(R.id.pm1) !!,
                bottomSheetDialog.findViewById(R.id.pm2) !!,
                bottomSheetDialog.findViewById(R.id.pm3) !!,
                bottomSheetDialog.findViewById(R.id.pm4) !!,
                bottomSheetDialog.findViewById(R.id.pm5) !!,
                bottomSheetDialog.findViewById(R.id.pm6) !!,
                bottomSheetDialog.findViewById(R.id.pm7) !!,
                bottomSheetDialog.findViewById(R.id.pm8) !!,
                bottomSheetDialog.findViewById(R.id.pm9) !!,
                bottomSheetDialog.findViewById(R.id.pm10)!!,
                bottomSheetDialog.findViewById(R.id.pm11)!!
            )

            // Set onClickListener for all buttons
            for (button in timeButtons) {
                button.setOnClickListener() {
                    timeSelectButton.text = button.text
                    bottomSheetDialog.dismiss()
                }
            }

            // Grey out specific time button if time slot is reserved for that day
            val key = getReservationKey()
            if (Repository.reservationTable.containsKey(key)) {
                val availableTimes = Repository.reservationTable[key]!!
                for ((i, isTimeAvailable) in availableTimes.withIndex()) {
                    var button = timeButtons[i]
                    if (isTimeAvailable) {
                        button.isEnabled = true
                    } else {
                        button.isEnabled = false
                        button.setTextColor(Color.GRAY)
                    }
                }
            }

            // Grey out all individual time buttons before current time if today is selected
            val dateMillis = System.currentTimeMillis()
            val sdfDate = SimpleDateFormat("MMM d, yyyy")
            if (dateSelectButton.text == sdfDate.format(dateMillis)) {
                val sdfHour = SimpleDateFormat("HH")
                val currentHour = sdfHour.format(dateMillis).toInt() // Hour in day 0-23
                var i = 0
                while (i <= currentHour) {
                    var button = timeButtons[i]
                    button.isEnabled = false
                    button.setTextColor(Color.GRAY)
                    i++
                }
            }

            bottomSheetDialog.show()
        }
    }

    private fun getReservationKey(): String {
        return roomName.text.toString() + " - " + dateSelectButton.text.toString()
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}