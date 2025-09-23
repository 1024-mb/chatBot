package com.chatbot.APIServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.chatbot.util.speak;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class weatherAPI {
    private static final String API_KEY_WEATHER = "7eS832Ldvfmt36Drluj20AJc3u1F5bDE";
    private static final Logger logger = LoggerFactory.getLogger(weatherAPI.class);

    public void getData() {
        try {
            String urlString = "https://api.tomorrow.io/v4/timelines?location=42.3478,-71.0466&fields=temperature&units=metric&apikey=" + API_KEY_WEATHER;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10_000); // 10s
            conn.setReadTimeout(15_000);

            try (InputStream inputStream = conn.getInputStream();BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));) {

                String inputLine;
                StringBuffer content = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine.trim());
                }

                JSONObject root = new JSONObject(content.toString());

                // Navigate to timelines
                JSONObject data = root.getJSONObject("data");
                JSONArray timelines = data.getJSONArray("timelines");

                double totalTemperature = 0;

                // Loop through each timeline (usually one)
                int i;
                int count = 0;
                double maxtemp = 0.0, mintemp = 1000;

                for (i = 0; i < timelines.length(); i++) {
                    JSONObject timeline = timelines.getJSONObject(i);
                    JSONArray intervals = timeline.getJSONArray("intervals");

                    // Loop through each interval to extract temperature
                    for (int j = 0; j < intervals.length(); j++) {
                        JSONObject interval = intervals.getJSONObject(j);
                        double temp = interval.getJSONObject("values").getDouble("temperature");
                        totalTemperature += temp;
                        maxtemp = Math.max(temp, maxtemp);
                        mintemp = Math.min(temp, mintemp);
                        count++;
                    }
                }

                double avg = totalTemperature / count;

                System.out.printf("The average temperature for today will be %.1f°C. The high will be: %.1f°C and the low will be %.1f°C",
                        avg, maxtemp, mintemp);
                System.out.println();
                speak.speak("average temperature for today will be " + Math.round(avg) + "°C. The high will be: " +
                        Math.round(maxtemp) + "°C and the low will be " + Math.round(mintemp) + "°C.");

            } finally {
                conn.disconnect();
            }
        }
        catch(MalformedURLException e) {
            System.out.println("NewsAPI Malformed URL");
            logger.error("Error Fetching the news URL ", e);
        }
        catch(IOException e) {
            System.out.println("I/O Operation Failed");
            logger.error("Error in IO Operations ", e);
        }

    }
}
