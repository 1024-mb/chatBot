package com.chatbot.APIServices;

import com.chatbot.util.speak;
import com.chatbot.util.getKeywords;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Collectors;



public class wikipediaAPI {
    private static final String ENCODING = "UTF-8";
    private static final String TOKEN = "4898cf977e54986f1d4a1423049350b193cd382ac45dc87c0d6389ebdd739cfbabb968c7ad03f21cdbc4872017bc43f9";
    private static final Logger logger = LoggerFactory.getLogger(wikipediaAPI.class);
    private static final getKeywords getKeywords = new getKeywords();

    public void getPage(String command) {
        //Get the first link about Wikipedia
        try {
            String wikipediaTitle = "";
            String title = "";

            command = " " + command + " ";

            String pageTitle = com.chatbot.util.getKeywords.cleanTitle(command).trim(); // Replace with your desired page title

            String urlStr = "https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch="
                    + URLEncoder.encode(pageTitle, "UTF-8") + "&format=json";

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(urlStr))
                    .GET()
                    .build();

            HttpResponse<String> httpResponse =  client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            JSONObject obj = new JSONObject(httpResponse);
            JSONArray searchResults = obj.getJSONObject("query").getJSONArray("search");

            if (!searchResults.isEmpty()) {
                JSONObject firstResult = searchResults.getJSONObject(0);
                title = firstResult.getString("title");

            } else {
                logger.info("No result for normal \"<query>\", trying \"tell + <query>\"");

                // If nothing found, try other command,
                pageTitle = com.chatbot.util.getKeywords.cleanTitle(command).trim();
                pageTitle = "tell" + pageTitle;

                urlStr = "https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch="
                        + URLEncoder.encode(pageTitle, "UTF-8") + "&format=json";


                /*
                conn = (HttpURLConnection) new URL(urlStr).openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");

                json = "";
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    json = in.lines().collect(Collectors.joining());
                }

                 */

                HttpRequest newRequest = HttpRequest.newBuilder()
                        .uri(URI.create(urlStr))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(newRequest, HttpResponse.BodyHandlers.ofString());
                JSONObject newObj = new JSONObject(response);

                searchResults = newObj.getJSONObject("query").getJSONArray("search");

                if (!searchResults.isEmpty()) {
                    JSONObject firstResult = searchResults.getJSONObject(0);
                    title = firstResult.getString("title");

                } else {
                    System.out.println("No results found.");
                }

            }

            title = title.replaceAll(" ", "_");

            String wikipediaApiJSON = "https://www.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles=" + title.trim();

            //IOException here
            HttpURLConnection httpcon = (HttpURLConnection) new URL(wikipediaApiJSON).openConnection();
            httpcon.addRequestProperty("User-Agent", "Mozilla/5.0");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
                 InputStream modelIn = new
                         FileInputStream("C:\\Users\\FUJITSU\\JavaPrograms\\chatBotNlp\\src\\main\\resources\\en-sent.bin");) {

                //Read line by line
                String responseSB = in.lines().collect(Collectors.joining());


                httpcon.disconnect();

                String result = responseSB.split("extract\":\"")[1];

                //Tell only the 150 first characters of the result
                wikipediaTitle = com.chatbot.util.getKeywords.cleanText(wikipediaTitle).replaceAll("\\\\", "");
                speak.speak(wikipediaTitle);

                String textToTell = result;
                textToTell = com.chatbot.util.getKeywords.cleanText(textToTell).replaceAll("\\\\", "");

                try {
                    SentenceModel sentenceModel = new SentenceModel(modelIn);
                    SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);
                    String[] sentences = sentenceDetector.sentDetect(textToTell);

                    if (sentences.length < 5) {
                        for (String sentence : sentences) {
                            System.out.println("   " + sentence);
                        }
                        for (String sentence : sentences) {
                            speak.speak(sentence);
                        }

                    } else {
                        for (int count = 0; count < 5; count++) {
                            System.out.println("   " + sentences[count]);
                        }
                        for (int count = 0; count < 5; count++) {
                            speak.speak(sentences[count]);
                        }
                    }
                    System.out.println();
                    speak.speak("See More On Wikipedia Below ");
                    System.out.println("See More At: " + "https://en.wikipedia.org/wiki/" + title.trim());


                } catch (IOException e) {
                    System.out.println("Error: Couldn't open page");
                    logger.error("URL is invalid ", e);
                }


            } catch (IOException e) {
                System.out.println("Error: Couldn't open page");
                logger.error("URL is invalid ", e);
                return;
            }
            catch(IndexOutOfBoundsException error) {
                System.out.println("Sorry - I Couldn't Find Anything");
                logger.error("URL is invalid ", error);
            }

        }

        catch(MalformedURLException e){
            System.out.println("Error: Couldn't open page");
            logger.error("URL is invalid ", e);
            return;
        }

        catch(IOException e) {
            System.out.println("Error: Unable to read data from the file");
            logger.error("IOException ", e);
            return;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
