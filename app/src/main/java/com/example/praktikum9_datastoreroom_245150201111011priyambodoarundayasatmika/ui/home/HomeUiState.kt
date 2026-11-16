package com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.ui.home

import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.data.model.Student

data class HomeUiState(
    val students: List<Student> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val sortAscending: Boolean = true
)
