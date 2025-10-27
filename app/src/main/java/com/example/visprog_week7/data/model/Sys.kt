package com.example.visprog_week7.data.model
import com.google.gson.annotations.SerializedName
data class Sys(
    @SerializedName("sunrise") val sunrise: Long, // Sunrise time (Unix timestamp)
    @SerializedName("sunset") val sunset: Long // Sunset time (Unix timestamp)
    // other sys fields are intentionally omitted
)