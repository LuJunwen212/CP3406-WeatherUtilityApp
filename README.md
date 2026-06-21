# CP3406 Weather Utility App

## Overview

Weather Utility App is a simple Android utility application developed for CP3406 Assessment 1.

The application provides quick, at-a-glance weather information for selected cities. Users can view the current temperature, apparent temperature, humidity, wind speed, and general weather conditions.

The app also includes a settings screen that allows users to change the selected city and switch between Celsius and Fahrenheit.

## Features

* Displays live current weather information
* Shows current temperature
* Shows real apparent temperature, also known as “Feels Like”
* Displays humidity
* Displays wind speed
* Displays a weather condition description
* Supports three cities:

  * Singapore
  * Kuala Lumpur
  * Johor Bahru
* Supports Celsius and Fahrenheit
* Includes a manual refresh button
* Includes a Utility screen and a Settings screen
* Uses bottom navigation for simple screen switching
* Displays an error message when weather data cannot be loaded

## Technologies Used

* Kotlin
* Android Studio
* Jetpack Compose
* Material Design 3
* Retrofit
* Gson Converter
* Kotlin Coroutines
* StateFlow
* ViewModel
* Repository Pattern
* Open-Meteo Weather API
* Git and GitHub

## Application Architecture

The application uses a basic modern Android architecture.

### Model

The model classes represent weather data returned by the Open-Meteo API.

Main model files include:

* `WeatherResponse`
* `CurrentWeather`

### API Service

`WeatherApi` defines the Retrofit request used to retrieve current weather information from Open-Meteo.

The application requests:

* Temperature
* Apparent temperature
* Relative humidity
* Wind speed
* Weather code

### Repository

`WeatherRepository` manages communication between the API service and the ViewModel.

It also stores the latitude and longitude coordinates for each supported city.

### ViewModel

`WeatherViewModel` manages weather data and UI state.

It handles:

* Loading weather information
* Loading state
* Error state
* Updating weather data after a city change
* Refreshing weather data

### User Interface

The user interface is built entirely with Jetpack Compose and Material Design 3.

The application contains two main screens:

#### Utility Screen

The Utility screen displays current weather information in a clear card-based layout.

#### Settings Screen

The Settings screen allows the user to:

* Select a city
* Select Celsius or Fahrenheit

The selected settings immediately affect the information shown on the Utility screen.

## Supported Cities

| City         | Country   |
| ------------ | --------- |
| Singapore    | Singapore |
| Kuala Lumpur | Malaysia  |
| Johor Bahru  | Malaysia  |

## Weather API

This application uses the Open-Meteo API.

Open-Meteo provides current weather information without requiring an API key.

API base URL:

```text
https://api.open-meteo.com/
```

## Project Structure

```text
com.lujunwen.weatherutilityapp
├── data
│   ├── WeatherApi.kt
│   └── WeatherRepository.kt
├── model
│   └── WeatherResponse.kt
├── viewmodel
│   └── WeatherViewModel.kt
├── ui.theme
│   ├── Color.kt
│   ├── Theme.kt
│   └── Type.kt
└── MainActivity.kt
```

## How to Run the Application

1. Clone or download this repository.
2. Open the project in Android Studio.
3. Allow Android Studio to complete the Gradle sync.
4. Create or select an Android emulator.
5. Run the application using the Run button.

The application can also be installed on a physical Android phone using USB debugging or an APK file.

## Minimum Requirements

* Android Studio
* Android SDK
* Android 7.0 or later
* Internet connection for retrieving live weather data

## Screenshots

Screenshots of the Utility screen and Settings screen are included separately in the assessment submission documentation.

## Development Progress

The project was developed using regular Git commits to demonstrate continuous progress.

Major development stages included:

* Initial Android project setup
* Utility and Settings screen development
* Bottom navigation implementation
* Retrofit API integration
* ViewModel and Repository implementation
* Real apparent temperature support
* README documentation

## Known Limitations

* Only three cities are currently supported.
* Settings are not permanently saved after the application is closed.
* An internet connection is required to retrieve current weather information.
* The app does not currently provide a multi-day forecast.

## Future Improvements

Possible future improvements include:

* Adding more cities
* Adding location-based weather
* Saving settings using DataStore
* Adding hourly and multi-day forecasts
* Improving error and loading displays
* Adding weather icons
* Adding dark mode settings

## Author

Lu Junwen

CP3406 Assessment 1
Weather Utility App
