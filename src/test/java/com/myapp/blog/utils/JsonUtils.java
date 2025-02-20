package com.myapp.blog.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.ResponseEntity;

public class JsonUtils {

    public static void printPrettyJson(ResponseEntity<String> response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            String responseBody = response.getBody();
            if (isJson(responseBody, objectMapper)) {
                // Parse JSON and pretty print
                JsonNode json = objectMapper.readTree(responseBody);
                String prettyJson = objectMapper.writeValueAsString(json);
                System.out.println("Response Body (Pretty JSON):\n" + prettyJson);
            } else {
                // Print plain text response as is
                System.out.println("Response Body (Plain Text):\n" + responseBody);
            }
        } catch (Exception e) {
            System.out.println("Failed to process response: " + e.getMessage());
        }
    }

    private static boolean isJson(String responseBody, ObjectMapper objectMapper) {
        try {
            objectMapper.readTree(responseBody);
            return true;  // It's valid JSON
        } catch (Exception e) {
            return false; // It's not JSON
        }
    }
}
