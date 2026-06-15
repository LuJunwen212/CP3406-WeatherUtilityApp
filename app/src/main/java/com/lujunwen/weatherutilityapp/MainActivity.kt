package com.lujunwen.weatherutilityapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lujunwen.weatherutilityapp.ui.theme.WeatherUtilityAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WeatherUtilityAppTheme {
                WeatherUtilityApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherUtilityApp() {
    var selectedScreen by remember { mutableStateOf("Utility") }
    var selectedCity by remember { mutableStateOf("Singapore") }
    var selectedUnit by remember { mutableStateOf("Celsius") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Weather Utility App")
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedScreen == "Utility",
                    onClick = { selectedScreen = "Utility" },
                    icon = { Text(text = "☀️") },
                    label = { Text(text = "Utility") }
                )

                NavigationBarItem(
                    selected = selectedScreen == "Settings",
                    onClick = { selectedScreen = "Settings" },
                    icon = { Text(text = "⚙️") },
                    label = { Text(text = "Settings") }
                )
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            if (selectedScreen == "Utility") {
                UtilityScreen(
                    city = selectedCity,
                    unit = selectedUnit
                )
            } else {
                SettingsScreen(
                    selectedCity = selectedCity,
                    selectedUnit = selectedUnit,
                    onCityChange = { selectedCity = it },
                    onUnitChange = { selectedUnit = it }
                )
            }
        }
    }
}

@Composable
fun UtilityScreen(
    city: String,
    unit: String
) {
    val temperatureCelsius = when (city) {
        "Singapore" -> 31
        "Kuala Lumpur" -> 29
        "Johor Bahru" -> 30
        else -> 31
    }

    val feelsLikeCelsius = when (city) {
        "Singapore" -> 34
        "Kuala Lumpur" -> 32
        "Johor Bahru" -> 33
        else -> 34
    }

    val temperature = if (unit == "Celsius") {
        "$temperatureCelsius°C"
    } else {
        "${(temperatureCelsius * 9 / 5) + 32}°F"
    }

    val feelsLike = if (unit == "Celsius") {
        "$feelsLikeCelsius°C"
    } else {
        "${(feelsLikeCelsius * 9 / 5) + 32}°F"
    }

    val condition = when (city) {
        "Singapore" -> "Partly Cloudy"
        "Kuala Lumpur" -> "Thunderstorms"
        "Johor Bahru" -> "Cloudy"
        else -> "Partly Cloudy"
    }

    val humidity = when (city) {
        "Singapore" -> "78%"
        "Kuala Lumpur" -> "82%"
        "Johor Bahru" -> "80%"
        else -> "78%"
    }

    val windSpeed = when (city) {
        "Singapore" -> "12 km/h"
        "Kuala Lumpur" -> "9 km/h"
        "Johor Bahru" -> "10 km/h"
        else -> "12 km/h"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = city,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = temperature,
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = condition,
                    fontSize = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                WeatherInfoRow(label = "Humidity", value = humidity)
                WeatherInfoRow(label = "Wind Speed", value = windSpeed)
                WeatherInfoRow(label = "Feels Like", value = feelsLike)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // API refresh function will be added later.
            }
        ) {
            Text(text = "Refresh Weather")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "At-a-glance weather information for daily planning.",
            fontSize = 14.sp
        )
    }
}

@Composable
fun WeatherInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)

        Text(
            text = value,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SettingsScreen(
    selectedCity: String,
    selectedUnit: String,
    onCityChange: (String) -> Unit,
    onUnitChange: (String) -> Unit
) {
    val cities = listOf("Singapore", "Kuala Lumpur", "Johor Bahru")
    val units = listOf("Celsius", "Fahrenheit")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Settings",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Select City",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        cities.forEach { city ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedCity == city,
                    onClick = { onCityChange(city) }
                )

                Text(text = city)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Temperature Unit",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        units.forEach { unit ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedUnit == unit,
                    onClick = { onUnitChange(unit) }
                )

                Text(text = unit)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "These settings control the city and temperature unit shown on the main utility screen."
        )
    }
}