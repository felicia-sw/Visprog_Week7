package com.example.visprog_week7.uiSoal1.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.visprog_week7.R
import com.example.visprog_week7.dataSoal1.model.WeatherResponseDto
import com.example.visprog_week7.uiSoal1.viewmodel.WeatherUiState
import com.example.visprog_week7.uiSoal1.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = viewModel()
) {
    val uiState = viewModel.uiState
    val cityQuery = viewModel.searchQuery

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.weather___home_2),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Top Search Bar Section - Does NOT scroll
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Menu Icon (Placeholder for functionality)
                IconButton(onClick = { /* TODO: Implement Menu */ }) {
                    Icon(
                        Icons.Outlined.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }

                // Search TextField
                OutlinedTextField(
                    value = cityQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    placeholder = { Text("Enter city name...", color = Color.White.copy(alpha = 0.7f)) },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = Color.White.copy(alpha = 0.7f)
                        )
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { viewModel.searchWeather() }),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0x33FFFFFF),
                        unfocusedContainerColor = Color(0x33FFFFFF),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                )

                // Search Button
                Button(
                    onClick = viewModel::searchWeather,
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FFFFFF)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
                    modifier = Modifier.height(IntrinsicSize.Max)
                ) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }
            }

            // Main Content Area - MUST be scrollable (LazyColumn)
            when (uiState) {
                WeatherUiState.Initial -> InitialScreen()
                WeatherUiState.Loading -> LoadingScreen()
                is WeatherUiState.Success -> WeatherContent(uiState.weatherData, viewModel)
                is WeatherUiState.Error -> ErrorScreen(uiState.message)
            }
        }
    }
}

@Composable
fun InitialScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Filled.Search,
            contentDescription = "Search",
            tint = Color.White.copy(alpha = 0.5f),
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Search for a city to get started",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 18.sp
        )
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
        Text(text = "Loading...", color = Color.White, modifier = Modifier.padding(top = 80.dp))
    }
}

@Composable
fun ErrorScreen(errorMessage: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Red triangle as in the example
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color.Red, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "!", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = errorMessage,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun WeatherContent(weatherData: WeatherResponseDto, viewModel: WeatherViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            // Location and Date
            Text(
                text = "📍 ${weatherData.name}",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = viewModel.getFormattedDate(weatherData.dt),
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 18.sp
            )
            Text(
                text = "Updated as of ${viewModel.getFormattedUpdateTime(weatherData.dt)}",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Current Weather & Panda
            val weatherMain = weatherData.weather.firstOrNull()?.main ?: ""
            val weatherIconId = weatherData.weather.firstOrNull()?.icon ?: ""
            val weatherCondition = viewModel.getWeatherCondition(weatherMain)
            val pandaResId = when (weatherCondition) {
                "Clear" -> R.drawable.blue_and_black_bold_typography_quote_poster_3 // Clear Panda
                "Rain" -> R.drawable.blue_and_black_bold_typography_quote_poster_2   // Rain Panda
                "Clouds" -> R.drawable.blue_and_black_bold_typography_quote_poster   // Clouds Panda
                else -> R.drawable.blue_and_black_bold_typography_quote_poster_3 // Default to Clear
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Weather Icon from API
                    val iconUrl = "https://openweathermap.org/img/wn/$weatherIconId@2x.png"
                    Image(
                        painter = rememberAsyncImagePainter(iconUrl),
                        contentDescription = weatherCondition,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = weatherCondition,
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Light
                    )
                }

                // Temperature
                Text(
                    text = "${weatherData.main.temp.toInt()}°C",
                    color = Color.White,
                    fontSize = 100.sp,
                    fontWeight = FontWeight.Bold
                )

                // Panda Image
                Image(
                    painter = painterResource(id = pandaResId),
                    contentDescription = "Weather Panda",
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Weather Details Grid
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                // Row 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Humidity
                    WeatherDetailCard(
                        title = "HUMIDITY",
                        value = "${weatherData.main.humidity}%",
                        iconResId = R.drawable.icon_humidity,
                        modifier = Modifier.weight(1f)
                    )
                    // Wind
                    WeatherDetailCard(
                        title = "WIND",
                        value = "${weatherData.wind.speed.toInt()}km/h",
                        iconResId = R.drawable.icon_wind,
                        modifier = Modifier.weight(1f)
                    )
                    // Feels Like
                    WeatherDetailCard(
                        title = "FEELS LIKE",
                        value = "${weatherData.main.feelsLike.toInt()}°",
                        iconResId = R.drawable.icon_feels_like,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Row 2 (Rainfall and Cloudiness are placeholders as the DTO omits them, following UI example)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Rain Fall (Using placeholder as `rain` field is omitted in model)
                    WeatherDetailCard(
                        title = "RAIN FALL",
                        value = "0.0 mm",
                        iconResId = R.drawable.vector_2, // Umbrella/Rain icon
                        modifier = Modifier.weight(1f)
                    )
                    // Pressure
                    WeatherDetailCard(
                        title = "PRESSURE",
                        value = "${weatherData.main.pressure}hPa",
                        iconResId = R.drawable.devices, // Placeholder icon
                        modifier = Modifier.weight(1f)
                    )
                    // Cloudiness (Using placeholder)
                    WeatherDetailCard(
                        title = "CLOUDINESS",
                        value = "${weatherData.weather.firstOrNull()?.let { "" } ?: "N/A"}", // Minimal value as `clouds` data is missing in DTO
                        iconResId = R.drawable.cloud,
                        modifier = Modifier.weight(1f)
                    )
                }
            }


            Spacer(modifier = Modifier.height(32.dp))

            // Sunrise and Sunset
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sunrise
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.vector), // Sunrise icon
                        contentDescription = "Sunrise",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(text = "SUNRISE", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    Text(
                        text = viewModel.getFormattedTime(weatherData.sys.sunrise),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Sunset
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.vector_21png), // Sunset icon
                        contentDescription = "Sunset",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(text = "SUNSET", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    Text(
                        text = viewModel.getFormattedTime(weatherData.sys.sunset),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun WeatherScreenPreview() {
    WeatherScreen(
        viewModel = WeatherViewModel()
    )
}