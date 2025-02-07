package com.myapp.blog.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

import com.myapp.blog.config.CucumberSpringConfiguration;
import com.myapp.blog.repositories.UserRepository;
import com.myapp.blog.models.User;
import io.cucumber.java.en.*;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

@ContextConfiguration(classes = CucumberSpringConfiguration.class)
@SpringBootTest()
public class AddUserSteps {

    @Autowired
    private UserRepository userRepository;

    private Response response;
    private int userId;
    private List<User> usersFromJson;

    @Value("${server.port}")
    private int port;

    @When("I send a POST request to {string} with user details from {string}")
    public void i_send_a_post_request_with_user_details_from(String endpoint, String jsonFilePath) throws IOException {
        // Load JSON file using ClassLoader
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(jsonFilePath);
        if (inputStream == null) {
            throw new RuntimeException("Test data file not found: " + jsonFilePath);
        }

        usersFromJson = objectMapper.readValue(inputStream, new TypeReference<List<User>>() {
        });

        for (User user : usersFromJson) {
            response = given()
                    .auth().preemptive().basic("user", "userPass")
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .body(user)
                    .when()
                    .post("http://localhost:" + port + endpoint)
                    .then()
                    .statusCode(201)
                    .extract().response();

            userId = response.jsonPath().getInt("id");
        }
    }

    @Then("the response should have status {int}")
    public void the_response_should_have_status(Integer expectedStatus) {
        Assert.assertEquals(expectedStatus.intValue(), response.getStatusCode());
    }

    @Then("the user should be present in the database")
    public void the_user_should_be_present_in_the_database() {
        for (User user : usersFromJson) {
            Optional<User> userInDb = userRepository.findByName(user.getName());
            Assert.assertTrue(userInDb.isPresent());
            Assert.assertEquals(user.getName(), userInDb.get().getName());
        }
    }
}
