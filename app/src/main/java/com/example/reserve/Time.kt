package com.example.reserve

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Time (
    val id  : Int     = -1,
    val t1  : Boolean = false,
    val t2  : Boolean = false,
    val t3  : Boolean = false,
    val t4  : Boolean = false,
    val t5  : Boolean = false,
    val t6  : Boolean = false,
    val t7  : Boolean = false,
    val t8  : Boolean = false,
    val t9  : Boolean = false,
    val t10 : Boolean = false,
    val t11 : Boolean = false,
    val t12 : Boolean = false,
    val t13 : Boolean = false,
    val t14 : Boolean = false,
    val t15 : Boolean = false,
    val t16 : Boolean = false,
    val t17 : Boolean = false,
    val t18 : Boolean = false,
    val t19 : Boolean = false,
    val t20 : Boolean = false,
    val t21 : Boolean = false,
    val t22 : Boolean = false,
    val t23 : Boolean = false,
    val t24 : Boolean = false,
    val date: String  = "",
    val room_id : Int = -1
)