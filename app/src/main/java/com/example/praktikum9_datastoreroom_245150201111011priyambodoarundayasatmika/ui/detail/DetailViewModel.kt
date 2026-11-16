package com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.data.repository.FavoriteRepository
import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.data.repository.StudentRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: StudentRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private var favoriteJob: Job? = null

    fun loadStudent(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val student = repository.getStudent(id)
                _uiState.update {
                    it.copy(
                        student = student,
                        isLoading = false,
                        errorMessage = null
                    )
                }
                // Catatan 12: panggil observeFavorite setelah data student dimuat
                observeFavorite(id)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = e.message ?: "Data tidak tersedia",
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun observeFavorite(id: Int) {
        favoriteJob?.cancel()
        favoriteJob = viewModelScope.launch {
            favoriteRepository.observeIsFavorite(id).collect { isFav ->
                _uiState.update { it.copy(isFavorite = isFav) }
            }
        }
    }

    fun toggleFavorite() {
        val student = uiState.value.student ?: return
        viewModelScope.launch {
            if (uiState.value.isFavorite) {
                favoriteRepository.remove(student.id)
            } else {
                favoriteRepository.add(student)
            }
        }
    }

    companion object {
        fun factory(
            repository: StudentRepository,
            favoriteRepository: FavoriteRepository
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DetailViewModel(repository, favoriteRepository) as T
                }
            }
    }
}
