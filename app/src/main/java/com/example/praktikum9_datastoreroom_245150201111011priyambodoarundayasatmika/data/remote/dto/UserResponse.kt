package com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.data.remote.dto

data class UserResponse(
    val users: List<UserDto>,
    val total: Int,
    val skip: Int,
    val limit: Int
)
