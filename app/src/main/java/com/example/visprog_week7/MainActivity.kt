package com.example.visprog_week7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.visprog_week7.uiSoal1.theme.Visprog_Week7Theme
import com.example.visprog_week7.uiSoal1.view.WeatherScreen
import com.example.visprog_week7.uiSoal2.navigation.Screen
import com.example.visprog_week7.uiSoal2.view.AlbumDetailScreen
import com.example.visprog_week7.uiSoal2.view.ArtistDetailScreen

/**
 * Main Activity serves as the entry point and hosts the AppSelector to switch between Soal 1 and Soal 2.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Visprog_Week7Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF282828) // Dark background for the selector screen
                ) {
                    AppSelector()
                }
            }
        }
    }
}

/**
 * Composable function to allow the user to select which project (Soal) to run.
 */
@Composable
fun AppSelector() {
    var selectedApp by remember { mutableStateOf<String?>(null) }

    // Choose which app to display based on user selection
    when (selectedApp) {
        "SoalAaron" -> com.example.visprog_week7.uiSoal1.view.WeatherScreen() // Note: Need full path since this component is in Soal1's View package
        "SoalHayya" -> ArtistExplorerNavHost()
        else -> SelectorScreen(onSelect = { selectedApp = it })
    }
}

@Composable
fun SelectorScreen(onSelect: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Select Application to Run",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White.copy(alpha = 0.9f),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onSelect("SoalAaron") }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Text("1. PanPan Weather App (Soal Aaron)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onSelect("SoalHayya") }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Text("2. Artist Explorer App (Soal Hayya)")
        }
    }
}


// NavHost function for Soal Hayya (unchanged logic)
@Composable
fun ArtistExplorerNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.ArtistDetail.route
    ) {
        composable(route = Screen.ArtistDetail.route) {
            ArtistDetailScreen(navController = navController)
        }

        composable(
            route = Screen.AlbumDetail.route,
            arguments = listOf(navArgument("albumId") { type = NavType.StringType })
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getString("albumId")
            if (albumId != null) {
                AlbumDetailScreen(navController = navController, albumId = albumId)
            } else {
                Text("Error: Album ID missing", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Visprog_Week7Theme {
        SelectorScreen(onSelect = {})
    }
}
