# chatBotNlp

A Java-based chatbot/assistant project that integrates offline speech recognition (VOSK) with several API services for weather, news, translation, Wikipedia, WolframAlpha, and flight information.

This repository contains the project source, included VOSK model assets, and helper utilities for audio, text processing, and external API integrations.

## Key Features

- Offline speech recognition using the VOSK model included in `assets/models/vosk-model-small-en-us-0.15/`
- Multiple API service wrappers located in `src/main/java/com/chatbot/APIServices/`:
  - `weatherAPI.java`, `newsAPI.java`, `translateAPI.java`, `wikipediaAPI.java`, `wolframAPI.java`, `flightAPI.java`
- Audio helper utilities in `src/main/java/com/chatbot/audioServices/` (media playback, number conversion)
- Simple application entrypoint: `com.chatbot.app.App`
- Utilities for text, date/time, speaking output and simple math operations in `src/main/java/com/chatbot/util/`

## Requirements

- JDK 11 or newer (project is Maven-based — adjust JDK as needed)
- Apache Maven (to build and run)
- A working microphone for speech input (if using voice features)
- Recommended: Windows (repository was developed on Windows and commands below use PowerShell examples)

## Included Model & Assets

- The VOSK small English model is included under:

  - `assets/models/vosk-model-small-en-us-0.15/`

  Keep this folder in place if you intend to run offline ASR.

## Build

Open a PowerShell terminal in the project root (`pom.xml` location) and run:

```powershell
mvn clean package

This will compile sources and produce build artifacts under `target/`.

To run the main class (if `maven-exec-plugin` is available in the project), you can use:

```powershell
mvn -Dexec.mainClass="com.chatbot.app.App" exec:java
```

Or run the produced JAR directly (replace `<your-jar>.jar` with the actual filename in `target/`):

```powershell
java -jar target\<your-jar>.jar
```

Alternatively, open the project in an IDE (IntelliJ IDEA or Eclipse) and run the `com.chatbot.app.App` main class.

## Configuration & Environment Variables

Some API wrappers may require API keys or configuration (for example, news, translation, or flight-related services). Examples for PowerShell:

```powershell
setx NEWS_API_KEY "your_news_api_key"
setx TRANSLATE_API_KEY "your_translate_api_key"
setx WOLFRAM_APP_ID "your_wolfram_app_id"
```

The project currently expects configuration to be read by the API service classes or supplied via environment variables. Inspect `src/main/java/com/chatbot/APIServices/` for implementation details.

## Project Structure (important files)

- `pom.xml` — Maven build file
- `src/main/java/com/chatbot/app/App.java` — Application entrypoint
- `src/main/java/com/chatbot/APIServices/` — API wrappers for external services
- `src/main/java/com/chatbot/audioServices/` — Audio helpers and media player
- `src/main/java/com/chatbot/util/` — Utility classes (`speak.java`, `getKeywords.java`, `getDateTime.java`, `mathOpn.java`, `openProgram.java`, `timer.java`)
- `assets/models/vosk-model-small-en-us-0.15/` — Offline speech recognition model files
- `src/main/resources/airportData.csv` — sample data referenced by flight/airport utilities

## Running Tests

Run unit tests with Maven:

```powershell
mvn test
```

There is a sample test at `src/test/java/com/chatbot/AppTest.java`.

## Troubleshooting

- If the application cannot find the VOSK model, make sure the `assets/models/vosk-model-small-en-us-0.15/` folder is present and readable.
- For microphone or audio issues, ensure Java has permissions and your OS-level microphone is available to applications.
- If an external API fails, verify the environment variables/API keys and network connectivity.

## Contributing

If you'd like to contribute:

- Fork the repository, create a branch for your feature/fix, and submit a pull request.
- Add tests for bug fixes and new features when appropriate.


## Acknowledgements

- VOSK — offline speech recognition library and models
- Any external API providers used by the service wrappers



