package com.example.visprog_week7.dataSoal1.model
import com.google.gson.annotations.SerializedName
data class Main (
    @SerializedName("temp") val temp: Double, // Temperature (e.g., 31.0)
    @SerializedName("feels_like") val feelsLike: Double, // Feels Like Temp (e.g., 35)
    @SerializedName("pressure") val pressure: Int, // Atmospheric pressure (e.g., 1011)
    @SerializedName("humidity") val humidity: Int // Humidity (e.g., 49)
    )