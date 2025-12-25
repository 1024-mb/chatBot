package com.chatbot.APIServices;

import com.google.cloud.translate.*;
import com.chatbot.util.speak;

import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;


@SuppressWarnings("sun.misc.Unsafe::objectFieldOffset")
public class translateAPI {

    private static final Logger logger = LoggerFactory.getLogger(weatherAPI.class);
    private static final String TRANSLATE_KEY = System.getenv("TRANSLATE");;
    private static speak speak = new speak();

    public void get_translation(String text, String langIn) {
        String translatedText;

        if(langIn.equals("")) {
            Translate translate = TranslateOptions.newBuilder().setApiKey(TRANSLATE_KEY).build().getService();

            Translation translation = translate.translate(text, Translate.TranslateOption.targetLanguage("en"));

            translatedText = translation.getTranslatedText();

            System.out.println();
            System.out.println("Translated Text: " + translatedText);
        }
        else {
            Translate translate = TranslateOptions.newBuilder().setApiKey(TRANSLATE_KEY).build().getService();

            Translation translation = translate.translate(text, Translate.TranslateOption.targetLanguage(langIn));

            translatedText = translation.getTranslatedText();

            System.out.println();
            System.out.println("Translated Text: " + translatedText);
        }

        try {
            // Load voices
            System.setProperty("freetts.voices",
                    "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

            VoiceManager vm = VoiceManager.getInstance();
            Voice voice = vm.getVoice("kevin16");

            if (voice == null) {
                System.err.println("Voice not found: kevin16");
                return;
            }

            voice.allocate();
            voice.speak(translatedText);
            voice.deallocate();


        } catch (Exception e) {
            logger.error("Error: 84, translateAPI", e);
        }


    }

}
