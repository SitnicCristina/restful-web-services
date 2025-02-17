package com.myapp.blog.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.ResponseEntity;

public class JsonUtils {

    public static void printPrettyJson(ResponseEntity<String> response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            Object json = objectMapper.readValue(response.getBody(), Object.class);
            String prettyJson = objectMapper.writeValueAsString(json);

            System.out.println("Response Body (Pretty JSON):\n" + prettyJson);
        } catch (Exception e) {
            System.out.println("Failed to pretty-print JSON: " + e.getMessage());
        }
    }
}
