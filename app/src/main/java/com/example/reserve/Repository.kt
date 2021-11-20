package com.example.reserve

class Repository private constructor() {
    companion object {
        val roomList = mutableListOf<Room>()
    }
}