package com.example.visprog_week7.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WeatherDetailCard(
    title: String,
    value: String,
    iconResId: Int,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        // Use a translucent white for the card background, matching the example UI
        colors = CardDefaults.cardColors(containerColor = Color(0x33FFFFFF)),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = modifier
            .aspectRatio(1f) // Square shape
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = title,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.White
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun WeatherDetailCardPreview() {
    WeatherDetailCard(
        title = "Humidity",
        value = "75%",
        iconResId = android.R.drawable.ic_menu_compass // Placeholder icon
    )
}