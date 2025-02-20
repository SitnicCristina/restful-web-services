package com.myapp.blog.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.myapp.blog.models.Comment;
import com.myapp.blog.models.Like;

import com.myapp.blog.repositories.LikeRepository;
import com.myapp.blog.steps.common.TestContext;
import com.myapp.blog.utils.JsonUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class LikesSteps {

    @Autowired
    private TestContext testContext;

    @Autowired
    private LikeRepository likeRepository;

    private ResponseEntity<String> lastResponse;
    private List<Like> likeFromJson;

    @Value("${server.port}")
    private int port;

    @Value("${username.value}")
    private String username;

    @Value("${password.value}")
    private String password;

    @When("I send a POST request to {string} with ID {int} and like details from {string}")
    public void i_send_a_post_request_to_with_id_and_like_details_from(String endpoint, int postId, String jsonFilePath) throws IOException {
        // Load comment details from JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(jsonFilePath);
        if (inputStream == null) {
            throw new RuntimeException("Test data file not found: " + jsonFilePath);
        }

        // Load list of likes but send them individually
        likeFromJson = objectMapper.readValue(inputStream, new TypeReference<List<Like>>() {});

        // Set up RestTemplate with authentication
        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication(username, password)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(username, password);

        // Construct full endpoint with postId
        String fullEndpoint = "http://localhost:" + port + endpoint.replace("{postId}", String.valueOf(postId));

        for (Like like : likeFromJson) {
            HttpEntity<Like> entity = new HttpEntity<>(like, headers);

            // Log Request Details
            System.out.println("Sending POST request to: " + fullEndpoint);
            System.out.println("Headers: " + headers);
            System.out.println("Request Body: " + like);

            lastResponse = restTemplate.exchange(
                    fullEndpoint,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            testContext.setLastResponse(lastResponse);

            // Log Response Details
            System.out.println("Response Status: " + lastResponse.getStatusCode());
            System.out.println("Response Headers: " + lastResponse.getHeaders());

            JsonUtils.printPrettyJson(lastResponse);
        }
    }
}
