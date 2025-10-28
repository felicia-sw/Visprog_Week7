package com.example.visprog_week7.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visprog_week7.data.repository.WeatherRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherViewModel(
    private val repository: WeatherRepository = WeatherRepository()
) : ViewModel() {

    var uiState by mutableStateOf<WeatherUiState>(WeatherUiState.Initial)
        private set

    var searchQuery by mutableStateOf("")
        private set

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
    }

    fun searchWeather() {
        if (searchQuery.isBlank()) return

        uiState = WeatherUiState.Loading

        viewModelScope.launch {
            val result = repository.getWeatherByCity(searchQuery)

            result.onSuccess { weatherData ->
                uiState = WeatherUiState.Success(weatherData)
            }.onFailure { e ->
                val errorMessage = when (e) {
                    is HttpException -> {
                        // Handle 404 Not Found explicitly as per requirement example
                        if (e.code() == 404) "HTTP 404 Not Found"
                        else "HTTP Error: ${e.code()}"
                    }
                    is IOException -> "Network Error: Check connection"
                    else -> "An unknown error occurred"
                }
                uiState = WeatherUiState.Error("Oops! Something went wrong\n$errorMessage")
            }
        }
    }

    // Utility functions for formatting Unix timestamps
    fun getFormattedDate(timestamp: Long): String {
        return SimpleDateFormat("MMMM d", Locale.getDefault()).format(Date(timestamp * 1000L))
    }

    fun getFormattedTime(timestamp: Long): String {
        return SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(timestamp * 1000L))
    }

    fun getFormattedUpdateTime(timestamp: Long): String {
        return SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(timestamp * 1000L))
    }

    // Logic to normalize weather condition for Panda image matching
    fun getWeatherCondition(main: String): String {
        return when (main.lowercase(Locale.ROOT)) {
            "clear" -> "Clear"
            "rain" -> "Rain"
            "clouds" -> "Clouds"
            else -> main
        }
    }
}