package com.example.visprog_week7.dataSoal2.model

import com.google.gson.annotations.SerializedName

/**
 * Top-level response wrapper for the "searchalbum.php" endpoint.
 */
data class AlbumSearchResponse(
    @SerializedName("album") val albums: List<AlbumSummary>?
)

/**
 * Model representing a summary of an album, used for the Grid display on the Artist screen.
 */
data class AlbumSummary(
    @SerializedName("idAlbum") val idAlbum: String,
    @SerializedName("strAlbum") val strAlbum: String,
    @SerializedName("intYearReleased") val intYearReleased: String?, // Year released
    @SerializedName("strAlbumThumb") val strAlbumThumb: String? // Cover image thumbnail
)