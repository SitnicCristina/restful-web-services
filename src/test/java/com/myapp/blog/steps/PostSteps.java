package com.myapp.blog.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.myapp.blog.models.Post;
import com.myapp.blog.repositories.PostRepository;
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

public class PostSteps {

    @Autowired
    private TestContext testContext;

    @Autowired
    private PostRepository postRepository;

    private ResponseEntity<String> lastResponse;
    private List<Post> postFromJson;

    @Value("${server.port}")
    private int port;

    @Value("${username.value}")
    private String username;

    @Value("${password.value}")
    private String password;

    @When("I send a POST request to {string} with ID {int} and post details from {string}")
    public void i_send_a_post_request_to_with_id_and_post_details_from(String endpoint, int userId, String jsonFilePath) throws IOException {
        // Load blog post details from JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(jsonFilePath);
        if (inputStream == null) {
            throw new RuntimeException("Test data file not found: " + jsonFilePath);
        }

        // Load list of posts but send them individually
        postFromJson = objectMapper.readValue(inputStream, new TypeReference<List<Post>>() {});

        // Set up RestTemplate with authentication
        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication(username, password)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(username, password);

        // Construct full endpoint with userId
        String fullEndpoint = "http://localhost:" + port + endpoint.replace("{userId}", String.valueOf(userId));

        for (Post post : postFromJson) {
            HttpEntity<Post> entity = new HttpEntity<>(post, headers);

            // Log Request Details
            System.out.println("Sending POST request to: " + fullEndpoint);
            System.out.println("Headers: " + headers);
            System.out.println("Request Body: " + post);

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
