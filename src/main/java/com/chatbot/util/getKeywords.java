package com.chatbot.util;
import com.chatbot.util.speak;

public class getKeywords {
    public String cleanFlight(String Flight) {
        Flight = Flight.toLowerCase();
        Flight = Flight.trim();
        Flight = Flight.replaceAll("Airport", "");
        Flight = Flight.replaceAll("Airfield", "");
        Flight = Flight.replaceAll("International", "");
        Flight = Flight.replaceAll("Intl", "");
        Flight = Flight.replaceAll("Int", "");

        Flight = Flight.replaceAll("airport", "");
        Flight = Flight.replaceAll("airfield", "");
        Flight = Flight.replaceAll("international", "");
        Flight = Flight.replaceAll("intl", "");
        Flight = Flight.replaceAll("int", "");

        Flight = Flight.replaceAll(" ", "");

        return Flight;
    }

    public static String cleanText(String inputText) {
        inputText = inputText.replaceAll("_", " ");
        inputText = inputText.replaceAll("&", "and");
        inputText = inputText.replaceAll("--", "");
        inputText = inputText.replaceAll("#[^\\s]+", "");
        inputText = inputText.replaceAll("\\\\u[^\\s]+", "");
        inputText = inputText.replaceAll("\\\\n", "");
        inputText = inputText.replaceAll("[{]+", "");
        inputText = inputText.replaceAll("[}]+", "");

        inputText = inputText.replaceAll("(?<=\\p{L})\\.(?=\\p{L})", ". ");
        inputText = inputText.replaceAll(" {2}", " ");


        return inputText;
    }

    public static String cleanTitle(String Title) {
        Title = Title.replaceAll("\\babout\\b", "");
        Title = Title.replaceAll("\\btell\\b", "");
        Title = Title.replaceAll("\\bme\\b", "");
        Title = Title.replaceAll("\\ba\\b", "");
        Title = Title.replaceAll("\\binformation\\b", "");

        return Title;
    }

    public static String cleanCMD(String CMD) {
        CMD = CMD.replaceAll("translate", "");
        CMD = CMD.replace("", "");

        String[] cmds = CMD.split(" ");

        for(int count=cmds.length-1; count>=0; count--) {
            if(cmds[count].equals("to")) {
                cmds[count] = "";
                break;
            } else if(cmds[count].equals("in")) {
                cmds[count] = "";
                break;
            }

        }

        CMD = String.join(" ", cmds);

        if(CMD.contains("what") && CMD.contains("is")) {
            CMD = CMD.replaceFirst("what", "");
            CMD = CMD.replaceFirst("is", "");
        }

        cmds = CMD.split(" ");

        for(int count=1; count<cmds.length; count++) {
            cmds[count] = cmds[count].substring(0, 2) + cmds[count].substring(1);

        }
        CMD = String.join(" ", cmds);

        return CMD;
    }
}
