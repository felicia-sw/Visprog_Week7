package com.example.visprog_week7.uiSoal2.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.visprog_week7.dataSoal2.model.AlbumSummary
import com.example.visprog_week7.dataSoal2.model.Artist
import com.example.visprog_week7.uiSoal2.navigation.Screen // FIX: Corrected import
import com.example.visprog_week7.uiSoal2.viewmodel.ArtistUiState
import com.example.visprog_week7.uiSoal2.viewmodel.ArtistViewModel

// Define a dark color scheme to match the 'retro / gruvbox' requirement
private val PrimaryDark = Color(0xFF282828)
private val CardColor = Color(0xFF323030)
private val TextColor = Color(0xFFFBF1C7)
private val AccentColor = Color(0xFFFE8019)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistDetailScreen(
    navController: NavController,
    viewModel: ArtistViewModel = viewModel()
) {
    val uiState = viewModel.artistUiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Artist Explorer", color = TextColor) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryDark),
                navigationIcon = {
                    // Back button, though on the main screen it might just exit the app.
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextColor)
                    }
                }
            )
        },
        containerColor = PrimaryDark // Set background color for the scaffold
    ) { paddingValues ->
        // Handle the three states defined in ArtistUiState
        when (uiState) {
            ArtistUiState.Loading -> LoadingScreen(modifier = Modifier.padding(paddingValues))
            is ArtistUiState.Error -> ErrorScreen(uiState.message, modifier = Modifier.padding(paddingValues))
            is ArtistUiState.Success -> ArtistContent(
                artist = uiState.artist,
                albums = uiState.albums,
                onAlbumClick = { albumId ->
                    // Navigation logic: when an album is clicked, navigate to AlbumDetail
                    navController.navigate(Screen.AlbumDetail.createRoute(albumId))
                },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun ArtistContent(
    artist: Artist,
    albums: List<AlbumSummary>,
    onAlbumClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Enable overall scrolling
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- 1. Artist Banner/Thumbnail ---
        OutlinedCard( // Use OutlinedCard for design compliance [cite: Soal Week 7 VP.pdf]
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.outlinedCardColors(containerColor = CardColor),
            border = BorderStroke(1.dp, CardColor), // Using solid color border for a clean look
            modifier = Modifier.fillMaxWidth().height(250.dp)
        ) {
            Box(Modifier.fillMaxSize()) {
                // Coil's AsyncImage for loading external URLs
                AsyncImage(
                    model = artist.strArtistBanner ?: artist.strArtistThumb, // Banner first, fallback to thumbnail
                    contentDescription = "${artist.strArtist} Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Name and Genre Overlay (Bottom Left)
                Column(
                    modifier = Modifier.align(Alignment.BottomStart)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(12.dp)
                ) {
                    Text(
                        text = artist.strArtist ?: "Unknown Artist",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextColor
                    )
                    Text(
                        text = artist.strGenre ?: "Unknown Genre",
                        fontSize = 16.sp,
                        color = AccentColor
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // --- 2. Biography Section ---
        Text(
            text = "Biography",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextColor,
            modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
        )
        OutlinedCard(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.outlinedCardColors(containerColor = CardColor),
            border = BorderDefaults.outlinedCardBorder(true, CardColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = artist.strBiographyEN ?: "No biography available.",
                color = TextColor.copy(alpha = 0.8f),
                fontSize = 14.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        // --- 3. Albums Grid Header ---
        Text(
            text = "Albums",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextColor,
            modifier = Modifier.align(Alignment.Start).padding(bottom = 12.dp)
        )

        // --- 4. Albums Grid (LazyVerticalGrid) ---
        // We constrain the height of the grid inside the verticalScrollable column.
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Required LazyVerticalGrid [cite: Soal Week 7 VP.pdf]
            contentPadding = PaddingValues(0.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .heightIn(min = 200.dp, max = 800.dp) // Constrain height to allow the parent column to scroll
                .fillMaxWidth()
        ) {
            items(albums, key = { it.idAlbum }) { album ->
                AlbumGridItem(album = album, onClick = onAlbumClick)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun AlbumGridItem(album: AlbumSummary, onClick: (String) -> Unit) {
    OutlinedCard( // Use OutlinedCard for design compliance [cite: Soal Week 7 VP.pdf]
        onClick = { onClick(album.idAlbum) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = CardColor),
        border = BorderDefaults.outlinedCardBorder(true, CardColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = album.strAlbumThumb,
                contentDescription = "${album.strAlbum} Cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = album.strAlbum,
                color = TextColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            Text(
                text = album.intYearReleased ?: "N/A",
                color = TextColor.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        }
    }
}

// Common helper composables for Loading and Error screens were already provided in the previous step and are referenced here.
// LoadingScreen and ErrorScreen need to be defined once in a common location or in one of the view files.
// For now, I'll assume they are defined in ArtistDetailScreen.kt to simplify dependencies, but ideally they'd be in a utils file.

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AccentColor) // Show loading indicator [cite: Soal Week 7 VP.pdf]
            Text(text = "Loading...", color = TextColor, modifier = Modifier.padding(top = 16.dp))
        }
    }
}

@Composable
fun ErrorScreen(errorMessage: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize().padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = errorMessage,
            color = Color.Red,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}
