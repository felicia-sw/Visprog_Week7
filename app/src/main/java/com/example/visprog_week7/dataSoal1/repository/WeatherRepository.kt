package com.example.visprog_week7.dataSoal1.repository

import com.example.visprog_week7.dataSoal1.di.NetworkModule
import com.example.visprog_week7.dataSoal1.model.WeatherResponseDto
import com.example.visprog_week7.dataSoal1.service.WeatherService

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

