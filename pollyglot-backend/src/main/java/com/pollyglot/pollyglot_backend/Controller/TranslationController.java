package com.pollyglot.pollyglot_backend.Controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pollyglot.pollyglot_backend.service.HuggingFaceService;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class TranslationController {

    private final HuggingFaceService huggingFaceService;

    // Constructor injection for HuggingFaceService
    @Autowired
    public TranslationController(HuggingFaceService huggingFaceService) {
        this.huggingFaceService = huggingFaceService;
    }

    /**
     * Handles POST requests to /api/translate for text translation.
     *
     * @param request A map containing "text" (the text to translate) and "targetLanguage" (the language code).
     * @return A ResponseEntity containing a map with "translatedText" or "error" message.
     */
    @PostMapping("/translate")
    public ResponseEntity<Map<String, String>> translateText(@RequestBody Map<String, String> request) {
        String textToTranslate = request.get("text");
        String targetLanguage = request.get("targetLanguage");

        // Basic input validation
        if (textToTranslate == null || textToTranslate.trim().isEmpty() || targetLanguage == null || targetLanguage.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Text and target language are required."));
        }

        try {
            // Call the HuggingFaceService to perform the translation
            String translatedText = huggingFaceService.translate(textToTranslate, targetLanguage);
            // Return a successful response with the translated text
            return ResponseEntity.ok(Map.of("translatedText", translatedText));
        } catch (IllegalArgumentException e) {
            // Handle unsupported language errors
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // Catch any other exceptions during translation and return a server error
            e.printStackTrace(); // Print stack trace for debugging purposes
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to translate text: " + e.getMessage()));
        }
    }
}