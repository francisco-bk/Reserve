package com.example.reserve

class Repository private constructor() {
    companion object {
        val reservedRooms = arrayListOf<Room>()
        val favorites = arrayListOf<Room>()
        val reservationTable = hashMapOf<String, Array<Boolean>>() // key in format of "ROOMNAME - DATE", Array of len 24 representing 24 hrs of day
    }
}