package com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.ui.favorites

import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.data.local.FavoriteStudentEntity

data class FavoritesUiState(
    val favorites: List<FavoriteStudentEntity> = emptyList(),
    val isLoading: Boolean = false
)
