package com.chatbot.util;

import com.chatbot.util.speak;
import java.io.IOException;

public class openProgram {
    speak SPEAK = new speak();

    openProgram(String command) {
        if (findAction("note pad", command)) {
            try {
                Runtime.getRuntime().exec("cmd /c start notepad.exe");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if (findAction("edge", command)) {
            try {
                Runtime.getRuntime().exec("cmd /c start msedge.exe");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (findAction("explorer", command)) {
            try {
                Runtime.getRuntime().exec("cmd /c start explorer.exe");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (findAction("chrome", command)) {
            try {
                Runtime.getRuntime().exec("cmd /c start chrome.exe");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public boolean findAction(String keyWord, String command) {
        if(command.contains(keyWord) &&
                !(command.toLowerCase().contains("close") ||
                        command.toLowerCase().contains("shut") ||
                        command.toLowerCase().contains("quit") ||
                        command.toLowerCase().contains("exit") ||
                        command.toLowerCase().contains("kill")
                )) {

            System.out.println("Opening " + keyWord);
            speak.speak("Opening " + keyWord);

            return true;
        } else {
            return false;
        }

    }

}
