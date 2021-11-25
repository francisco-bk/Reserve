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
import java.util.*


class RequestReservationActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var backButton: ImageButton
    private lateinit var reserveButton: Button
    private lateinit var checkBox: CheckBox
    private lateinit var dateSelectButton: Button
//    private lateinit var timeSelect: Spinner
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
//        timeSelect = findViewById(R.id.timeSelect)
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

        val date = System.currentTimeMillis()
        val sdf = SimpleDateFormat("MMM dd, yyyy")
        dateSelectButton.text = sdf.format(date)

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
            // TODO: don't allow reserve if in past
            // preliminary implementation of storing time data in repository
//            val roomObj = Room("ENG quad?", roomName.text.toString(), buildingName.text.toString(), timeSelect.selectedItem.toString(), dateSelectButton.text.toString(), dow)
            val roomObj = Room("ENG quad?", roomName.text.toString(), buildingName.text.toString(),
                timeSelectButton.text.toString(), dateSelectButton.text.toString(), dow)
            Repository.reservedRooms.add(roomObj)
            val roomKey = roomName.text.toString() + " - " + dateSelectButton.text.toString()
//            var timeStr = timeSelect.selectedItem.toString()
            var timeStr = timeSelectButton.text.toString()
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

        timeSelectButton.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)

            bottomSheetDialog.setContentView(R.layout.dialog_time_selection)

            Repository.reservationTable

            val am12 =  bottomSheetDialog.findViewById<Button>(R.id.am12)!!.setOnClickListener {
                timeSelectButton.text = "12:00 AM"; bottomSheetDialog.dismiss() }
            val am1 =   bottomSheetDialog.findViewById<Button>(R.id.am1) !!.setOnClickListener {
                timeSelectButton.text = "1:00 AM"; bottomSheetDialog.dismiss() }
            val am2 =   bottomSheetDialog.findViewById<Button>(R.id.am2) !!.setOnClickListener {
                timeSelectButton.text = "2:00 AM"; bottomSheetDialog.dismiss() }
            val am3 =   bottomSheetDialog.findViewById<Button>(R.id.am3) !!.setOnClickListener {
                timeSelectButton.text = "3:00 AM"; bottomSheetDialog.dismiss() }
            val am4 =   bottomSheetDialog.findViewById<Button>(R.id.am4) !!.setOnClickListener {
                timeSelectButton.text = "4:00 AM"; bottomSheetDialog.dismiss() }
            val am5 =   bottomSheetDialog.findViewById<Button>(R.id.am5) !!.setOnClickListener {
                timeSelectButton.text = "5:00 AM"; bottomSheetDialog.dismiss() }
            val am6 =   bottomSheetDialog.findViewById<Button>(R.id.am6) !!.setOnClickListener {
                timeSelectButton.text = "6:00 AM"; bottomSheetDialog.dismiss() }
            val am7 =   bottomSheetDialog.findViewById<Button>(R.id.am7) !!.setOnClickListener {
                timeSelectButton.text = "7:00 AM"; bottomSheetDialog.dismiss() }
            val am8 =   bottomSheetDialog.findViewById<Button>(R.id.am8) !!.setOnClickListener {
                timeSelectButton.text = "8:00 AM"; bottomSheetDialog.dismiss() }
            val am9 =   bottomSheetDialog.findViewById<Button>(R.id.am9) !!.setOnClickListener {
                timeSelectButton.text = "9:00 AM"; bottomSheetDialog.dismiss() }
            val am10 =  bottomSheetDialog.findViewById<Button>(R.id.am10)!!.setOnClickListener {
                timeSelectButton.text = "10:00 AM"; bottomSheetDialog.dismiss() }
            val am11 =  bottomSheetDialog.findViewById<Button>(R.id.am11)!!.setOnClickListener {
                timeSelectButton.text = "11:00 AM"; bottomSheetDialog.dismiss() }
            val pm12 =  bottomSheetDialog.findViewById<Button>(R.id.pm12)!!.setOnClickListener {
                timeSelectButton.text = "12:00 PM"; bottomSheetDialog.dismiss() }
            val pm1 =   bottomSheetDialog.findViewById<Button>(R.id.pm1) !!.setOnClickListener {
                timeSelectButton.text = "1:00 PM"; bottomSheetDialog.dismiss() }
            val pm2 =   bottomSheetDialog.findViewById<Button>(R.id.pm2) !!.setOnClickListener {
                timeSelectButton.text = "2:00 PM"; bottomSheetDialog.dismiss() }
            val pm3 =   bottomSheetDialog.findViewById<Button>(R.id.pm3) !!.setOnClickListener {
                timeSelectButton.text = "3:00 PM"; bottomSheetDialog.dismiss() }
            val pm4 =   bottomSheetDialog.findViewById<Button>(R.id.pm4) !!.setOnClickListener {
                timeSelectButton.text = "4:00 PM"; bottomSheetDialog.dismiss() }
            val pm5 =   bottomSheetDialog.findViewById<Button>(R.id.pm5) !!.setOnClickListener {
                timeSelectButton.text = "5:00 PM"; bottomSheetDialog.dismiss() }
            val pm6 =   bottomSheetDialog.findViewById<Button>(R.id.pm6) !!.setOnClickListener {
                timeSelectButton.text = "6:00 PM"; bottomSheetDialog.dismiss() }
            val pm7 =   bottomSheetDialog.findViewById<Button>(R.id.pm7) !!.setOnClickListener {
                timeSelectButton.text = "7:00 PM"; bottomSheetDialog.dismiss() }
            val pm8 =   bottomSheetDialog.findViewById<Button>(R.id.pm8) !!.setOnClickListener {
                timeSelectButton.text = "8:00 PM"; bottomSheetDialog.dismiss() }
            val pm9 =   bottomSheetDialog.findViewById<Button>(R.id.pm9) !!.setOnClickListener {
                timeSelectButton.text = "9:00 PM"; bottomSheetDialog.dismiss() }
            val pm10 =  bottomSheetDialog.findViewById<Button>(R.id.pm10)!!.setOnClickListener {
                timeSelectButton.text = "10:00 PM"; bottomSheetDialog.dismiss() }
            val pm11 =  bottomSheetDialog.findViewById<Button>(R.id.pm11)!!.setOnClickListener {
                timeSelectButton.text = "11:00 PM"; bottomSheetDialog.dismiss() }

            bottomSheetDialog.show()
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