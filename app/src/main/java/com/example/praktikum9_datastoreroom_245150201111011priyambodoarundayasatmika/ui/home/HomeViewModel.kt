package com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.data.model.Student
import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.data.preferences.UserPreferencesRepository
import com.example.praktikum9_datastoreroom_245150201111011priyambodoarundayasatmika.data.repository.StudentRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: StudentRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortAscending = MutableStateFlow(true)
    val sortAscending: StateFlow<Boolean> = _sortAscending.asStateFlow()

    private var rawStudents: List<Student> = emptyList()

    init {
        // Pulihkan lastSearchQuery dari DataStore saat inisialisasi (Langkah 4)
        viewModelScope.launch {
            val savedQuery = preferencesRepository.lastSearchQuery.first()
            _searchQuery.value = savedQuery
        }

        // Amati preferensi sort dari DataStore (Tugas Praktikum Ketentuan 6)
        viewModelScope.launch {
            preferencesRepository.sortAscending.collect { isAsc ->
                _sortAscending.value = isAsc
                _uiState.update { current ->
                    current.copy(
                        sortAscending = isAsc,
                        students = applySorting(rawStudents, isAsc)
                    )
                }
            }
        }

        observeSearch()
        observeDataStoreSave()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun toggleSortAscending() {
        viewModelScope.launch {
            val newSort = !_sortAscending.value
            preferencesRepository.saveSortAscending(newSort)
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeDataStoreSave() {
        viewModelScope.launch {
            searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { value ->
                    preferencesRepository.saveLastSearchQuery(value)
                }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearch() {
        viewModelScope.launch {
            searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    try {
                        val data = if (query.isBlank()) {
                            repository.getStudents()
                        } else {
                            repository.searchStudents(query)
                        }
                        rawStudents = data
                        val sorted = applySorting(data, _sortAscending.value)
                        _uiState.update {
                            it.copy(
                                students = sorted,
                                isLoading = false,
                                errorMessage = if (sorted.isEmpty() && query.isNotBlank()) {
                                    "Tidak ada profil dengan kata kunci \"$query\""
                                } else null
                            )
                        }
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        _uiState.update {
                            it.copy(
                                errorMessage = e.message ?: "Pencarian gagal",
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }

    private fun applySorting(list: List<Student>, ascending: Boolean): List<Student> {
        return if (ascending) {
            list.sortedBy { it.name.lowercase() }
        } else {
            list.sortedByDescending { it.name.lowercase() }
        }
    }

    fun retry() {
        val query = searchQuery.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val data = if (query.isBlank()) {
                    repository.getStudents()
                } else {
                    repository.searchStudents(query)
                }
                rawStudents = data
                val sorted = applySorting(data, _sortAscending.value)
                _uiState.update {
                    it.copy(students = sorted, isLoading = false)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.message ?: "Request gagal", isLoading = false)
                }
            }
        }
    }

    fun refresh() {
        val query = searchQuery.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val data = if (query.isBlank()) {
                    repository.getStudents()
                } else {
                    repository.searchStudents(query)
                }
                rawStudents = data
                val sorted = applySorting(data, _sortAscending.value)
                _uiState.update {
                    it.copy(
                        students = sorted,
                        isLoading = false,
                        errorMessage = if (sorted.isEmpty() && query.isNotBlank()) {
                            "Tidak ada profil dengan kata kunci \"$query\""
                        } else null
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = e.message ?: "Gagal memperbarui data",
                        isLoading = false
                    )
                }
            }
        }
    }

    companion object {
        fun factory(
            repository: StudentRepository,
            preferencesRepository: UserPreferencesRepository
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeViewModel(repository, preferencesRepository) as T
                }
            }
    }
}
