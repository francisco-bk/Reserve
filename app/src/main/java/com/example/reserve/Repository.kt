package com.example.reserve

class Repository private constructor() {
    companion object {
        val roomList = arrayListOf<Room>()
        val favorites = arrayListOf<Boolean>()
        val reservedTimes = arrayListOf<String>()
    }
}