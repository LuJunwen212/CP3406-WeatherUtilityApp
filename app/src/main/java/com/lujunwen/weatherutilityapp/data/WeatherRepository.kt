package com.lujunwen.weatherutilityapp.data

import com.lujunwen.weatherutilityapp.model.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepository {

    private val api: WeatherApi = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApi::class.java)

    suspend fun getWeatherForCity(city: String): WeatherResponse {
        val coordinates = getCoordinates(city)

        return api.getCurrentWeather(
            latitude = coordinates.first,
            longitude = coordinates.second
        )
    }

    private fun getCoordinates(city: String): Pair<Double, Double> {
        return when (city) {
            "Singapore" -> Pair(1.3521, 103.8198)
            "Kuala Lumpur" -> Pair(3.1390, 101.6869)
            "Johor Bahru" -> Pair(1.4927, 103.7414)
            else -> Pair(1.3521, 103.8198)
        }
    }
}