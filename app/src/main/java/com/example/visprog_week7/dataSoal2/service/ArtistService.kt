package com.example.visprog_week7.dataSoal2.service

import com.example.visprog_week7.dataSoal2.model.AlbumDetailResponse
import com.example.visprog_week7.dataSoal2.model.AlbumSearchResponse
import com.example.visprog_week7.dataSoal2.model.ArtistResponse
import com.example.visprog_week7.dataSoal2.model.TrackResponse
import retrofit2.http.GET
import retrofit2.http.Query

// here is where we can translate the four required endpoints into a kotlin interface that retrofit can understand and use
// this interface lives in the services package

interface ArtistService {
    @GET("search.php") // the file name here is the endpoint
    suspend fun searchArtist(
        @Query("s") artistName: String // based on the Cari artis: search.php?s={artistName}
    ): ArtistResponse

    @GET("searchalbum.php")
    suspend fun searchAlbums(
        @Query("s") artistName: String // based on the Cari album: searchalbum.php?s={artistName}
    ): AlbumSearchResponse

    @GET("album.php")
    suspend fun getAlbumDetails(
        @Query("m") albumId: String // based on the Detail album: album.php?m={albumId}
    ): AlbumDetailResponse

    @GET("track.php")
    suspend fun getAlbumTrack(
        @Query("m") albumId: String
    ): TrackResponse // based on the Daftar lagu: track.php?m={albumId}
}