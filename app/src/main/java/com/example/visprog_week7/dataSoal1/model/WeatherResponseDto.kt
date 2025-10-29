package com.example.visprog_week7.dataSoal1.model
import com.google.gson.annotations.SerializedName
data class WeatherResponseDto(
    @SerializedName("name") val name: String, // City name (e.g., "Kota Medan")
    @SerializedName("main") val main: Main,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("sys") val sys: Sys,
    @SerializedName("dt") val dt: Long // Time of data calculation, useful for update time
)