package com.example.visprog_week7.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.visprog_week7.dataSoal2.model.AlbumDetail
import com.example.visprog_week7.dataSoal2.model.Track
import com.example.visprog_week7.uiSoal2.viewmodel.AlbumDetailUiState
import com.example.visprog_week7.uiSoal2.viewmodel.ArtistViewModel

// Re-using Gruvbox/Retro Colors
private val GruvboxDarkBg = Color(0xFF282828)
private val GruvboxYellow = Color(0xFFFABD2F)
private val GruvboxGreen = Color(0xFFB8BB26)
private val GruvboxGrey = Color(0xFF928374)
private val GruvboxWhite = Color(0xFFEBDBB2)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    navController: NavController,
    albumId: String,
    viewModel: ArtistViewModel = viewModel()
) {
    val uiState = viewModel.albumDetailUiState

    // Start fetching album details when the screen is composed
    LaunchedEffect(albumId) {
        viewModel.loadAlbumDetails(albumId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Album Detail", color = GruvboxYellow) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = GruvboxYellow
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GruvboxDarkBg)
            )
        },
        containerColor = GruvboxDarkBg
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                AlbumDetailUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
                is AlbumDetailUiState.Error -> ErrorScreen(uiState.message, modifier = Modifier.fillMaxSize())
                is AlbumDetailUiState.Success -> AlbumContent(
                    album = uiState.album,
                    tracks = uiState.tracks,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun AlbumContent(album: AlbumDetail, tracks: List<Track>, viewModel: ArtistViewModel) {
    LazyColumn( // All content uses LazyColumn to ensure scrollability [cite: Soal Week 7 VP.pdf]
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
    ) {
        // --- Album Header Section ---
        item {
            Text(
                text = album.strAlbum ?: "Unknown Album",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = GruvboxYellow,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Album Cover, Year, and Genre
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Album Cover [cite: Soal Week 7 VP.pdf]
                OutlinedCard(
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, GruvboxGrey),
                    modifier = Modifier.size(150.dp)
                ) {
                    AsyncImage(
                        model = album.strAlbumThumb,
                        contentDescription = "Album Cover",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = album.strArtist ?: "N/A", color = GruvboxWhite)
                    Text(text = album.intYearReleased ?: "N/A", color = GruvboxGreen)
                    Text(text = album.strGenre ?: "N/A", color = GruvboxGreen)
                }
            }

            // Description Section [cite: Soal Week 7 VP.pdf]
            OutlinedCard(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = GruvboxDarkBg,
                    contentColor = GruvboxWhite
                ),
                border = BorderStroke(1.dp, GruvboxGrey),
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            ) {
                Text(
                    text = album.strDescriptionEN ?: "No description available.",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp),
                    color = GruvboxWhite
                )
            }

            // Tracks Header
            Text(
                text = "Tracks",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = GruvboxWhite,
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )
            // Header divider
            Divider(color = GruvboxGrey.copy(alpha = 0.5f), thickness = 1.dp)
        }

        // --- Track List Section ---
        items(tracks) { track ->
            TrackItem(
                track = track,
                viewModel = viewModel
            )
            Divider(color = GruvboxGrey.copy(alpha = 0.5f), thickness = 0.5.dp)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TrackItem(track: Track, viewModel: ArtistViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            // Display track number here if it were available, otherwise just name
            text = track.strTrack ?: "Unknown Track",
            fontSize = 16.sp,
            color = GruvboxWhite,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = viewModel.formatDuration(track.intDuration), // Formatted duration [cite: Soal Week 7 VP.pdf]
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
            color = GruvboxGreen
        )
    }
}
