package com.example.visprog_week7.dataSoal1.service

import retrofit2.http.GET
import retrofit2.http.Query
import com.example.visprog_week7.dataSoal1.model.WeatherResponseDto
import com.example.visprog_week7.utilSoal1.Constants // Import Constants

interface WeatherService {
    @GET("weather")
    suspend fun getCurrentWeather(
        // 'q' for city name (user search input)
        @Query("q") cityName: String,

        // 'appid' parameter, using the key from Constants
        @Query("appid") apiKey: String = Constants.API_KEY,

        // 'units' parameter (use metric for Celsius)
        @Query("units") units: String = Constants.UNITS
    ): WeatherResponseDto
}