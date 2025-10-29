package com.example.visprog_week7.dataSoal2.model

import com.google.gson.annotations.SerializedName


data class ArtistResponse(
    @SerializedName("artists") val artists: List<Artist>?
)
//Halaman artis menampilkan:
//●
//Nama artis, genre, banner/thumb artis, dan biografi singkat.
data class Artist(
    @SerializedName("idArtist") val idArtist: String?, // pbtained from postman; json data
    @SerializedName("strArtist") val strArtist: String?,
    @SerializedName("strGenre") val strGenre: String?,
    @SerializedName("strBiographyEN") val strBiographyEN: String?,
    @SerializedName("strArtistThumb") val strArtistThumb: String?,
    @SerializedName("strArtistBanner") val strArtistBanner: String?

)