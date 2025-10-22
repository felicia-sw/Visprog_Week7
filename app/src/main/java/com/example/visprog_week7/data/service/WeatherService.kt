package com.example.visprog_week7.data.service

// File: com.example.visprog_week7.data.service/WeatherService.kt

import retrofit2.http.GET
import retrofit2.http.Query
import com.example.visprog_week7.data.model.WeatherResponseDto // Import DTO

// Define constants here or in a separate file as suggested before
private const val API_KEY = "3a1c37be5022a6b3db0f08b689359c59"
private const val UNITS = "metric"

interface WeatherService {
    @GET("weather")
    suspend fun getCurrentWeather(
        // 'q' for city name (user search input)
        @Query("q") cityName: String,

        // 'appid' parameter
        @Query("appid") apiKey: String = API_KEY,

        // 'units' parameter (use metric for Celsius)
        @Query("units") units: String = UNITS
    ): WeatherResponseDto
}