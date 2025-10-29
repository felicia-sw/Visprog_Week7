package com.example.visprog_week7.dataSoal1.di

import com.example.visprog_week7.dataSoal1.service.WeatherService
import com.example.visprog_week7.utilSoal1.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val weatherService: WeatherService by lazy {
        retrofit.create(WeatherService::class.java)
    }
}