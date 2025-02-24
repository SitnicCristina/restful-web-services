package com.myapp.blog.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.myapp.blog.models.Comment;
import com.myapp.blog.repositories.CommentRepository;
import com.myapp.blog.steps.common.TestContext;
import com.myapp.blog.utils.JsonUtils;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CommentSteps {
    @Autowired
    private TestContext testContext;

    @Autowired
    private CommentRepository commentRepository;

    private ResponseEntity<String> lastResponse;
    private List<Comment> commentFromJson;

    @Value("${server.port}")
    private int port;

    @Value("${username.value}")
    private String username;

    @Value("${password.value}")
    private String password;

    @When("I send a POST request to {string} with ID {int} and comment details from {string}")
    public void i_send_a_post_request_to_with_id_and_comment_details_from(String endpoint, int postId, String jsonFilePath) throws IOException {
        // Load comment details from JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(jsonFilePath);
        if (inputStream == null) {
            throw new RuntimeException("Test data file not found: " + jsonFilePath);
        }

        // Load list of comments but send them individually
        commentFromJson = objectMapper.readValue(inputStream, new TypeReference<List<Comment>>() {});

        // Set up RestTemplate with authentication
        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication(username, password)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(username, password);

        // Construct full endpoint with postId
        String fullEndpoint = "http://localhost:" + port + endpoint.replace("{postId}", String.valueOf(postId));

        for (Comment comment : commentFromJson) {
            HttpEntity<Comment> entity = new HttpEntity<>(comment, headers);

            // Log Request Details
            System.out.println("Sending POST request to: " + fullEndpoint);
            System.out.println("Headers: " + headers);
            System.out.println("Request Body: " + comment);

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
