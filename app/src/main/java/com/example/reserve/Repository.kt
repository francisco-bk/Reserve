package com.example.reserve

class Repository private constructor() {
    companion object {
        val reservedRooms = arrayListOf<Room>()
        val favorites = arrayListOf<Room>()
    }
}