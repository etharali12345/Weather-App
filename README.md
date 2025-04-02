# Weather-App
A simple Java Weather App that fetches real-time weather data for a city using an online API, displaying temperature, humidity, wind speed, and conditions.

# How to Use
1. Launch the Application
To open the weather app window, run the following commands. Make sure the JSON JAR file is available
  javac -cp .:org.json-1.6-20240205.jar WeatherApp.java
  java -cp .:org.json-1.6-20240205.jar WeatherApp

 
3. Search for a City
    • In the top left panel, find the input field labeled "Enter City".
    • Type the name of a city (e.g., "London", "Tokyo").
    • Choose a unit of measurement:
        ◦ Metric: Temperature in °C, wind speed in m/s.
        ◦ Imperial: Temperature in °F, wind speed in mph.
    • Click the Search button.
    • The app will verify the city and retrieve the current weather data.

4. Understanding the Display
If the city exists:
Right Panel (Weather Info)
    • Top Section
    • City name.
    • Weather condition icon.
    • Background color changes based on city time (e.g., dark blue at night).
    • Current temperature with unit symbol.
    • Local time of the city.
    • Weather condition (e.g., "Cloudy", "Sunny").
    • Wind speed and humidity.
Forecast Section (Bottom Right)
    • Displays temperatures for:
    • Midnight (00:00).
    • Dawn (06:00).
    • Noon (12:00).
    • Evening (18:00).
Search History (Bottom Left)
Automatically updates with:
City name.
Search timestamp (date and time).

If the city isn't found:
An error message will be displayed.


# Implementation Details
1. GUI Setup
    • WeatherApp Class: Extends JFrame to create the main application window. The constructor initializes the GUI components, layout, and application behavior.
    • Panels:
        ◦ Left Panel: Contains the search field, unit toggle (Metric/Imperial), and search history.
        ◦ Right Panel: Displays weather details and forecasts.
2. User Interaction
    • The Search Button has an ActionListener that:
        1. Retrieves the city name from the input field.
        2. Calls getJSON() to fetch weather data.
        3. Updates the display using displayWeather() and displayForecast().
        4. Adds the search query to history using addSearchToHistory().

3. Data Fetching
    • getJSON() method:
        ◦ Takes the city name and API URL.
        ◦ Sends an HTTP request using HttpURLConnection.
        ◦ Returns weather data as a JSON string.
        ◦ Handles errors (e.g., invalid city).

4. Data Processing
    • displayWeather() method:
        ◦ Extracts key details (temperature, humidity, wind speed, etc.).
        ◦ Converts units if Imperial is selected.
        ◦ Calculates the city's local time.
        ◦ Updates labels on the right panel.

5. Forecast Display
    • displayForecast() method:
        ◦ Extract temperatures for 00:00, 06:00, 12:00, and 18:00 from the JSON data.
        ◦ Converts temperature units to Imperial (°F) or Metric (°C) based on the user's selection.
        ◦ Updates the forecast labels at the bottom with the processed temperatures.
6. Visual Features
    • changeBackground(): Adjusts colors based on the time of day (morning, afternoon, evening, night).
    • getWeatherIcon(): Displays appropriate icons based on weather conditions (sun, clouds, rain, snow, storm) and time of day.

7. Search History
    • addSearchToHistory():
        ◦ Adds a search query with a timestamp to the history list.
        ◦ Tracks and displays past searches with their respective timestamps.
