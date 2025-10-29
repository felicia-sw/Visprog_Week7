package com.example.visprog_week7.dataSoal1.model
import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("speed") val speed: Double // Wind speed (e.g., 2.0)
    // deg and gust are intentionally omitted
)