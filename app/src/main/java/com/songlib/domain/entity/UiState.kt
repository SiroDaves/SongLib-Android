package com.songlib.domain.entity

sealed class UiState {
    object Loading : UiState()
    object Loaded : UiState()
    object Filtered : UiState()
    object Saving : UiState()
    object Saved : UiState()
    class Error(val errorMessage: String) : UiState()
}
