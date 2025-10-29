package com.example.visprog_week7.dataSoal2.Container

import com.example.visprog_week7.dataSoal2.service.ArtistService
import com.example.visprog_week7.utilSoal2.ArtistConstants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//object = defines this class as an object [a singleton] which only exist one instance of this container in the entire app, guaranteeing that only one retrofit client is ever created
object Container {
    private val retrofit:Retrofit = Retrofit.Builder()
        .baseUrl(ArtistConstants.BASE_URL) // connection point 1
        .addConverterFactory(GsonConverterFactory.create()) // tells retrofit to use GSON to automatically convert raw json INTO YOUR mODEL/DTO classes
        .build()
    val artistService: ArtistService by lazy {
        retrofit.create(ArtistService::class.java) // connection point 2; takes the configures retrofit engine and produces a concretem working implementation of the ArtistService interface [just blueprint]
    }
}