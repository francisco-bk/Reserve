package com.example.reserve

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Room (
    val id : Int = -1,
    val location : String = "",
    val room_name : String = "",
    val hall : String = "",
    val features : Boolean = false,
    val capacity : Int = 0,
    val image : String = "",
    val time : String = "",
    val date : String = "",
    val dow : String = ""
)
