package com.example.reserve

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Reservation (
    val room_id : Int = -1,
    val user : Int = -1,
    val time : Int = -1,
    val date : String = "",
//    val dow : String = ""
)