package com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.data.remote.dto

import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.data.model.Student

data class UserDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val university: String
)

fun UserDto.toStudent() = Student(
    id = id,
    name = "$firstName $lastName",
    email = email,
    university = university
)
