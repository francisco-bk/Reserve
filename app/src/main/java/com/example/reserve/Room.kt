package com.example.reserve

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Room(
    val location : String,
    val room : String,
    val hall : String,
    val time : String,
    val date : String,
    val dow : String
)
