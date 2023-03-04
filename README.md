# GeoWeather App

This is an Android app that connects to the OpenWeather API to fetch and display weather information for the user's current location. The app has two tabs: one for the current weather and another for a list of weather history.

## Getting Started

To use this app, you will need to register for a free Public API KEY from OpenWeather. Once you have obtained your key, replace the `API_KEY` constant in the `MainActivity` class with your own key.

### Prerequisites

- Android Studio 4.1 or higher
- Android SDK 21 or higher

### Installing

1. Clone this repository to your local machine using `git clone https://github.com/gcrnj/geo_weather.git`.
2. Open the project in Android Studio.
3. Go to your `local.properties`
3. Enter your api key using `WEATHER_API_KEY=your_api_key_here`
4. Run the app on an emulator or physical device.

### Registering an Account

To register an account, follow these steps:

1. Click the "Sign Up" button on the welcome screen.
2. Fill in your registration details, including your first name, last name, email, mobile number, and password.
3. Click the "Register" button.

### Logging In

To log in to your account, follow these steps:

1. Click the "Log In" button on the welcome screen.
2. Fill in your email and password.
3. Click the "Log In" button.

### Testing

To run the app's unit tests, follow these steps:

1. Open the project in Android Studio.
2. Open the "UserTesting.kt" file located in the "com.gtech.geoweather" package.
3. Run the tests using the Android Studio test runner.

## Features

- Registration and sign-in functionality
- Display of current location (city and country)
- Display of current temperature in Celsius
- Display of time of sunrise and sunset
- Icon indicating current weather conditions (sun for sunny, rain for rainy, moon for past 6PM)

## Libraries Used

- Retrofit: For making HTTP requests to the OpenWeather API.
- Gson: For parsing the JSON response from the API.
- Glide: For loading and caching images of weather conditions icons.

## Architecture

This app uses the Model-View-ViewModel (MVVM) architecture pattern, with the following components:

- Repository: Acts as a single source of truth for data, abstracting the data source and exposing it to the ViewModel.
- ViewModel: Provides data to the View and handles user interactions. Communicates with the Repository to retrieve and update data.
- View: Displays data and handles user input.

## Security Measures

- API key is stored in a constant in the app code, rather than being hard-coded in the HTTP request.
- User passwords are hashed and salted using the SHA-256 algorithm before being stored in the local database.

## Acknowledgments

- [OpenWeather](https://openweathermap.org/api) for providing the weather data API.
- [FlatIcon](https://www.flaticon.com/) for providing the weather conditions icons.
- [Google](https://developer.android.com/) for providing the Android development platform and Jetpack libraries.
