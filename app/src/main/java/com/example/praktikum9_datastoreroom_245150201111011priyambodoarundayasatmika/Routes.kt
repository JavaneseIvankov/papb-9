package com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika

object Routes {
    const val HOME = "home"
    const val FAVORITES = "favorites"
    const val PROFILE = "profile"
    const val ABOUT = "about"
    const val DETAIL = "detail/{studentId}"

    fun detail(studentId: Int): String = "detail/$studentId"
}
