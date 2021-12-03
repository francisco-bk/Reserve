package com.example.reserve

class Repository private constructor() {
    companion object {
        const val BASE_URL = "https://reserve-backend.herokuapp.com/"

        val reservedRooms = arrayListOf<Room>()
        val favorites = arrayListOf<Room>()

        /** Key in format of "ROOMNAME - DATE"
         *  Value: array of len 24 representing 24 hrs of day, false if time is unavailable. */
        val reservationTable = hashMapOf<String, Array<Boolean>>() // k
    }
}