package com.chatbot.APIServices;

import com.chatbot.util.speak;
import com.chatbot.util.getKeywords;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.json.JSONArray;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class newsAPI {
    private static final String API_KEY_NEWS = "pub_924e0b93858445d5a68f32318e67ba42";
    private static final getKeywords CLEAN = new getKeywords();
    private static final Logger logger = LoggerFactory.getLogger(newsAPI.class);

    public void getNews() {
        String urlString = "";
        urlString = "https://newsdata.io/api/1/latest?apikey=" + API_KEY_NEWS + "&q=BBC&language=en&country=my,wo&category=top";

        try {
            //IOException here
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //IOException here
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10_000); // 10s
            conn.setReadTimeout(15_000);

            //IOException here
            try (InputStream inputStream = conn.getInputStream();
                 BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {


                String inputLine;

                StringBuffer content = new StringBuffer();

                //IOException here
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine.trim());
                }

                JSONObject root = new JSONObject(content.toString());
                JSONArray data = root.getJSONArray("results");

                int count;
                int relevantCount = 0;

                ArrayList<String> titleList = new ArrayList<>();

                for (count = 0; count < data.length(); count++) {
                    JSONObject headline = data.getJSONObject(count);

                    // Checks the headline is already inside to prevent duplicates.
                    if (headline.getString("language").equals("english") &&
                            (!titleList.contains(headline.getString("title")))) {

                        String title = headline.getString("title");

                        titleList.add(title);

                        // Helps to produce a concise description that doesn't repeat title
                        String description = headline.getString("description").replaceAll(title, "");

                        relevantCount++;

                        // cleans up formatting errors from news outlets
                        title = getKeywords.cleanText(title);
                        description = getKeywords.cleanText(description);

                        System.out.println();

                        // Numbers the headlines
                        System.out.println(relevantCount + "- " + title);
                        speak.speak(relevantCount + " " + title);

                        //IOException here
                        InputStream modelIn = new FileInputStream("C:\\Users\\FUJITSU\\JavaPrograms\\chatBotNlp\\src\\main\\resources\\en-sent.bin");

                        try {
                            SentenceModel sentenceModel = new SentenceModel(modelIn);
                            SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);
                            String[] sentences = sentenceDetector.sentDetect(description);

                            ArrayList<String> descriptions = new ArrayList<>();

                            // limits the number of sentences of description output to produce
                            // a good summary
                            if (sentences.length < 3) {
                                for (String sentence : sentences) {
                                    if (!descriptions.contains(sentence)) {
                                        System.out.println("   " + sentence);
                                    }
                                }

                                for (String sentence : sentences) {
                                    if (!descriptions.contains(sentence)) {
                                        speak.speak(sentence);
                                        descriptions.add(sentence);
                                    }
                                }

                            } else {
                                for (int i = 0; i < 3; i++) {
                                    System.out.println("   " + sentences[i]);
                                }

                                for (int i = 0; i < 3; i++) {
                                    speak.speak(sentences[i]);
                                }

                            }

                        } catch (IOException e) {
                            System.out.println(description);
                            speak.speak(description);

                            logger.error("Error - Couldn't load language model from OpenNLP line 82 ", e);

                        } finally {
                            if (modelIn != null) {
                                try {
                                    modelIn.close();
                                } catch (IOException e) {
                                    logger.error("couldn't close news model line 119 ", e);
                                }
                            }
                        }
                    }
                }
                System.out.println();
                System.out.println("That's all for today. Come back tomorrow for new headlines");
                speak.speak("That's all for today. Come back tomorrow for new headlines");


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
