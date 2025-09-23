package com.chatbot.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class getDateTime {
    public void getTime(String command) {
        DateTimeFormatter formatter12hr = DateTimeFormatter.ofPattern("hh:mm a");
        command = command.toLowerCase();

        if ((command.contains("in") || command.contains("and"))) {
            Set<String> zones = ZoneId.getAvailableZoneIds();

            String zone = "";
            String cityFinal = "";
            String countryFinal = "";

            for (String zoneItem : zones) {
                String zoneString = zoneItem.toLowerCase().replaceAll("_", " ");

                String city = "";
                String country = "";

                if (zoneString.contains("/")) {
                    String[] parts = zoneItem.split("/");

                    city = parts[parts.length - 1].toLowerCase();
                    country = parts[parts.length - 2].toLowerCase();

                } else {
                    city = zoneString.toLowerCase();
                }

                String commandComp = command.toLowerCase().replaceAll(" ", "_");


                if (commandComp.contains(city)) {
                    zone = zoneItem;
                    cityFinal = city;
                    break;
                }

                if (!country.isEmpty() && commandComp.contains(country)) {
                    zone = zoneItem;
                    countryFinal = country;
                    break;
                }

            }

            if (!zone.isEmpty()) {
                LocalTime localTime = LocalTime.now(ZoneId.of(zone));

                String strTime = localTime.format(formatter12hr).toString();

                String hours = strTime.substring(0, 2);
                String minutes = strTime.substring(3, 5);
                String meridian = strTime.substring(6);

                String out = "";
                if (!cityFinal.isEmpty()) {
                    cityFinal = cityFinal.replaceAll("_", " ");

                    out = "";
                    for (String component : cityFinal.split(" ")) {
                        out += " " + component.substring(0, 1).toUpperCase() + component.substring(1);
                    }


                } else if (!countryFinal.isEmpty()) {
                    out = "";
                    for (String component : cityFinal.split(" ")) {
                        out += " " + component.substring(0, 1).toUpperCase() + component.substring(1);
                    }

                }

                speak.speak("It is " + hours + " " + minutes + " " + meridian + " right now in" + out + ".");
                System.out.println("It is " + strTime + " right now in" + out + ".");

            } else {
                speak.speak("This place is not in my index. Please try a larger city nearby");
                System.out.println("This place is not in my index. Please try a larger city nearby");
            }

        } else {
            LocalTime localTime = LocalTime.now();
            String strTime = localTime.format(formatter12hr).toString();

            String hours = strTime.substring(0, 2);
            String minutes = strTime.substring(3, 5);
            String meridian = strTime.substring(6);

            speak.speak("It is " + hours + " " + minutes + " " + meridian + " right now.");
            System.out.println("It is " + strTime + " right now.");

                                    /*
                                    if (Integer.parseInt(hours) < 10 && Integer.parseInt(hours) > 0) {
                                        speak("It is " + hours.replace("0", "") + " " + minutes + "AM right now.");
                                        System.out.println("It is " + hours.replace("0", "") + ":" + minutes + "AM right now.");
                                    } else {
                                        speak("It is " + hours + " " + minutes + " right now.");
                                        System.out.println("It is " + hours + ":" + minutes + " right now.");
                                    }

                                     */
        }
    }


    public void getDate() {
        LocalDate localDate = LocalDate.now();
        String strDate = localDate.toString();

        int monthNo = Integer.parseInt(strDate.substring(5, 7));
        String monthName = "";

        switch (monthNo) {
            case 1 -> monthName = "January";
            case 2 -> monthName = "February";
            case 3 -> monthName = "March";
            case 4 -> monthName = "April";
            case 5 -> monthName = "May";
            case 6 -> monthName = "June";
            case 7 -> monthName = "July";
            case 8 -> monthName = "August";
            case 9 -> monthName = "September";
            case 10 -> monthName = "October";
            case 11 -> monthName = "November";
            case 12 -> monthName = "December";
        }

        int day = Integer.parseInt(strDate.substring(8));
        int year = Integer.parseInt(strDate.substring(0, 4));
        System.out.println("it is the " + day + " " + monthName + " " + year);
        speak.speak("it is the " + day + " " + monthName + " " + year);

    }
}
