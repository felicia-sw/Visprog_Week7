package com.example.visprog_week7.uiSoal2.view.Navigation

/**
 * Sealed class to define application screen routes, ensuring type safety.
 */
sealed class Screen(val route: String) {
    // Route for the main Artist Detail Screen
    data object ArtistDetail : Screen("artist_detail")

    // Route for the Album Detail Screen, which requires an albumId argument
    data object AlbumDetail : Screen("album_detail/{albumId}") {
        // Helper function to create the route string with the argument filled
        fun createRoute(albumId: String) = "album_detail/$albumId"
    }
}
