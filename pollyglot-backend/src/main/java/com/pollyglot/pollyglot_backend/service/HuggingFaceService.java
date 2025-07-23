package com.pollyglot.pollyglot_backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

@Service
public class HuggingFaceService {

    // Injects the Hugging Face API token from application.properties
    @Value("${huggingface.api.token}")
    private String huggingFaceApiToken;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper; // Used for parsing JSON responses

    // A map to store the Hugging Face model URLs for different target languages.
    // You can add more language pairs and their corresponding models here.
    // These are examples using Helsinki-NLP/opus-mt models, which are good for specific language pairs.
    // If you want a single multilingual model, you might use 'facebook/m2m100-1.2B'
    // but its API usage (input/output format) might differ slightly.
    private static final Map<String, String> MODEL_URLS = new HashMap<>();
    static {
        MODEL_URLS.put("fr", "https://router.huggingface.co/hf-inference/models/Helsinki-NLP/opus-mt-en-fr"); // English to French
        MODEL_URLS.put("es", "https://router.huggingface.co/hf-inference/models/Helsinki-NLP/opus-mt-en-es"); // English to Spanish
        MODEL_URLS.put("ja", "https://router.huggingface.co/hf-inference/models/Helsinki-NLP/opus-mt-en-jap"); // English to Japanese
        // Add more language mappings here, e.g., for German:
        // MODEL_URLS.put("de", "https://api-inference.huggingface.co/models/Helsinki-NLP/opus-mt-en-de");
    }

    // Constructor for dependency injection of RestTemplate and ObjectMapper
    public HuggingFaceService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Translates the given text to the target language using a Hugging Face model.
     *
     * @param text The text to be translated.
     * @param targetLanguage The target language code (e.g., "fr", "es", "ja").
     * @return The translated text.
     * @throws Exception if translation fails or an unsupported language is provided.
     */
    public String translate(String text, String targetLanguage) throws Exception {
        // Get the specific model URL for the target language
        String modelUrl = MODEL_URLS.get(targetLanguage);
        if (modelUrl == null) {
            throw new IllegalArgumentException("Unsupported target language: " + targetLanguage);
        }

        // Set up HTTP headers for the request to Hugging Face
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(huggingFaceApiToken); // Authenticate with your Hugging Face API token
        headers.setContentType(MediaType.APPLICATION_JSON); // Indicate that the request body is JSON

        // --- ADD THIS LINE ---
        headers.setAccept(java.util.Collections.singletonList(MediaType.APPLICATION_JSON)); // What we expect to receive
        // --- END ADDITION ---

        // Create the request body. Hugging Face Inference API typically expects {"inputs": "your text"}
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("inputs", text);

        // Create the HTTP entity (headers + body)
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // Make the POST request to the Hugging Face Inference API
        ResponseEntity<String> response = restTemplate.exchange(
                modelUrl,
                HttpMethod.POST,
                entity,
                String.class // Expecting a String response (JSON)
        );

        // Check if the request was successful (2xx status code)
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            // Parse the JSON response from Hugging Face.
            // It typically returns an array like: [{"translation_text": "Bonjour, comment allez-vous?"}]
            JsonNode root = objectMapper.readTree(response.getBody());

            // Extract the "translation_text" from the JSON response
            if (root.isArray() && root.size() > 0 && root.get(0).has("translation_text")) {
                return root.get(0).get("translation_text").asText();
            } else {
                // Handle unexpected JSON structure
                throw new RuntimeException("Unexpected Hugging Face API response format: " + response.getBody());
            }
        } else {
            // Handle API errors (non-2xx status codes)
            String errorBody = response.getBody() != null ? response.getBody() : "No response body";
            throw new RuntimeException("Hugging Face API error: " + response.getStatusCode() + " - " + errorBody);
        }
    }
}
