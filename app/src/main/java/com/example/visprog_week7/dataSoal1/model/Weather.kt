package com.example.visprog_week7.dataSoal1.model
import com.google.gson.annotations.SerializedName
data class Weather(
    @SerializedName("main") val main: String, // Weather condition group ("Clear", "Rain", "Clouds") for Panda logic
    @SerializedName("icon") val icon: String // Icon ID to get the weather icon image
    // id and description are intentionally omitted
)