package com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.data.local.FavoriteStudentEntity
import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState(isLoading = true))
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            favoriteRepository.observeFavorites().collect { list ->
                _uiState.value = FavoritesUiState(
                    favorites = list,
                    isLoading = false
                )
            }
        }
    }

    companion object {
        fun factory(favoriteRepository: FavoriteRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return FavoritesViewModel(favoriteRepository) as T
                }
            }
    }
}
