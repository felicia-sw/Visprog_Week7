package com.example.visprog_week7.data.repository

import com.example.visprog_week7.data.di.NetworkModule
import com.example.visprog_week7.data.model.WeatherResponseDto
import com.example.visprog_week7.data.service.WeatherService

class WeatherRepository(
    private val weatherService: WeatherService = NetworkModule.weatherService
) {
    suspend fun getWeatherByCity(cityName: String): Result<WeatherResponseDto> {
        return try {
            val response = weatherService.getCurrentWeather(cityName)
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}