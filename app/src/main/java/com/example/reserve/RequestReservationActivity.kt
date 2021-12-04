package com.example.reserve

import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.text.SimpleDateFormat


class RequestReservationActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var backButton: ImageButton
    private lateinit var reserveButton: Button
    private lateinit var checkBox: CheckBox
    private lateinit var dateSelectButton: Button
    private lateinit var timeSelectButton: Button
    private lateinit var buildingName: TextView
    private lateinit var roomName: TextView
    private lateinit var imageView: ImageView

    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val reservationJsonAdapter : JsonAdapter<Reservation> = moshi.adapter(Reservation::class.java)
    private val timeJsonAdapter : JsonAdapter<Time> = moshi.adapter(Time::class.java)

    private val type = Types.newParameterizedType(List::class.java, Time::class.java)
    private val timeListJsonAdapter = moshi.adapter<List<Time>>(type)

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
        imageView = findViewById(R.id.imageView)

        val id = intent.extras?.getInt("id")
        val building = intent.extras?.getString("building")
        val room = intent.extras?.getString("room")
        val image = intent.extras?.getString("image")

        if (building != null) buildingName.text = building
        if (room != null) roomName.text = room
        if (image != null) Glide.with(this).load(image).into(imageView)

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

        val today2 = System.currentTimeMillis()
        val sdf = SimpleDateFormat("MMM d, yyyy")
        val sdf2 = SimpleDateFormat("EEE")
        val todayString = sdf.format(today2)
        dateSelectButton.text = todayString
        var dow = sdf2.format(today2)

        val sdf3 = SimpleDateFormat("MM-dd-yyyy")
        var selectedDate = sdf3.format(today2)

        setTimeDisplay()

        datePicker.addOnPositiveButtonClickListener {
            // find day of week
            dateSelectButton.text = datePicker.headerText
            val dateInMillis = datePicker.selection!! + DateUtils.DAY_IN_MILLIS
            val sdfDay = SimpleDateFormat("EEE")
            dow = sdfDay.format(dateInMillis)
            selectedDate = sdf3.format(dateInMillis)
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
            // store reservation data in repository (LOCAL)
            val roomObj = Room(id!!, "ENG quad?", roomName.text.toString(), buildingName.text.toString(), false, 100, "", timeSelectButton.text.toString(), dateSelectButton.text.toString(), dow)
            Repository.reservedRooms.add(roomObj)
            val roomKey = getReservationKey()
            // LOCAL SEGMENT END

            // Get the hr in Int from range 1-24 (1 is 12:00 AM, 2 is 1:00 AM, etc.)
            val timeStr = timeSelectButton.text.toString()
            var hrInt = timeStr.replace(":00 AM", "").replace(":00 PM", "").toInt()
            if (timeStr.contains("AM") && timeStr.contains("12")) hrInt = 1
            else if (timeStr.contains("PM") && !timeStr.contains("12")) hrInt += 12

            // LOCAL RESERVE BEGIN
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
            // LOCAL RESERVE END

            val newReservation = Reservation(id!!, 5, hrInt, selectedDate)
            val requestBody = reservationJsonAdapter.toJson(newReservation).toRequestBody(("application/json; charset=utf-8").toMediaType())
            val postRequest = Request.Builder().url(Repository.BASE_URL + "reservations/").post(requestBody).build()
            client.newCall(postRequest).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("NETWORK_DEBUG", "Reservation POST Error: " + e.printStackTrace().toString())
                }
                override fun onResponse(call: Call, response: Response) {
                    Log.d("NETWORK_DEBUG", "Reservation POST response: $response")
                }
            })

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

            // Grey out specific time button if time slot is reserved for that day (NETWORK)
//            var j = 1
//            var timeArray : ArrayList<Boolean> = ArrayList()
//            while (j < 25) {
//                val getRequest = Request.Builder()
//                    .url(Repository.BASE_URL + "times/" + id + "/" + selectedDate + "/" + j + "/").build()
//                Log.d("THISISJ", j.toString())
//                client.newCall(getRequest).enqueue(object : Callback {
//                    override fun onFailure(call: Call, e: IOException) {
//                        Log.d(
//                            "NETWORK DEBUG",
//                            "Time table GET error: " + e.printStackTrace().toString()
//                        )
//                    }
//
//                    override fun onResponse(call: Call, response: Response) {
//                        response.use {
//                            if (!it.isSuccessful) {
//                                Log.d("NETWORK_DEBUG", "Time table GET unsuccessful: $response")
//                            }
//                            val timeTable = timeJsonAdapter.fromJson(response.body!!.string())!!
//                            if (timeTable.error.equals("")) {
//                                timeArray.add(true)
//                            } else {
//                                timeArray.add(false)
//                            }
////                            val timeTables =
////                                timeListJsonAdapter.fromJson(response.body!!.string())!!
////                            for (timeTableObj in timeTables) {
////                                if (timeTableObj.date.equals(selectedDate) && timeTableObj.room_id == id) {
////                                    val timeTable = timeToList(timeTableObj)
////                                    Log.d("NETWORK_DEBUG", timeTable.toString())
////                                    runOnUiThread {
////                                        for ((i, isTimeUnavailable) in timeTable.withIndex()) {
////                                            val button = timeButtons[i]
////                                            if (isTimeUnavailable) {
////                                                button.isEnabled = false
////                                                button.setTextColor(resources.getColor(R.color.grey))
////                                            } else {
////                                                button.isEnabled = true
////                                            }
////                                        }
////                                    }
////                                }
////                            }
//                        }
//                        j++
//                    }
//                })
//            }
//            val i = 0
//            while (i < 24) {
//                val button = timeButtons[i]
//                if (timeArray[i]) {
//                    button.isEnabled = false
//                    button.setTextColor(resources.getColor(R.color.grey))
//                } else {
//                    button.isEnabled = true
//                }
//            }


            // Grey out specific time button if time slot is reserved for that day (LOCAL)
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

    fun timeToList(time: Time): List<Boolean> {
        return listOf(
            time.t1,
            time.t2,
            time.t3,
            time.t4,
            time.t5,
            time.t6,
            time.t7,
            time.t8,
            time.t9,
            time.t10,
            time.t11,
            time.t12,
            time.t13,
            time.t14,
            time.t15,
            time.t16,
            time.t17,
            time.t18,
            time.t19,
            time.t20,
            time.t21,
            time.t22,
            time.t23,
            time.t24
        )
    }
}