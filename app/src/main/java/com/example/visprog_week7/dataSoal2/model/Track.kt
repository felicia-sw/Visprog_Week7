package com.example.visprog_week7.dataSoal2.model

import com.google.gson.annotations.SerializedName

/**
 * Top-level response wrapper for the "track.php" endpoint.
 */
data class TrackResponse(
    @SerializedName("track") val tracks: List<Track>?
)

/**
 * Model representing a single track within an album.
 */
data class Track(
    @SerializedName("idTrack") val idTrack: String?,
    @SerializedName("strTrack") val strTrack: String?, // Track Name
    @SerializedName("intDuration") val intDuration: String? // Duration in milliseconds (as a String)
)
