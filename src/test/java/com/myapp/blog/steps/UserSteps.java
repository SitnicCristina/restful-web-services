package com.myapp.blog.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.myapp.blog.config.CucumberSpringConfiguration;
import com.myapp.blog.models.Category;
import com.myapp.blog.models.Post;
import com.myapp.blog.models.User;
import com.myapp.blog.repositories.UserRepository;
import com.myapp.blog.steps.common.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 * The {@code UserSteps} class defines Cucumber step definitions for testing
 * the User API endpoints. It provides automated test steps for performing
 * CRUD operations (GET, POST, DELETE) on users.
 *
**/
 @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserSteps extends CucumberSpringConfiguration {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestContext testContext;

    private ResponseEntity<String> lastResponse;
    private List<User> usersFromJson;
    private List<Post> postFromJson;

    @Value("${server.port}")
    private int port;

    @Value("${username.value}")
    private String username;

    @Value("${password.value}")
    private String password;

    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String endpoint) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication(username, password)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Log Request Details
        System.out.println("Sending GET request to: http://localhost:" + port + endpoint);
        System.out.println("Headers: " + headers.toString());

        lastResponse = restTemplate.exchange(
                "http://localhost:" + port + endpoint,
                HttpMethod.GET,
                entity,
                String.class
        );

        testContext.setLastResponse(lastResponse);
    }

    @When("I send a GET request to {string} with ID {int}")
    public void i_send_a_get_request_to(String endpoint, int id) {
       RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication(username, password)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(username, password);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String fullEndpoint = "http://localhost:" + port + endpoint
                .replace("{id}", String.valueOf(id))
                .replace("{userId}", String.valueOf(id));

        // Log Request Details
        System.out.println("Sending GET request to: " + fullEndpoint);
        System.out.println("Headers: " + headers);

        lastResponse = restTemplate.exchange(
                fullEndpoint,
                HttpMethod.GET,
                entity,
                String.class
        );

        testContext.setLastResponse(lastResponse);
    }

    @When("I send a POST request to {string} with user details from {string}")
    public void i_send_a_post_request_with_user_details_from(String endpoint, String jsonFilePath) throws IOException {
        // Load JSON file using ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(jsonFilePath);
        if (inputStream == null) {
            throw new RuntimeException("Test data file not found: " + jsonFilePath);
        }

        usersFromJson = objectMapper.readValue(inputStream, new TypeReference<List<User>>() {});

        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication(username, password)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(username, password);

        for (User user : usersFromJson) {
            HttpEntity<User> entity = new HttpEntity<>(user, headers);

            System.out.println("Sending POST request to: http://localhost:" + port + endpoint);
            System.out.println("Headers: " + headers);
            System.out.println("Request Body: " + user);

            lastResponse = restTemplate.exchange(
                    "http://localhost:" + port + endpoint,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            testContext.setLastResponse(lastResponse);
        }
    }

    @When("I send a DELETE request to {string} with ID {int}")
    public void i_send_a_delete_request_to(String endpoint, int userId) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication(username, password)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String fullEndpoint = "http://localhost:" + port + endpoint.replace("{id}", String.valueOf(userId));

        // Log Request Details
        System.out.println("Sending DELETE request to: " + fullEndpoint);
        System.out.println("Headers: " + headers);

        lastResponse = restTemplate.exchange(
                fullEndpoint,
                HttpMethod.DELETE,
                entity,
                String.class
        );

        testContext.setLastResponse(lastResponse);

        // Log Response Details
        System.out.println("Response Status: " + lastResponse.getStatusCode());
        System.out.println("Response Headers: " + lastResponse.getHeaders());
    }

    //   DataBase checks
    @Given("a user with ID {int} exists in the database")
    public void a_user_with_id_exists_in_the_database(int userId) {
        validateUserInDatabaseById(userId);
    }

    @Then("the user with ID {int} should not be present in the database")
    public void the_user_with_id_should_not_be_present_in_the_database(int userId) {
        Optional<User> userInDb = userRepository.findById(userId);
        Assert.assertFalse("User should not be found in the database", userInDb.isPresent());
    }

    public void validateUserInDatabase(int userId, String expectedName) {
        Optional<User> userInDb = userRepository.findById(userId);
        Assert.assertTrue("User should be found in the database", userInDb.isPresent());
        Assert.assertEquals("User name should match", expectedName, userInDb.get().getName());
    }

    public void validateUserInDatabaseById(int userId) {
        Optional<User> userInDb = userRepository.findById(userId);
        Assert.assertTrue("User should be found in the database", userInDb.isPresent());
    }
}
