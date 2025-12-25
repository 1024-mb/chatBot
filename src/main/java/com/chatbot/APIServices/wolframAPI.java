package com.chatbot.APIServices;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.chatbot.util.speak;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class wolframAPI {
    private static final Logger logger = LoggerFactory.getLogger(wikipediaAPI.class);

    public void getResponse(String query) {
        try {
            String queryFormatted = query.replaceAll(" ", "%20");

            String urlString = "https://api.wolframalpha.com/v2/query?appid=" +
                    System.getenv("APPID"); + "L&input=" +
                    queryFormatted + "&includepodid=Result&format=plaintext";

            //IOException here
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //IOException here
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10_000); // 10s
            conn.setReadTimeout(15_000);

            //IOException here
            try(InputStream inputStream = conn.getInputStream(); BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {
                String inputLine;
                StringBuffer content = new StringBuffer();

                //IOException here
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine.trim());
                }
                conn.disconnect();

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();

                InputSource is = new InputSource(new StringReader(String.valueOf(content)));
                Document document = builder.parse(is);

                document.getDocumentElement().normalize();

                String output = "";

                try {
                    Node queryResult = document.getElementsByTagName("plaintext").item(0);
                    output = queryResult.getFirstChild().getTextContent();

                }
                catch(Exception e) {
                    Node queryResult;
                    Element result;
                    String improvedQuery = "";

                    if(document.getElementsByTagName("didyoumeans").getLength() >0) {
                        queryResult = document.getElementsByTagName("didyoumeans").item(0);
                        result = (Element) queryResult.getFirstChild();
                        improvedQuery = result.getAttribute("val");

                    } else if(document.getElementsByTagName("relatedqueries").getLength() >0){
                        queryResult = document.getElementsByTagName("relatedqueries").item(0);
                        result = (Element) queryResult.getFirstChild();
                        improvedQuery = result.getTextContent();
                    }

                    urlString = "https://api.wolframalpha.com/v2/query?appid=" +
                            "K8J32RHU7" + "L&input=" +
                            improvedQuery.replaceAll(" ", "%20")
                            + "&includepodid=Result&format=plaintext";

                    //IOException here
                    url = new URL(urlString);
                    HttpURLConnection connSecond = (HttpURLConnection) url.openConnection();

                    //IOException here
                    connSecond.setRequestMethod("GET");
                    connSecond.setConnectTimeout(10_000); // 10s
                    connSecond.setReadTimeout(15_000);

                    //IOException here
                    try(InputStream secondStream = connSecond.getInputStream(); BufferedReader SecondIn = new BufferedReader(new InputStreamReader(secondStream))) {
                        String secondInput;
                        StringBuffer secondContent = new StringBuffer();

                        //IOException here
                        while ((secondInput = SecondIn.readLine()) != null) {
                            secondContent.append(secondInput.trim());
                        }
                        connSecond.disconnect();

                        DocumentBuilderFactory secondFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder secondBuilder = secondFactory.newDocumentBuilder();

                        InputSource secondis = new InputSource(new StringReader(String.valueOf(secondContent)));
                        Document secondDocument = secondBuilder.parse(secondis);

                        secondDocument.getDocumentElement().normalize();

                        output = "";

                        Node secondQueryResult = secondDocument.getElementsByTagName("plaintext").item(0);
                        output = secondQueryResult.getFirstChild().getTextContent();


                    } catch(IOException error) {
                        error.printStackTrace();
                        System.out.println("IOERROR");
                        logger.error("ERROR: I/O Error", e);

                    } catch(Exception unfound) {
                        System.out.println("Sorry - I couldn't find what you were looking for");
                        speak.speak("Sorry - I couldn't find what you were looking for");
                    }
                }


                String regex = "\\b\\w*Data\\w*\\b";
                output = output.replaceAll(regex, "");

                System.out.println(output);
                speak.speak(output);


            }

            catch(IOException e) {
                e.printStackTrace();
                System.out.println("IOERROR");
                logger.error("ERROR: I/O Error", e);
            }

        }
         catch (ParserConfigurationException e) {
            logger.error("ERROR: Couldn't configure parser correctly  |  ParserConfigurationException", e);
            System.out.println("Sorry, An Error Has Occured");

        } catch (SAXException e) {
            logger.error("ERROR: Couldn't read XML document correctly | SAXException ", e);
            System.out.println("ERROR: Couldn't read XML document correctly | SAXException ");

        }

        catch(MalformedURLException e){
            System.out.println("Error: Couldn't open page | MalformedURLException");
            logger.error("URL is invalid ", e);
        }

        catch(IOException e) {
            System.out.println("Error: Unable to read data from the file | IOException");
            logger.error("IOException ", e);
        }
    }

}
