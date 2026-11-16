package com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.ui.detail

import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.data.model.Student

data class DetailUiState(
    val student: Student? = null,
    val isLoading: Boolean = false,
    val isFavorite: Boolean = false,
    val errorMessage: String? = null
)
