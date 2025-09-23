package com.chatbot.app;

import com.chatbot.APIServices.*;
import com.chatbot.audioServices.numConverter;
import com.chatbot.audioServices.mediaPlayer;
import com.chatbot.util.*;
import com.chatbot.util.getKeywords;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.vosk.Model;
import org.vosk.Recognizer;
// if Android
import javax.sound.sampled.*;
import javax.speech.Central;

import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceDetectorME;

import edu.cmu.sphinx.api.LiveSpeechRecognizer;

import javax.speech.AudioException;
import javax.speech.EngineException;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import java.io.IOException;
import java.util.Scanner;

public class App {
    private LiveSpeechRecognizer recognizer;
    private static final timer timer = new timer();
    private static final getKeywords getKeywords = new getKeywords();
    private static final numConverter CONVERTER = new numConverter();
    private static final mediaPlayer MEDIA_PLAYER = new mediaPlayer();
    private static final weatherAPI WEATHER_API = new weatherAPI();
    private static final newsAPI NEWS_API = new newsAPI();
    private static final flightAPI FLIGHT_API = new flightAPI();
    private static final speak SPEAK = new speak();
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final getDateTime dateTime = new getDateTime();
    private static final wikipediaAPI wiki = new wikipediaAPI();
    private static final mathOpn OPN = new mathOpn();
    private static final wolframAPI WOLFRAM_API = new wolframAPI();
    private static final translateAPI translateAPI = new translateAPI();

    private static final Scanner scanner = new Scanner(System.in);


    public void startSpeechRecognition() {
        try {
            Model model = new Model("assets/models/vosk-model-small-en-us-0.15");
            Recognizer recognizer = new Recognizer(model, 16000);

            AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Microphone not supported");
                return;
            }

            try {
                TargetDataLine mic = (TargetDataLine) AudioSystem.getLine(info);
                mic.open(format);
                mic.start();

                System.out.println();

                System.out.println("Listening... Say Something!");

                byte[] buffer = new byte[4096];

                while (true) {
                    int bytesRead = mic.read(buffer, 0, buffer.length);

                    if (bytesRead <= 0) continue;

                    // Vosk expects raw PCM byte array (little-endian signed 16-bit)
                    if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                        String resultJson = recognizer.getResult(); // JSON string with "text"

                        JSONObject commandJSON = new JSONObject(resultJson);
                        String command = commandJSON.getString("text");

                        if (command != null && !command.isEmpty()) {

                            String[] words = command.split(" ");

                            for (int count = 0; count < words.length; count++) {
                                words[count] = words[count].substring(0, 1).toUpperCase() + words[count].substring(1);
                            }

                            System.out.print("Recognized: " + String.join(" ", words));
                            System.out.println();
                        }

                        if (Arrays.asList(command.split("\\s+")).contains("timer")) {timer.startTimer(command);}

                        else if (Arrays.asList(command.split("\\s+")).contains("time")) {dateTime.getTime(command);}

                        else if (command.contains("date")) {dateTime.getDate();}

                        else if (Arrays.asList(command.split("\\s+")).contains("search")
                                || Arrays.asList(command.split("\\s+")).contains("look up")
                                || Arrays.asList(command.split("\\s+")).contains("google")) {
                            try {
                                String[] searchTerms = {"search", "look up", "google"};

                                for (String search : searchTerms) {
                                    command = (command.contains(search)) ? command.replace(search, "") : command;
                                }

                                if (command.contains(" ")) {
                                    command = command.replace(" ", "+");
                                    command = command.replace("  ", "+");
                                }

                                command = command.trim();

                                Runtime.getRuntime().exec("cmd /c start msedge.exe https://www.google.com/search?q=" + command);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }


                        else if (command.contains("news") || command.contains("headlines")) {NEWS_API.getNews();}

                        else if (command.contains("weather") || command.contains("whether")) {WEATHER_API.getData();}

                        else if (command.contains("play")) {
                            String name = "", nameOriginal = "";

                            speak.speak("Enter Song Name");
                            System.out.println("Enter Song Name / X to Exit: ");
                            nameOriginal = scanner.nextLine();

                            while (!nameOriginal.toUpperCase().equals("X") && !nameOriginal.equals("")) {
                                MEDIA_PLAYER.songPlayer("nameOriginal");

                                speak.speak("Enter Song Name");
                                System.out.println("Enter Song Name / X to Exit: ");
                                nameOriginal = scanner.nextLine();
                            }
                            System.out.println("Player closed.");
                        }

                        else if (command.contains("spell")) {
                            String[] replacement = {"how", "to", "word", "spell", "the"};

                            for (int count = 0; count < replacement.length; count++) {
                                command = command.replaceAll(replacement[count], "");
                            }
                            command = command.trim();

                            speak.speak(command + " is spelt");

                            for (int count = 0; count < command.length(); count++) {
                                String target = command.substring(count, count + 1);
                                speak.speak(target);
                                System.out.print(target);
                            }

                            System.out.println();

                        }


                        else if (command.contains("tell") ||
                                command.contains("information") || command.contains("who is") ||
                                command.contains("research") || command.contains("about")) {wiki.getPage(command);}

                        else if (command.contains("divide") ||
                                Arrays.asList(command.split("\\s+")).contains("over") ||
                                command.contains("multipl") ||
                                Arrays.asList(command.split("\\s+")).contains("times") ||
                                command.contains("add") ||
                                command.contains("sum") ||
                                command.contains("plus") ||
                                command.contains("subtract") ||
                                command.contains("percent") ||
                                Arrays.asList(command.split("\\s+")).contains("percentage") ||
                                Arrays.asList(command.split("\\s+")).contains("calculate") ||
                                Arrays.asList(command.split("\\s+")).contains("power")
                        ) {OPN.computeMath(command);}

                        else if (command.contains("motivation")) {MEDIA_PLAYER.soundPlayer("Motivation");}


                        else if (command.contains("flights") || command.contains("flight") ||
                                command.contains("plane") || command.contains("departure")) {

                            System.out.println();
                            System.out.println("Enter Departure Airport:  ");

                            String departureAirport = scanner.nextLine();

                            System.out.println();

                            System.out.print("Enter Arrival Airport (enter ANY for ALL): ");
                            String arrivalAirport = scanner.nextLine();
                            FLIGHT_API.getData(departureAirport, arrivalAirport);


                        }

                        else if (command.contains("translate") || (command.contains("what")&&command.contains("is")&&command.contains("in"))) {
                            if(!command.contains("to") && command.contains("english")) {
                                translateAPI.get_translation(command, "en");
                            }
                            else {
                                String langIn;
                                StringBuilder text = new StringBuilder();

                                String[] words = command.split(" ");

                                langIn = words[words.length-1];

                                for(int count=0; count<words.length-3; count++) {
                                    text.append(words[count]).append(" ");
                                }

                                String code;

                                switch(langIn.toLowerCase()) {
                                    case "english"       -> code = "en";
                                    case "french"        -> code = "fr";
                                    case "german"        -> code = "de";
                                    case "italian"       -> code = "it";
                                    case "chinese"       -> code = "zh";
                                    case "japanese"      -> code = "ja";
                                    default              -> code = "en"; // fallback
                                }

                                String stringText = com.chatbot.util.getKeywords.cleanCMD(text.toString());
                                translateAPI.get_translation(stringText.toLowerCase(), code);
                            }
                        }


                        else if (command.split(" ").length >= 3){
                            WOLFRAM_API.getResponse(getKeywords.cleanTitle(command));
                        }

                        if (!command.isEmpty()) {
                            System.out.println();
                            System.out.println("Listening... Say Something!");
                        }
                    }
                }

            } catch (LineUnavailableException e) {
                System.out.println("Mic is currently being used by another application");
                logger.error("MicUnavailable  ", e);
            }

        } catch (IOException e) {
            logger.error("Couldn't Access Microphone ", e);
        }
    }

    //Operations
    //voice function

    public static void main(String[] args) throws IOException {
        App va = new App();
        va.startSpeechRecognition();
    }
}
