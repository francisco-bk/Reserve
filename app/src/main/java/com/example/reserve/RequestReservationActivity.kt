package com.example.reserve

import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.*
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
        timeSelectButton = findViewById(R.id.timeSelect)
        buildingName = findViewById(R.id.buildingName)
        roomName = findViewById(R.id.roomName)

        val building = intent.extras?.getString("building")
        val room = intent.extras?.getString("room")

        if (building != null) buildingName.text = building
        if (room != null) roomName.text = room

        updateReservationButton()

        val fragmentManager = supportFragmentManager
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val combinedValidator = CompositeDateValidator.allOf(
            listOf(DateValidatorPointForward.from(today),
                DateValidatorPointBackward.before(today + 6*DateUtils.DAY_IN_MILLIS)))
        val calendarConstraints = CalendarConstraints.Builder()
            .setStart(today)
            .setEnd(today + DateUtils.YEAR_IN_MILLIS)
            .setValidator(combinedValidator)
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

        setTimeDisplay()

        datePicker.addOnPositiveButtonClickListener {
            // find day of week
            dateSelectButton.text = datePicker.headerText
            val dateInMillis = datePicker.selection!! + DateUtils.DAY_IN_MILLIS
            val sdfDay = SimpleDateFormat("EEE")
            dow = sdfDay.format(dateInMillis)
            setTimeDisplay()
            updateReservationButton()
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
            // store reservation data in repository
            val roomObj = Room("ENG quad?", roomName.text.toString(), buildingName.text.toString(),
                timeSelectButton.text.toString(), dateSelectButton.text.toString(), dow)
            Repository.reservedRooms.add(roomObj)
            val roomKey = getReservationKey()
            val timeStr = timeSelectButton.text.toString()
            var hrInt = timeStr.replace(":00 AM", "").replace(":00 PM", "").toInt()
            if (timeStr.contains("AM") && timeStr.contains("12")) hrInt = 0
            else if (timeStr.contains("PM") && !timeStr.contains("12")) hrInt += 12

            val availableTimes : Array<Boolean>
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
            updateReservationButton()
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
                    val button = timeButtons[i]
                    if (isTimeAvailable) {
                        button.isEnabled = true
                    } else {
                        button.isEnabled = false
                        button.setTextColor(resources.getColor(R.color.grey))
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
                    val button = timeButtons[i]
                    button.isEnabled = false
                    button.setTextColor(resources.getColor(R.color.grey))
                    i++
                }
            }

            bottomSheetDialog.show()
        }
    }

    /** Set the timeSelectButton to display earliest available time of the date selected displayed on
     * dateSelectButton. */
    private fun setTimeDisplay() {
        val today = System.currentTimeMillis()
        val sdf = SimpleDateFormat("MMM d, yyyy")
        val todayString = sdf.format(today)
        val sdfHour = SimpleDateFormat("h:00 aa")
        val currentHour = sdfHour.format(today + DateUtils.HOUR_IN_MILLIS)
        val sdfHourInt = SimpleDateFormat("H")
        val hourInt = sdfHourInt.format(today).toInt()

        val key = getReservationKey()
        if (Repository.reservationTable.containsKey(key)) {
            val availableTimes = Repository.reservationTable[key]!!
            var i = 0
            while (i < 24) {
                if (availableTimes[i]) { // check if room has open time slot for that day
                    // convert i to hour from 0-12
                    val iDiv = i / 12
                    val AMPM = if (iDiv == 1) "PM" else "AM"
                    val iMod = i % 12
                    var timeStr = if (iMod == 0) "12:00 " else "$iMod:00 "
                    timeStr += AMPM

                    if (!dateSelectButton.text.equals(todayString) || i > hourInt) {
                        timeSelectButton.text = timeStr
                        return
                    }
                }
                i++
            }
            timeSelectButton.text = getString(R.string.all_booked)
            timeSelectButton.isEnabled = false
            timeSelectButton.setTextColor(resources.getColor(R.color.grey))
            return
        }
        if (dateSelectButton.text.equals(todayString)) {
            timeSelectButton.text = currentHour
        } else {
            timeSelectButton.text = getString(R.string.am12)
        }
        timeSelectButton.isEnabled = true
        timeSelectButton.setTextColor(resources.getColor(R.color.black))
    }

    /** Get reservation key to search for room in Repository.reservationTable */
    private fun getReservationKey(): String {
        return roomName.text.toString() + " - " + dateSelectButton.text.toString()
    }

    /** lock/unlock reservation button if conditions are/aren't met */
    private fun updateReservationButton() {
        if (checkBox.isChecked && timeSelectButton.isEnabled) {
            reserveButton.isEnabled = true
            reserveButton.setBackgroundColor(resources.getColor(R.color.red))
        } else {
            reserveButton.isEnabled = false
            reserveButton.setBackgroundColor(resources.getColor(R.color.grey))
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