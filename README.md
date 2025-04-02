# Weather-App
A simple Java Weather App that fetches real-time weather data for a city using an online API, displaying temperature, humidity, wind speed, and conditions.

## 👆 How to Use
### Launch the Application: 
To open the weather app window, run the following commands. Make sure the JSON JAR file is available:
  1. Compile the Java file:
        ```bash
        javac -cp .:org.json-1.6-20240205.jar WeatherApp.java
        ```
  2. Run the application:
        ```bash
        java -cp .:org.json-1.6-20240205.jar WeatherApp
        ```
### City Search
- Enter city name in search field (e.g. "Berlin", "Tokyo")
- Select measurement units:
  - **Metric**: °C, m/s
  - **Imperial**: °F, mph
- Click "Search" button

<br><br>

## 🌦️ Weather Display Overview:
**If the city is found, the following information is presented:**
**Current Weather Details:**
- City name and local time
- Temperature with unit (°C/°F)
- Weather condition (e.g. "Rainy", "Sunny")
- Wind speed and humidity
- Dynamic background (changes by time of day)

**Daily Temperature Forecast**
- Displays predicted temperatures for:
  - 00:00 - Midnight temperature
  - 06:00 - Dawn temperature
  - 12:00 - Noon temperature
  - 18:00 - Evening temperature

**Search History:**
- Automatically saves:
  - City names
  - Search date/time

**If the city isn't found:**
An error message will be displayed.

<br><br>

## ⚙️ Implementation Details
**1. GUI Setup**
  - ***WeatherApp Class:*** Extends JFrame to create the main application window. The constructor initializes the GUI components, layout, and application behavior.
  - ***Panels:*** 
    - Left Panel: Contains the search field, unit toggle (Metric/Imperial), and search history.
    - Right Panel: Displays weather details and forecasts.
      
**2. User Interaction**
- ***The Search Button has an ActionListener that:*** 
  - Retrieves the city name from the input field.
  - Calls getJSON() to fetch weather data.
  - Updates the display using displayWeather() and displayForecast().
  - Adds the search query to history using addSearchToHistory().

**3. Data Fetching**
- ***getJSON() method:*** 
  - Takes the city name and API URL.
  - Sends an HTTP request using HttpURLConnection.
  - Returns weather data as a JSON string.
  - Handles errors (e.g., invalid city).

**4. Data Processing**
- ***displayWeather() method:***
  - Extracts key details (temperature, humidity, wind speed, etc.).
  - Converts units if Imperial is selected.
  - Calculates the city's local time.
  - Updates labels on the right panel.

**5. Forecast Display**
- ***displayForecast() method:***
  - Extract temperatures for 00:00, 06:00, 12:00, and 18:00 from the JSON data.
  - Converts temperature units to Imperial (°F) or Metric (°C) based on the user's selection.
  - Updates the forecast labels at the bottom with the processed temperatures.
    
**6. Visual Features**
  - ***changeBackground():*** Adjusts colors based on the time of day (morning, afternoon, evening, night).
  - ***getWeatherIcon():*** Displays appropriate icons based on weather conditions (sun, clouds, rain, snow, storm) and time of day.

**7. Search History**
- ***addSearchToHistory() method:***
  - Adds a search query with a timestamp to the history list.
  - Tracks and displays past searches with their respective timestamps.
