package com.example.visprog_week7.dataSoal2.repository

import com.example.visprog_week7.dataSoal2.Container.Container
import com.example.visprog_week7.dataSoal2.model.*
import com.example.visprog_week7.dataSoal2.service.ArtistService
import java.io.IOException
import retrofit2.HttpException // FIX: Added import for correct error handling

/**
 * Repository acts as the data broker for the Artist Explorer app.
 * It manages calls to the remote data source (ArtistService) and handles basic error wrapping,
 * returning a clean Kotlin Result<T> object to the ViewModel.
 */
class ArtistRepository(
    // Injects the service instance from the dependency container (Container)
    private val service: ArtistService = Container.artistService
) {
    /**
     * Helper function to safely execute an API call within a try-catch block.
     * This translates low-level network/parsing errors into a general Result failure.
     */
    // 1. Updated safeApiCall to catch HttpException
    private suspend fun <T> safeApiCall(call: suspend () -> T): Result<T> {
        return try {
            val result = call()
            Result.success(result)
        } catch (e: IOException) {
            // Handle network-related exceptions
            Result.failure(e)
        } catch (e: HttpException) { // <--- Catch HTTP errors
            Result.failure(e)
        } catch (e: Exception) {
            // Handle other generic exceptions
            Result.failure(e)
        }
    }

    /**
     * Fetches the main artist details (Artist). Throws if not found.
     */
    // 2. Updated return type to non-nullable Artist and added null-check
    suspend fun getArtistDetails(artistName: String): Result<Artist> { // <-- CHANGED from Artist? to Artist
        return safeApiCall {
            // If the list is empty or null, throw NoSuchElementException
            service.searchArtist(artistName).artists?.firstOrNull()
                ?: throw NoSuchElementException("Artist not found.")
        }
    }

    /**
     * Fetches the list of all albums (AlbumSummary) for a given artist.
     */
    suspend fun getArtistAlbums(artistName: String): Result<List<AlbumSummary>> {
        // No change needed here, as empty list is an acceptable result.
        return safeApiCall { service.searchAlbums(artistName).albums ?: emptyList() }
    }

    /**
     * Fetches detailed information for a single album (AlbumDetail) by ID.
     */
    // 3. Updated return type to non-nullable AlbumDetail and added null-check
    suspend fun getAlbumDetails(albumId: String): Result<AlbumDetail> { // <-- CHANGED from AlbumDetail? to AlbumDetail
        return safeApiCall {
            service.getAlbumDetails(albumId).albums?.firstOrNull()
                ?: throw NoSuchElementException("Album details not found.")
        }
    }

    /**
     * Fetches the track list (Track) for a single album by ID.
     */
    suspend fun getAlbumTracks(albumId: String): Result<List<Track>> {
        return safeApiCall { service.getAlbumTrack(albumId).tracks ?: emptyList() }
    }
}
