package com.example.visprog_week7.ui.viewmodel

import com.example.visprog_week7.data.model.WeatherResponseDto

sealed class WeatherUiState {
    data object Initial : WeatherUiState()
    data object Loading : WeatherUiState()
    data class Success(val weatherData: WeatherResponseDto) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}