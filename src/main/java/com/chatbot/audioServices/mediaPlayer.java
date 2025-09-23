package com.chatbot.audioServices;

import java.util.Scanner;
import javax.sound.sampled.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.chatbot.APIServices.flightAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class mediaPlayer {
    private static final Logger logger = LoggerFactory.getLogger(mediaPlayer.class);
    private static final String PATH = "C:\\Users\\FUJITSU\\Music\\";
    private static final String TYPE = ".wav";

    public void songPlayer(String name) {
        Scanner scanner = new Scanner(System.in);

        name = name + TYPE;
        name = PATH + name;

        File file = new File(name);

        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file)) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            String response = "";

            while(!response.equals("Q")) {
                System.out.println("""
                                                P - Play
                                                S - Stop
                                                R - Reset
                                                Q - Quit
                                                """);
                System.out.print("Enter Choice: ");
                response = scanner.next().toUpperCase();

                scanner.nextLine();

                switch(response) {
                    case "P":
                        System.out.println("Now Playing: " + name);
                        clip.start();

                        break;

                    case "S":
                        clip.stop();
                        break;

                    case "R":
                        clip.setMicrosecondPosition(0);
                        break;

                    case "Q":
                        clip.close();
                        break;

                    default:
                        System.out.println("Invalid input");
                        break;
                }
            }

        }
        catch (FileNotFoundException e) {
            logger.error("Media File Not Found - This Song Does Not Exist ", e);

        }
        catch (UnsupportedAudioFileException e) {
            logger.error("Unsupported file ", e);

        }
        catch(IOException e) {
            logger.error("Unable to Read/Write From File ", e);

        }
        catch (LineUnavailableException e) {
            System.out.println("The File Cannot Be Opened Because It Is In Use By Another Application");
            logger.error("The File Cannot Be Opened Because It Is In Use By Another Application ", e);

        }

        scanner.close();
    }

    public void soundPlayer(String name) {
        name = name + TYPE;
        name = PATH + name;

        File file = new File(name);

        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file)) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            String response = "";

            while (true) {
                clip.start();
                if(clip.getMicrosecondPosition() >= 7040000) {
                    break;
                }
            }

            try {
                Thread.sleep(70500);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Stopped audio");

            }

        }
        catch (FileNotFoundException e) {
            logger.error("Media File Not Found - This Song Does Not Exist ", e);

        }
        catch (UnsupportedAudioFileException e) {
            logger.error("Unsupported file ", e);

        }
        catch(IOException e) {
            logger.error("Unable to Read/Write From File ", e);

        }
        catch (LineUnavailableException e) {
            System.out.println("The File Cannot Be Opened Because It Is In Use By Another Application");
            logger.error("The File Cannot Be Opened Because It Is In Use By Another Application ", e);

        }
    }

}
