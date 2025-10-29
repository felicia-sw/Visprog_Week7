package com.example.visprog_week7.dataSoal2.service

import retrofit2.http.GET
import retrofit2.http.Query

// here is where we can translate the four required endpoints into a kotlin interface that retrofit can understand and use
// this interface lives in the services package

interface ArtistService {
    @GET("search.php") // the file name here is the endpoint
    suspend fun searchArtist(
        @Query("s") artistName: String // based on the Cari artis: search.php?s={artistName}
    ): String

    @GET("searchalbum.php")
    suspend fun searchAlbums(
        @Query("s") artistName: String // based on the Cari album: searchalbum.php?s={artistName}
    ): String

    @GET("album.php")
    suspend fun getAlbumDetails(
        @Query("m") albumId: String // based on the Detail album: album.php?m={albumId}
    ): String

    @GET("track.php")
    suspend fun getAlbumTrack(
        @Query("m") albumId: String
    ): String // based on the Daftar lagu: track.php?m={albumId}
}