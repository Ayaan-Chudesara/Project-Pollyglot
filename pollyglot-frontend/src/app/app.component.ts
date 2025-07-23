import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
   title = 'PollyGlot'; // Title for the application
  inputText: string = ''; // Holds the text entered by the user
  selectedLanguage: string = 'fr'; // Holds the currently selected target language (default: French)
  translatedText: string = ''; // Holds the translated text received from the backend

  // The URL of your Spring Boot backend's translation endpoint
  // Make sure this matches the URL where your backend is running.
  private backendUrl = 'http://localhost:8080/api/translate';

  // Inject HttpClient service into the component's constructor
  constructor(private http: HttpClient) { }

  /**
   * Handles the "Translate" button click event.
   * Sends the input text and selected language to the backend for translation.
   */
  onTranslate(): void {
    // Basic validation: check if input text is not empty
    if (!this.inputText.trim()) {
      this.translatedText = 'Please enter text to translate.';
      return;
    }

    // Prepare the payload to send to the Spring Boot backend
    const payload = {
      text: this.inputText,
      targetLanguage: this.selectedLanguage
    };

    // Make an HTTP POST request to the backend
    // Subscribe to the observable to handle the response or errors
    this.http.post<any>(this.backendUrl, payload).subscribe({
      next: (response) => {
        // On successful response, update translatedText
        if (response && response.translatedText) {
          this.translatedText = response.translatedText;
        } else {
          // Handle cases where the backend response is empty or malformed
          this.translatedText = 'Translation response was empty or malformed.';
        }
      },
      error: (error) => {
        // Log the error and display an error message to the user
        console.error('Error during translation:', error);
        this.translatedText = `Error: ${error.message || 'Could not connect to translation service.'}`;
      }
    });
  }

  /**
   * Handles the "Speak" button click event.
   * Uses the Web Speech API to speak the translated text aloud,
   * attempting to use the correct language voice.
   */
  onSpeakTranslatedText(): void {
    // Check if translated text exists and if Web Speech API is supported by the browser
    if (!this.translatedText || !('speechSynthesis' in window)) {
      console.warn('No translated text or Web Speech API not supported. Audio playback unavailable.');
      return;
    }

    const utterance = new SpeechSynthesisUtterance(this.translatedText);

    // Map your short language codes to more specific BCP 47 language tags
    // This is crucial for the browser to pick the correct voice.
    let langCode: string;
    switch (this.selectedLanguage) {
      case 'fr':
        langCode = 'fr-FR';
        break;
      case 'es':
        langCode = 'es-ES';
        break;
      case 'ja':
        langCode = 'ja-JP';
        break;
      // Add more cases for other languages if you expand your app
      default:
        langCode = 'en-US'; // Fallback to English if language is not explicitly mapped
    }
    utterance.lang = langCode;

    // Optional: You can try to find a specific voice if needed,
    // but setting utterance.lang is usually sufficient for default voices.
    // const voices = speechSynthesis.getVoices();
    // const selectedVoice = voices.find(voice => voice.lang === langCode);
    // if (selectedVoice) {
    //   utterance.voice = selectedVoice;
    // } else {
    //   console.warn(`No specific voice found for ${langCode}. Using default.`);
    // }

    speechSynthesis.speak(utterance);
  }
}
