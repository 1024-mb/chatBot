package com.chatbot.util;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class speak {
    private static final String VOICE_NAME = "kevin16";

    public static void speak(String speechText) {
        Voice voice;

        speechText = speechText.replaceAll("°C", " degrees celcius");
        speechText = speechText.replaceAll("(?<=[A-Z])(?=[A-Z])", " ");
        speechText = speechText.replaceAll("(\\d{2})(\\d{2})", "$1 $2");

        System.setProperty("freetts.voices",  "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        VoiceManager voiceManager = VoiceManager.getInstance();


        voice = voiceManager.getVoice(VOICE_NAME);
        voice.allocate();
        voice.speak(speechText);
        voice.deallocate();

        voice = null;
        voiceManager = null;
        System.clearProperty("freetts.voices");


    }


}
