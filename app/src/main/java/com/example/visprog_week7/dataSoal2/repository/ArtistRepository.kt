package com.example.visprog_week7.dataSoal2.repository

import com.example.visprog_week7.dataSoal2.Container.Container
import com.example.visprog_week7.dataSoal2.model.*
import com.example.visprog_week7.dataSoal2.service.ArtistService
import java.io.IOException

/**
 * Repository acts as the data broker for the Artist Explorer app.
 * It manages calls to the remote data source (ArtistService) and handles basic error wrapping,
 * returning a clean Kotlin Result<T> object to the ViewModel.
 */
class ArtistRepository(
    // Injects the service instance from the dependency container (ArtistNetworkModule)
    private val service: ArtistService = Container.artistService
) {
    /**
     * Helper function to safely execute an API call within a try-catch block.
     * This translates low-level network/parsing errors into a general Result failure.
     */
    private suspend fun <T> safeApiCall(call: suspend () -> T): Result<T> {
        return try {
            val result = call()
            Result.success(result)
        } catch (e: IOException) {
            // Handle network-related exceptions (e.g., no internet connection, DNS failure)
            Result.failure(e)
        } catch (e: Exception) {
            // Handle other generic exceptions (e.g., non-404 HTTP errors, parsing errors)
            Result.failure(e)
        }
    }

    /**
     * Fetches the main artist details (Artist) using the search endpoint.
     */
    suspend fun getArtistDetails(artistName: String): Result<Artist?> {
        // Calls the service, and takes the first artist found (or null if the list is empty)
        return safeApiCall { service.searchArtist(artistName).artists?.firstOrNull() }
    }

    /**
     * Fetches the list of all albums (AlbumSummary) for a given artist.
     */
    suspend fun getArtistAlbums(artistName: String): Result<List<AlbumSummary>> {
        // Returns an empty list if albums are null, ensuring the ViewModel always gets a list.
        return safeApiCall { service.searchAlbums(artistName).albums ?: emptyList() }
    }

    /**
     * Fetches detailed information for a single album (AlbumDetail) by ID.
     */
    suspend fun getAlbumDetails(albumId: String): Result<AlbumDetail?> {
        return safeApiCall { service.getAlbumDetails(albumId).albums?.firstOrNull() }
    }

    /**
     * Fetches the track list (Track) for a single album by ID.
     */
    suspend fun getAlbumTracks(albumId: String): Result<List<Track>> {
        return safeApiCall { service.getAlbumTrack(albumId).tracks ?: emptyList() }
    }
}
