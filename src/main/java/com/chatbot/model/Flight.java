package com.chatbot.model;

public class Flight {
    private String departureDate;
    private String departureScheduled;

    private String airportDeparture;
    private String departureIATA;

    private String arrivalScheduled;

    private String airportArrival;
    private String arrivalIATA;

    private String terminal;
    private String gate;

    private String flightNumber;
    private String airline;

    public Flight(String departureDate, String departureScheduled,
           String departureIATA, String airportDeparture,
           String arrivalIATA, String arrivalScheduled,
           String airportArrival, String flightNumber, String airline) {

        this.flightNumber = flightNumber;
        this.airline = airline;
        this.arrivalScheduled = arrivalScheduled;

        this.departureDate  = departureDate;


        this.airportArrival = airportArrival;
        this.arrivalIATA = arrivalIATA;

        this.airportDeparture = airportDeparture;
        this.departureIATA = departureIATA;
        this.departureScheduled = departureScheduled;
    }

    public String[] getDepartureData() {
        String result[] = {this.airportDeparture, this.departureIATA};
        return result;
    }

    public String[] getArrivalData() {
        String result[] = {this.airportArrival, this.arrivalIATA};
        return result;
    }

    public String getDate() {return this.departureDate;}

    public String getDepartureTime() {return this.departureScheduled;}

    public String getArrivalTime() {return this.arrivalScheduled;}

    public String getFlightNumber() {return this.flightNumber;}

    public String getAirline() {return this.airline;}

}
