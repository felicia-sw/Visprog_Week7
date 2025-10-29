package com.example.visprog_week7.uiSoal1.viewmodel

import com.example.visprog_week7.dataSoal1.model.WeatherResponseDto

sealed class WeatherUiState {
    data object Initial : WeatherUiState()
    data object Loading : WeatherUiState()
    data class Success(val weatherData: WeatherResponseDto) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}