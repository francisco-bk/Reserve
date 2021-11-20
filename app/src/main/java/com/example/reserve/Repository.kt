package com.example.reserve

class Repository private constructor() {
    companion object {
        val reservedRooms = hashMapOf<Room, String>()
        val favorites = arrayListOf<Room>()
    }
}