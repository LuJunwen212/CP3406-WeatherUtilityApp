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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lujunwen.weatherutilityapp.model.CurrentWeather
import com.lujunwen.weatherutilityapp.ui.theme.WeatherUtilityAppTheme
import com.lujunwen.weatherutilityapp.viewmodel.WeatherViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

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
fun WeatherUtilityApp(
    weatherViewModel: WeatherViewModel = viewModel()
) {
    var selectedScreen by remember { mutableStateOf("Utility") }
    var selectedCity by remember { mutableStateOf("Singapore") }
    var selectedUnit by remember { mutableStateOf("Celsius") }

    val weather by weatherViewModel.weather.collectAsState()
    val isLoading by weatherViewModel.isLoading.collectAsState()
    val errorMessage by weatherViewModel.errorMessage.collectAsState()

    LaunchedEffect(selectedCity) {
        weatherViewModel.loadWeather(selectedCity)
    }

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
                    unit = selectedUnit,
                    weather = weather,
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    onRefresh = {
                        weatherViewModel.loadWeather(selectedCity)
                    }
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
    unit: String,
    weather: CurrentWeather?,
    isLoading: Boolean,
    errorMessage: String?,
    onRefresh: () -> Unit
) {
    val temperatureCelsius = weather?.temperature ?: 0.0
    val humidity = weather?.humidity ?: 0
    val windSpeed = weather?.windSpeed ?: 0.0
    val weatherCode = weather?.weatherCode ?: 0

    val temperatureText = if (weather == null) {
        "--"
    } else if (unit == "Celsius") {
        "${temperatureCelsius.roundToInt()}°C"
    } else {
        val fahrenheit = (temperatureCelsius * 9 / 5) + 32
        "${fahrenheit.roundToInt()}°F"
    }

    val feelsLikeText = if (weather == null) {
        "--"
    } else if (unit == "Celsius") {
        "${(temperatureCelsius + 2).roundToInt()}°C"
    } else {
        val fahrenheit = ((temperatureCelsius + 2) * 9 / 5) + 32
        "${fahrenheit.roundToInt()}°F"
    }

    val condition = getWeatherDescription(weatherCode)

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
                if (isLoading) {
                    Text(
                        text = "Loading weather...",
                        fontSize = 22.sp
                    )
                } else if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        fontSize = 18.sp
                    )
                } else {
                    Text(
                        text = temperatureText,
                        fontSize = 52.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = condition,
                        fontSize = 22.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    WeatherInfoRow(label = "Humidity", value = "$humidity%")
                    WeatherInfoRow(label = "Wind Speed", value = "${windSpeed.roundToInt()} km/h")
                    WeatherInfoRow(label = "Feels Like", value = feelsLikeText)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRefresh
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

fun getWeatherDescription(code: Int): String {
    return when (code) {
        0 -> "Clear Sky"
        1, 2 -> "Partly Cloudy"
        3 -> "Cloudy"
        45, 48 -> "Foggy"
        51, 53, 55 -> "Drizzle"
        61, 63, 65 -> "Rain"
        80, 81, 82 -> "Rain Showers"
        95, 96, 99 -> "Thunderstorm"
        else -> "Unknown Weather"
    }
}