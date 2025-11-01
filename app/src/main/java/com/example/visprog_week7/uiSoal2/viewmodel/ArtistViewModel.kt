package com.example.visprog_week7.uiSoal2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visprog_week7.dataSoal2.repository.ArtistRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.concurrent.TimeUnit
import retrofit2.HttpException


/**
 * ViewModel to manage state and business logic for the Artist Explorer App.
 * Uses Coroutines (viewModelScope.launch, async) for asynchronous API calls and
 * updates observable UI state defined in ArtistUiState.
 */
class ArtistViewModel(
    // The repository is injected here, decoupling the ViewModel from the Service layer
    private val repository: ArtistRepository = ArtistRepository()
) : ViewModel() {

    // Observable state for the main Artist Detail screen
    var artistUiState by mutableStateOf<ArtistUiState>(ArtistUiState.Loading)
        private set

    // Observable state for the Album Detail screen
    var albumDetailUiState by mutableStateOf<AlbumDetailUiState>(AlbumDetailUiState.Loading)
        private set

    init {
        // Load the initial artist data on startup
        loadArtistData("John Mayer")
    }

    /**
     * Loads the artist details and their albums concurrently.
     * Uses Coroutines' async/await pattern for parallel fetching, making the app faster.
     */
    fun loadArtistData(artistName: String) {
        artistUiState = ArtistUiState.Loading
        viewModelScope.launch {
            try {
                coroutineScope {
                    // Fetch artist details and albums in parallel for faster loading
                    val artistResult = async { repository.getArtistDetails(artistName) }
                    val albumsResult = async { repository.getArtistAlbums(artistName) }

                    // Await results, using getOrThrow() to fail fast on failure
                    val artist = artistResult.await().getOrThrow()
                    val albums = albumsResult.await().getOrThrow() // Albums can be empty list

                    // If we reach here, artist is non-null due to repository logic
                    artistUiState = ArtistUiState.Success(artist, albums)
                }
            } catch (e: Exception) {
                handleApiError(e)
            }
        }
    }

    /**
     * Loads the details (AlbumDetail and Tracks) for a specific album ID concurrently.
     */
    fun loadAlbumDetails(albumId: String) {
        albumDetailUiState = AlbumDetailUiState.Loading
        viewModelScope.launch {
            try {
                coroutineScope {
                    val detailResult = async { repository.getAlbumDetails(albumId) }
                    val tracksResult = async { repository.getAlbumTracks(albumId) }

                    val albumDetail = detailResult.await().getOrThrow()
                    val tracks = tracksResult.await().getOrThrow()

                    if (albumDetail != null) {
                        albumDetailUiState = AlbumDetailUiState.Success(albumDetail, tracks)
                    } else {
                        handleApiError(Exception("Album details not found."), isAlbumDetail = true)
                    }
                }
            } catch (e: Exception) {
                handleApiError(e, isAlbumDetail = true)
            }
        }
    }

    /**
     * Utility function to format track duration from milliseconds (String) to minutes:seconds format (m:ss).
     * This fulfills the requirement for track information display.
     */
    fun formatDuration(durationMs: String?): String {
        return try {
            val millis = durationMs?.toLongOrNull() ?: return "N/A"
            val totalSeconds = TimeUnit.MILLISECONDS.toSeconds(millis)
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60

            // Format to m:ss, ensuring seconds are always two digits (e.g., 3:07)
            String.format("%d:%02d", minutes, seconds)
        } catch (e: Exception) {
            "N/A"
        }
    }

    /**
     * Centralized function to set the appropriate Error state for both screens.
     */
    private fun handleApiError(e: Throwable?, isAlbumDetail: Boolean = false) {
        val errorMessage = when (e) {
            is IOException -> "Network Error: Check connection"
            is NoSuchElementException -> "Error: Data not found."
            is HttpException -> "HTTP Error: ${e.code()}" // <--- ADD HttpException handling
            else -> "Error: Unknown error occurred."
        }

        if (isAlbumDetail) {
            albumDetailUiState = AlbumDetailUiState.Error("Oops! Something went wrong\n$errorMessage")
        } else {
            artistUiState = ArtistUiState.Error("Oops! Something went wrong\n$errorMessage")
        }
    }
}
