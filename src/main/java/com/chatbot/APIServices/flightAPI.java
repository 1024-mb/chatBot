package com.chatbot.APIServices;

import com.chatbot.model.Flight;
import org.json.JSONArray;
import org.json.JSONObject;
import com.chatbot.util.speak;
import com.chatbot.util.getKeywords;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class flightAPI {
    private static final String API_KEY_AIRPORT = System.getenv("AIRPORT");
    private static final getKeywords GET_KEYWORDS = new getKeywords();
    private static final Logger logger = LoggerFactory.getLogger(flightAPI.class);

    Scanner scanner = new Scanner(System.in);

    public void getData(String departureAirport, String arrivalAirport) {

        if (departureAirport.contains("portland") || arrivalAirport.contains("portland")) {
            System.out.println("""
                                        Please choose between:
                                        
                                            (1) Portland Jetport
                                            (2) Portland Intl.
                                            
                                        Type 1/2
                                        """);
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    departureAirport = (departureAirport.contains("portland")) ? "portland jetport" : departureAirport;
                    arrivalAirport = (arrivalAirport.contains("portland")) ? "portland jetport" : arrivalAirport;
                    break;


                case "2":
                    departureAirport = (departureAirport.contains("portland")) ? "portland intl" : departureAirport;
                    arrivalAirport = (arrivalAirport.contains("portland")) ? "portland intl" : arrivalAirport;
                    break;
            }


        }
        else if(departureAirport.contains("santa maria") || arrivalAirport.contains("santa maria")) {
            System.out.println("""
                                                    Please choose between:
                                                    
                                                        (1) Santa Maria Public, USA
                                                        (2) Santa Maria, Brazil
                                                        
                                                    Type 1/2
                                                    """);
            String choice = scanner.nextLine();

            switch(choice) {
                case "1":
                    departureAirport = (departureAirport.contains("santa maria")) ? "santa maria public" : departureAirport;
                    arrivalAirport = (arrivalAirport.contains("santa maria")) ? "santa maria public" : arrivalAirport;
                    break;


                case "2":
                    departureAirport = (departureAirport.contains("santa maria")) ? "santa maria" : departureAirport;
                    arrivalAirport = (arrivalAirport.contains("santa maria")) ? "santa maria" : arrivalAirport;
                    break;
            }

        }

        final String PATH = "C:\\Users\\FUJITSU\\JavaPrograms\\chatBotNlp\\src\\main\\resources\\airportData.csv";

        String airportCodeDeparture = "";
        String airportCodeArrival = "";

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(PATH))) {
            String line = bufferedReader.readLine().trim();

            arrivalAirport = GET_KEYWORDS.cleanFlight(arrivalAirport);
            departureAirport = GET_KEYWORDS.cleanFlight(departureAirport);


            while((line != null) && !(line.isEmpty()) && !(line.equals("System.IO.MemoryStream"))) {
                String[] airportData = line.split(",");

                String airportName = GET_KEYWORDS.cleanFlight(airportData[1]);

                airportCodeDeparture = (airportName.contains(departureAirport)) ? airportData[0] : airportCodeDeparture;
                airportCodeArrival = (airportName.contains(arrivalAirport)) ? airportData[0] : airportCodeArrival;

                line = bufferedReader.readLine();
            }



        }
        catch(IOException e) {
            logger.error("Unable to read data from CSV ", e);
        }


        try {
            LocalDate dateToday = LocalDate.now();
            ArrayList<Flight> flights = new ArrayList<>();
            StringBuffer content = new StringBuffer();
            String urlString;

            if(!(arrivalAirport.equals("any") || arrivalAirport.isEmpty())) {
                speak.speak("Sure, Here are all Flights From " +
                        GET_KEYWORDS.cleanText(airportCodeDeparture) + " To " + GET_KEYWORDS.cleanText(airportCodeArrival));

                urlString = "https://api.aviationstack.com/v1/flights?access_key="
                        + API_KEY_AIRPORT + "&limit=100" +
                        "&flight_status=scheduled" +
                        "&dep_iata=" + airportCodeDeparture
                        + "&arr_iata=" + airportCodeArrival;


            } else {
                speak.speak("Sure, Here are all Flights From " + GET_KEYWORDS.cleanText(airportCodeDeparture));

                urlString = "https://api.aviationstack.com/v1/flights?access_key="
                        + API_KEY_AIRPORT + "&limit=100" +
                        "&flight_status=scheduled" +
                        "&dep_iata=" + airportCodeDeparture;
            }


            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(urlString))
                    .GET()
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());            JSONObject root = new JSONObject(response.toString());

            // Navigate to timelines

            try {
                JSONArray data = root.getJSONArray("data");

                for(int count=0; count < data.length(); count++) {
                    JSONObject examine = data.getJSONObject(count);

                    String flight_date = examine.getString("flight_date");
                    JSONObject departure = examine.getJSONObject("departure");

                    String scheduledDeparture = departure.getString("scheduled");
                    String departureIATA = departure.getString("iata");

                    JSONObject arrival = examine.getJSONObject("arrival");

                    String arrivalIATA = arrival.getString("iata");
                    String scheduledArrival = arrival.getString("scheduled");

                    JSONObject airline = examine.getJSONObject("airline");

                    String airlineName;
                    String airportDeparture;
                    String airportArrival;
                    String flightNo;

                    try{airlineName = airline.getString("name");}
                    catch(Exception e) {airlineName = "Unknown Provider";}

                    try{airportDeparture = departure.getString("airport");}
                    catch(Exception e) {airportDeparture = "Error Loading Airport Departure";}

                    try{airportArrival = arrival.getString("airport");}
                    catch(Exception e) {airportArrival = "Error Loading Airport Arrival";}

                    try {
                        JSONObject flight = examine.getJSONObject("flight");
                        flight = flight.getJSONObject("codeshared");
                        flightNo = flight.getString("airline_iata").toUpperCase() +
                                flight.getString("flight_number");

                    } catch(Exception e) {
                        flightNo = "Flight Number Unknown";
                    }

                    /*
                     * String departureDate, String departureScheduled,
                     * String departureIATA, String airportDeparture,
                     * String arrivalIATA, String arrivalScheduled,
                     * String airportArrival, String flightNumber, String airline
                     *
                     *  */
                    flights.add(new Flight(flight_date, scheduledDeparture,
                            departureIATA, airportDeparture, arrivalIATA, scheduledArrival,
                            airportArrival, flightNo, airlineName));


                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }

            if(!(flights.isEmpty())) {
                System.out.println("=".repeat(85));
                System.out.println(flights.size() + " Flights Found.");
                System.out.println("All Times Are   ||   (UTC+8)");
                System.out.println();

                for(int count=0; count < flights.size(); count++) {
                    System.out.println("~".repeat(85));

                    System.out.printf("%-30s %-35s %-20s%n", "Flight",
                            flights.get(count).getFlightNumber(), "(" +
                                    flights.get(count).getDepartureData()[1] + " → " +
                                    flights.get(count).getArrivalData()[1] + ")");

                    String stringTimeDeparture = flights.get(count).getDepartureTime().substring(flights.get(count).getDepartureTime().indexOf("T") + 1, flights.get(count).getDepartureTime().indexOf("+"));
                    String stringTimeArrival = flights.get(count).getArrivalTime().substring(flights.get(count).getDepartureTime().indexOf("T") + 1, flights.get(count).getDepartureTime().indexOf("+"));

                    LocalTime timeDeparture = LocalTime.parse(stringTimeDeparture).plusHours(8);
                    LocalTime timeArrival = LocalTime.parse(stringTimeArrival).plusHours(8);

                    stringTimeDeparture = timeDeparture.toString();
                    stringTimeArrival = timeArrival.toString();

                    ArrayList<Flight> flightsOut = new ArrayList<>();

                    if ((flights.get(count).getDepartureData()[0].length() > 35) && !(flightsOut.contains(flights.get(count)))) {
                        System.out.printf("%-30s %-35s %-20s%n", "Departure Information",
                                flights.get(count).getDepartureData()[0].substring(0, 30) + "... ", stringTimeDeparture);


                        if (flights.get(count).getArrivalData()[0].length() > 35) {
                            System.out.printf("%-30s %-35s %-20s%n", "Arrival Information",
                                    flights.get(count).getArrivalData()[0].substring(0, 30) + "... ", stringTimeArrival);

                        }
                        else {
                            System.out.printf("%-30s %-35s %-20s%n", "Arrival Information",
                                    flights.get(count).getArrivalData()[0], stringTimeArrival);

                        }
                        flightsOut.add(flights.get(count));
                    }

                    else if(!(flightsOut.contains(flights.get(count)))) {
                        System.out.printf("%-30s %-35s %-20s%n", "Departure Information",
                                flights.get(count).getDepartureData()[0], stringTimeDeparture);


                        if (flights.get(count).getArrivalData()[0].length() > 35) {
                            System.out.printf("%-30s %-35s %-20s%n", "Arrival Information",
                                    flights.get(count).getArrivalData()[0].substring(0, 30) + "... ", stringTimeArrival);

                        }
                        else {
                            System.out.printf("%-30s %-35s %-20s%n", "Arrival Information",
                                    flights.get(count).getArrivalData()[0], stringTimeArrival);
                        }
                        flightsOut.add(flights.get(count));
                    }
                }

                System.out.println("=".repeat(85));
            }
            else {
                System.out.println("Sorry - No Flights Matching This Specification Found");
            }


        }
        catch(MalformedURLException e) {
            System.out.println("NewsAPI Malformed URL");
            logger.error("Error Fetching the news URL ", e);
        }

        catch(IOException e) {
            System.out.println("I/O Operation Failed");
            logger.error("Error in IO Operations ", e);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
            logger.error("Error in getting site data  ", e);
        }


    }
}
