
import java.awt.Color;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.JLabel;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.UIManager;
import java.awt.Component;



public class WeatherApp extends JFrame {
	// Data-related fields
	private List<String> historyDataList = new ArrayList<>();
	private DefaultListModel<String> historyListModel = new DefaultListModel<>();
	private static final String API_KEY = "7c1a7d84388976d9091d1b9efea3af42";
	private static final String CURRENT_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";
	private static final String FORECAST_URL = "https://api.openweathermap.org/data/2.5/forecast?q=%s&appid=%s&units=metric";
	
	// GUI components
	private JPanel contentPane, weatherAppPanel, cityPanel, forecastPanel; 
	private JTextField inputField;
	private JLabel cityLbl, iconLbl, tempResultLbl, timeResutLbl, windResultLbl, humidityResultLbl, conditionResultLbl;
	private JLabel temp1Lbl, temp2Lbl, temp3Lbl, temp4Lbl;
	private JRadioButton metricBtn, imperialBtn;
	private JList<String> historyJList;
	private List<JLabel> Labels = new ArrayList<>();
	
	// Serial Version UID
	private static final long serialVersionUID = 1L;
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WeatherApp frame = new WeatherApp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}//Main END

	
	public WeatherApp() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 734, 543);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(51, 51, 51));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Right Panel and It's Component
		weatherAppPanel = new JPanel();
		weatherAppPanel.setBackground(new Color(51, 51, 51));
		weatherAppPanel.setBounds(12, 12, 204, 482);
		contentPane.add(weatherAppPanel);
		
		JLabel inputLbl = new JLabel("Enter City:");
		inputLbl.setFont(new Font("Chilanka", Font.BOLD, 13));
		inputLbl.setForeground(Color.WHITE);
		
		inputField = new JTextField();
		inputField.setFont(new Font("FreeSans", Font.BOLD, 13));
		inputField.setForeground(new Color(255, 255, 255));
		inputField.setBorder(null);
		inputField.setBackground(new Color(102, 102, 102));
		inputField.setColumns(10);
		
		JLabel weatherAppLbl = new JLabel("Weather Application");
		weatherAppLbl.setFont(new Font("Chilanka", Font.BOLD, 16));
		weatherAppLbl.setForeground(Color.WHITE);
		weatherAppLbl.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblSearchHistory = new JLabel("Search History:");
		lblSearchHistory.setFont(new Font("Chilanka", Font.BOLD, 13));
		lblSearchHistory.setForeground(Color.WHITE);
		
		JButton searchBtn = new JButton("Search");
		searchBtn.setBorder(UIManager.getBorder("Button.border"));
		searchBtn.setForeground(new Color(0, 0, 0));
		searchBtn.setFont(new Font("FreeSans", Font.PLAIN, 13));
		searchBtn.setBackground(new Color(204, 204, 204));
			searchBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String city = inputField.getText().trim();
					
			        if (city.isEmpty()) {
			            JOptionPane.showMessageDialog(null, "Please enter a city name.", "Input Error", JOptionPane.ERROR_MESSAGE);
			            return;
			        }

			        String jsonResponse = getJSON(CURRENT_WEATHER_URL, city);
			        String jsonResponse2 = getJSON(FORECAST_URL, city);
			        if (jsonResponse.startsWith("Error") || jsonResponse2.startsWith("Error")) {
			            JOptionPane.showMessageDialog(null, jsonResponse, "Error", JOptionPane.ERROR_MESSAGE);
			            return;
			        }
			        
			        addSearchToHistory(city);
					displayWeather(jsonResponse);
					displayForecast(jsonResponse2);
				}
			});
		
		metricBtn = new JRadioButton("Metric");
		metricBtn.setFont(new Font("FreeSans", Font.PLAIN, 14));
		metricBtn.setForeground(Color.WHITE);
		metricBtn.setBackground(new Color(51, 51, 51));
		metricBtn.setSelected(true);
		
		imperialBtn = new JRadioButton("Imperial");
		imperialBtn.setFont(new Font("FreeSans", Font.PLAIN, 14));
		imperialBtn.setForeground(Color.WHITE);
		imperialBtn.setBackground(new Color(51, 51, 51));
		
		ButtonGroup group = new ButtonGroup();
		group.add(metricBtn);
		group.add(imperialBtn);
		
		historyJList = new JList<>(historyListModel);
		historyJList.setForeground(new Color(255, 255, 255));
		historyJList.setFont(new Font("FreeSans", Font.BOLD, 13));
		historyJList.setBorder(null);
		historyJList.setBackground(new Color(102, 102, 102));
        JScrollPane historyScrollPane = new JScrollPane(historyJList);
        historyScrollPane.setBorder(null);
		
		GroupLayout gl_weatherAppPanel = new GroupLayout(weatherAppPanel);
		gl_weatherAppPanel.setHorizontalGroup(
			gl_weatherAppPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_weatherAppPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblSearchHistory)
					.addContainerGap(109, Short.MAX_VALUE))
				.addGroup(gl_weatherAppPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(inputLbl, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
					.addGap(82))
				.addGroup(gl_weatherAppPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(metricBtn)
					.addContainerGap(145, Short.MAX_VALUE))
				.addGroup(gl_weatherAppPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(imperialBtn)
					.addContainerGap(134, Short.MAX_VALUE))
				.addGroup(gl_weatherAppPanel.createSequentialGroup()
					.addGap(60)
					.addComponent(searchBtn)
					.addContainerGap(81, Short.MAX_VALUE))
				.addGroup(gl_weatherAppPanel.createSequentialGroup()
					.addGap(13)
					.addComponent(historyScrollPane, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(Alignment.TRAILING, gl_weatherAppPanel.createSequentialGroup()
					.addGap(24)
					.addComponent(weatherAppLbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(35))
				.addGroup(Alignment.TRAILING, gl_weatherAppPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(inputField, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
					.addGap(29))
		);
		gl_weatherAppPanel.setVerticalGroup(
			gl_weatherAppPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_weatherAppPanel.createSequentialGroup()
					.addGap(33)
					.addComponent(weatherAppLbl)
					.addGap(18)
					.addComponent(inputLbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(inputField, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
					.addGap(8)
					.addComponent(metricBtn)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(imperialBtn)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(searchBtn)
					.addGap(30)
					.addComponent(lblSearchHistory, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(historyScrollPane, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE)
					.addGap(6))
		);
		
		
		// Left Panel and It's Component
		weatherAppPanel.setLayout(gl_weatherAppPanel);
		
		cityPanel = new JPanel();
		cityPanel.setBackground(new Color(204, 204, 204));
		cityPanel.setBounds(228, 12, 494, 482);
		contentPane.add(cityPanel);
		
		cityLbl = new JLabel("City");
		cityLbl.setFont(new Font("Chilanka", Font.BOLD, 15));
		cityLbl.setHorizontalAlignment(SwingConstants.CENTER);
		iconLbl = new JLabel("");
		iconLbl.setIcon(new ImageIcon("img/logo.png"));
		iconLbl.setFont(new Font("Chilanka", Font.BOLD, 12));
		iconLbl.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel tempLbl = new JLabel("Temp:");
		tempLbl.setFont(new Font("Chilanka", Font.BOLD, 14));
		JLabel timeLbl = new JLabel("City Time:");
		timeLbl.setFont(new Font("Chilanka", Font.BOLD, 14));
		JLabel conditionLbl = new JLabel("Condition:");
		conditionLbl.setFont(new Font("Chilanka", Font.BOLD, 14));
		JLabel windSpeedLbl = new JLabel("Wind Speed:");
		windSpeedLbl.setFont(new Font("Chilanka", Font.BOLD, 14));
		JLabel humidityLbl = new JLabel("Humidity:");
		humidityLbl.setFont(new Font("Chilanka", Font.BOLD, 14));
		
		tempResultLbl = new JLabel(".........");
		tempResultLbl.setFont(new Font("FreeSans", Font.BOLD, 14));
		timeResutLbl = new JLabel(".........");
		timeResutLbl.setFont(new Font("FreeSans", Font.BOLD, 14));
		conditionResultLbl = new JLabel(".........");
		conditionResultLbl.setFont(new Font("FreeSans", Font.BOLD, 14));
		windResultLbl = new JLabel(".........");	
		windResultLbl.setFont(new Font("FreeSans", Font.BOLD, 14));
		humidityResultLbl = new JLabel(".........");
		humidityResultLbl.setFont(new Font("FreeSans", Font.BOLD, 14));
		
		Labels.add(cityLbl);
		Labels.add(tempLbl);
		Labels.add(tempResultLbl);
		Labels.add(timeLbl);
		Labels.add(timeResutLbl);
		Labels.add(conditionLbl);
		Labels.add(conditionResultLbl);
		Labels.add(windSpeedLbl);
		Labels.add(windResultLbl);
		Labels.add(humidityLbl);
		Labels.add(humidityResultLbl);
		
		forecastPanel = new JPanel();
		forecastPanel.setBorder(null);
		forecastPanel.setBackground(new Color(204, 204, 204));
		
		GroupLayout gl_cityPanel = new GroupLayout(cityPanel);
		gl_cityPanel.setHorizontalGroup(
			gl_cityPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_cityPanel.createSequentialGroup()
					.addGap(39)
					.addComponent(forecastPanel, GroupLayout.PREFERRED_SIZE, 416, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(39, Short.MAX_VALUE))
				.addGroup(gl_cityPanel.createSequentialGroup()
					.addGap(80)
					.addGroup(gl_cityPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_cityPanel.createSequentialGroup()
							.addComponent(windSpeedLbl)
							.addGap(7)
							.addComponent(windResultLbl, GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE))
						.addGroup(gl_cityPanel.createSequentialGroup()
							.addComponent(timeLbl)
							.addPreferredGap(ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
							.addComponent(timeResutLbl, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)))
					.addGap(18)
					.addGroup(gl_cityPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_cityPanel.createSequentialGroup()
							.addComponent(conditionLbl, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(conditionResultLbl, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_cityPanel.createSequentialGroup()
							.addComponent(humidityLbl)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(humidityResultLbl, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)))
					.addGap(68))
				.addGroup(gl_cityPanel.createSequentialGroup()
					.addGap(160)
					.addComponent(iconLbl, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(159, Short.MAX_VALUE))
				.addGroup(gl_cityPanel.createSequentialGroup()
					.addGap(191)
					.addComponent(tempLbl)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tempResultLbl, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(198, Short.MAX_VALUE))
				.addGroup(gl_cityPanel.createSequentialGroup()
					.addGap(194)
					.addComponent(cityLbl, GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
					.addGap(194))
		);
		gl_cityPanel.setVerticalGroup(
			gl_cityPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_cityPanel.createSequentialGroup()
					.addGap(34)
					.addComponent(cityLbl)
					.addGap(12)
					.addComponent(iconLbl, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_cityPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(tempLbl)
						.addComponent(tempResultLbl))
					.addGap(29)
					.addGroup(gl_cityPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_cityPanel.createSequentialGroup()
							.addGroup(gl_cityPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(timeLbl)
								.addComponent(timeResutLbl))
							.addGap(27)
							.addGroup(gl_cityPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(windSpeedLbl)
								.addComponent(windResultLbl)))
						.addGroup(gl_cityPanel.createSequentialGroup()
							.addGroup(gl_cityPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(conditionLbl)
								.addComponent(conditionResultLbl))
							.addGap(27)
							.addGroup(gl_cityPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(humidityLbl)
								.addComponent(humidityResultLbl))))
					.addGap(26)
					.addComponent(forecastPanel, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
					.addGap(36))
		);
		
		JLabel forecastLbl = new JLabel("Forecast Display:");
		forecastLbl.setFont(new Font("Chilanka", Font.BOLD, 14));
		JLabel forecastTimeLbl = new JLabel("Time:");
		forecastTimeLbl.setFont(new Font("Chilanka", Font.BOLD, 14));
		JLabel forecastTempLbl = new JLabel("Temp:");
		forecastTempLbl.setFont(new Font("Chilanka", Font.BOLD, 14));
		JLabel time1Lbl = new JLabel("00:00");
		time1Lbl.setFont(new Font("FreeSans", Font.BOLD, 13));
		JLabel time2Lbl = new JLabel("06:00");
		time2Lbl.setFont(new Font("FreeSans", Font.BOLD, 13));
		JLabel time3Lbl = new JLabel("12:00");
		time3Lbl.setFont(new Font("FreeSans", Font.BOLD, 13));
		JLabel time4Lbl = new JLabel("18:00");
		time4Lbl.setFont(new Font("FreeSans", Font.BOLD, 13));
		temp1Lbl = new JLabel("temp");
		temp1Lbl.setFont(new Font("FreeSans", Font.BOLD, 13));
		temp2Lbl = new JLabel("temp");
		temp2Lbl.setFont(new Font("FreeSans", Font.BOLD, 13));
		temp3Lbl = new JLabel("temp");
		temp3Lbl.setFont(new Font("FreeSans", Font.BOLD, 13));
		temp4Lbl = new JLabel("temp");
		temp4Lbl.setFont(new Font("FreeSans", Font.BOLD, 13));
		
		Labels.add(forecastLbl);
		Labels.add(forecastTimeLbl);
		Labels.add(forecastTempLbl);
		Labels.add(time1Lbl);
		Labels.add(time2Lbl);
		Labels.add(time3Lbl);
		Labels.add(time4Lbl);
		Labels.add(temp1Lbl);
		Labels.add(temp2Lbl);
		Labels.add(temp3Lbl);
		Labels.add(temp4Lbl);
		
		GroupLayout gl_forecastPanel = new GroupLayout(forecastPanel);
		gl_forecastPanel.setHorizontalGroup(
			gl_forecastPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_forecastPanel.createSequentialGroup()
					.addGap(36)
					.addGroup(gl_forecastPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_forecastPanel.createSequentialGroup()
							.addComponent(forecastLbl)
							.addContainerGap())
						.addGroup(gl_forecastPanel.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_forecastPanel.createSequentialGroup()
								.addComponent(forecastTempLbl)
								.addGap(28)
								.addComponent(temp1Lbl)
								.addGap(37)
								.addComponent(temp2Lbl)
								.addGap(28)
								.addComponent(temp3Lbl, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
								.addGap(36)
								.addComponent(temp4Lbl)
								.addContainerGap())
							.addGroup(gl_forecastPanel.createSequentialGroup()
								.addComponent(forecastTimeLbl)
								.addGap(34)
								.addComponent(time1Lbl, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
								.addGap(45)
								.addComponent(time2Lbl, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
								.addGap(31)
								.addComponent(time3Lbl, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
								.addComponent(time4Lbl)
								.addGap(52)))))
		);
		gl_forecastPanel.setVerticalGroup(
			gl_forecastPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_forecastPanel.createSequentialGroup()
					.addGroup(gl_forecastPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_forecastPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(forecastLbl)
							.addGap(10)
							.addGroup(gl_forecastPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(time1Lbl)
								.addComponent(forecastTimeLbl)
								.addComponent(time2Lbl)))
						.addGroup(gl_forecastPanel.createSequentialGroup()
							.addGap(41)
							.addComponent(time3Lbl))
						.addGroup(gl_forecastPanel.createSequentialGroup()
							.addGap(41)
							.addComponent(time4Lbl)))
					.addGap(6)
					.addGroup(gl_forecastPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_forecastPanel.createSequentialGroup()
							.addGroup(gl_forecastPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(temp1Lbl)
								.addComponent(temp3Lbl)
								.addComponent(temp4Lbl)
								.addComponent(temp2Lbl))
							.addPreferredGap(ComponentPlacement.RELATED))
						.addComponent(forecastTempLbl))
					.addContainerGap(23, Short.MAX_VALUE))
		);
		gl_forecastPanel.linkSize(SwingConstants.VERTICAL, new Component[] {forecastTempLbl, temp1Lbl, temp2Lbl, temp3Lbl, temp4Lbl});
		gl_forecastPanel.linkSize(SwingConstants.HORIZONTAL, new Component[] {forecastTempLbl, temp1Lbl, temp2Lbl, temp3Lbl, temp4Lbl});
		forecastPanel.setLayout(gl_forecastPanel);
		cityPanel.setLayout(gl_cityPanel);
	}//Constructor END
	
	
	// Method to fetches weather data from an API using a city name
	public String getJSON(String stringURL, String city) {
		HttpURLConnection con = null;
		try {
			// Encode city and create the full URL with the API key
			String encodedCity = URLEncoder.encode(city, "UTF-8");
			String urlString = String.format(stringURL, encodedCity, API_KEY);
			
			// Convert the URL string into a URI and URL objects
			URI uri = new URI(urlString);
			URL url = uri.toURL();
			
			// Open the connection to the API
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			// Get the response code to check for errors
			int responseCode = con.getResponseCode();
			if (responseCode == 404) {
				return "Error: City not found.";
			} else if (responseCode != 200) {
				return "Error: Unable to fetch weather data.";
			}
			
			// Read the API's response line by line
			try (BufferedReader httpReader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
				StringBuilder httpResponse = new StringBuilder();
				String line;
				while ((line = httpReader.readLine()) != null) {
					httpResponse.append(line); //Combine all lines into a single JSON string
				}
				// Return the full response as a string
				return httpResponse.toString();
			}
		
		} catch (Exception e) {
			return "Error: Unable to fetch weather data. ";
		} finally {
			if (con != null) {
				con.disconnect(); 
			}
		}
	}// getJSON END


	// Method to processes the weather data and updates the UI
	public void displayWeather(String jsonResponse) {
		// Parse the JSON response
		JSONObject json = new JSONObject(jsonResponse);
		
		// Extract relevant information from JSON
		String city = json.getString("name");
		String condition = json.getJSONArray("weather").getJSONObject(0).getString("main");
		double temp = json.getJSONObject("main").getDouble("temp");
		int humidity = json.getJSONObject("main").getInt("humidity");
		double windSpeed = json.getJSONObject("wind").getDouble("speed");
		int timezoneOffset = json.getInt("timezone");
		
		// Calculate the city's local time
		Instant now = Instant.now();
		LocalDateTime cityTime = LocalDateTime.ofInstant(now, ZoneOffset.ofTotalSeconds(timezoneOffset));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		String timestamp = cityTime.format(formatter);
		
		// Determine if it's daytime based on the city's hour
		int hour = cityTime.getHour();
		boolean isDaytime = (hour >= 5 && hour < 17);
		
		// Handle unit conversions (Metric(Celsius, m/s)/Imperial(Fahrenheit, mph))
		String tempSymbol, windSymbol;
		if (imperialBtn.isSelected()) {
			temp = (temp * 9.0 / 5.0) + 32.0;
			windSpeed = windSpeed * 2.237;
			tempSymbol = "\u00B0F";
			windSymbol = "mph";
		} else {
			tempSymbol = "\u00B0C";
			windSymbol = "m/s";
		}
		
		// Update visual elements
		changeBackground(hour, isDaytime); // Change background color based on time of day
		getWeatherIcon(condition, isDaytime); // Get appropriate weather icon
		
		// Update the UI with the city's weather information
		cityLbl.setText(city);
		tempResultLbl.setText(String.format("%.2f%s", temp, tempSymbol)); 
		timeResutLbl.setText(timestamp); 
		conditionResultLbl.setText(condition); 
		humidityResultLbl.setText(humidity + "%");
		windResultLbl.setText(String.format("%.2f%s", windSpeed, windSymbol)); 
	}// displayWeather END
	
	// Method to process the weather forecast and update the  UI
	public void displayForecast(String jsonResponse) {
		// Parse the JSON response and get the forecast data
		JSONObject json = new JSONObject(jsonResponse);
		JSONArray forecastList = json.getJSONArray("list");
		
		// Predefined forecast times
		String[] targetTimes = {"00:00:00", "06:00:00", "12:00:00", "18:00:00"};
		
		// Labels for temperature display
		JLabel[] tempLabels = {temp1Lbl, temp2Lbl, temp3Lbl, temp4Lbl};
		
		// Convert times to a set for quick lookup
		Set<String> targetTimeSet = new HashSet<>(Arrays.asList(targetTimes));
		
		// Loop through the forecast and Extract the time
		for (int i = 0; i < forecastList.length(); i++) {
			JSONObject forecast = forecastList.getJSONObject(i);
			String dt_txt = forecast.getString("dt_txt");
			String time = dt_txt.split(" ")[1]; 
			
			// Check if the forecast's time matches any target times
			// If matched:  1- Get index of the label 2-Get the temperature 
			// 3- Convert temperature units if needed 4-Update the temperature label
			if (targetTimeSet.contains(time)) {
				int index = Arrays.asList(targetTimes).indexOf(time); 
				   
				double temp = forecast.getJSONObject("main").getDouble("temp");
				String tempSymbol;
				
				if (imperialBtn.isSelected()) {
					temp = (temp * 9.0 / 5.0) + 32.0; 
					tempSymbol = "\u00B0F"; 
				} else {
					tempSymbol = "\u00B0C"; 
				}
				
				tempLabels[index].setText(String.format("%.1f%s", temp, tempSymbol));
			}
		}
	}//displayForecast END
	
	// Method to adds a search query with timeStamp to the history list.
	public void addSearchToHistory(String search) {
		String timestamp = new SimpleDateFormat("yy/MM/dd HH:mm:ss").format(new Date());
		String entry = timestamp + " - " + search;
		historyDataList.add(entry);
		historyListModel.addElement(entry); 
	}//addSearchToHistory END
    
	// Method to change background color based on the hour of of day
	public void changeBackground(int hour, boolean isDaytime) {  
		Color bgColor;        // Main background color 
		Color forecastBgColor; // Forecast panel background color
		
		// Determine color
		if (hour >= 5 && hour < 12) { 
			// Morning 
			bgColor = new Color(221, 218, 255);  
			forecastBgColor = new Color(201, 213, 240); 
		} else if (hour >= 12 && hour < 17) {  
			// Midday
			bgColor = new Color(76, 139, 181); 
			forecastBgColor = new Color(109, 163, 199);
		} else if (hour >= 17 && hour < 21) {
			// Evening
			bgColor = new Color(75, 68, 189);  
			forecastBgColor = new Color(89, 80, 212);
		} else {  
			// Night
			bgColor = new Color(29, 25, 54);   
			forecastBgColor = new Color(40, 35, 69); 
		}
		
		// Set the background color for the panels
		cityPanel.setBackground(bgColor);
		forecastPanel.setBackground(forecastBgColor);
		
		// Set text color for labels based on whether it's daytime or nighttime
		Color textColor = isDaytime ? Color.darkGray : Color.WHITE;
		for (JLabel label : Labels) {
			label.setForeground(textColor); 
		}
	}// changeBackground END

    
	// Method to get appropriate weather icon based on the condition 
	public void getWeatherIcon(String condition, boolean isDaytime) {
		iconLbl.setText("");  // Clear existing icon
		
		String iconPath = "";
		
		// Set icon based on weather condition and time of day
		switch (condition.toLowerCase()) {
			case "clear":
				iconPath = isDaytime ? "img/sun.png" : "img/moon.png";
				break;
			case "clouds":
				iconPath = isDaytime ? "img/cloudyDay.png" : "img/cloudyNight.png";
				break;
			case "rain":
				iconPath = "img/rainy.png";
				break;
			case "drizzle":
				iconPath = isDaytime ? "img/dizzlyDay.png" : "img/dizzlyNight.png";
				break;
			case "thunderstorm":
				iconPath = "img/storm.png";
				break;
			case "snow":
				iconPath = "img/snow.png";
				break;
			case "mist":
			case "fog":
			case "haze":
				iconPath = "img/fog.png";
				break;
			default:
				iconPath = "img/default.png";
				break;
		}
		
		try {
			// Load and set icon
			ImageIcon icon = new ImageIcon(iconPath);
			iconLbl.setIcon(icon);
		} catch (Exception e) {
			System.err.println("Error loading icon: " + e.getMessage());
		}
	}// getWeatherIcon END
     
}//Class END
