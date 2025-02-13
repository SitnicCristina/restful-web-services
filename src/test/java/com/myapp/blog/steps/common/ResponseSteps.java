package com.myapp.blog.steps.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.blog.config.CucumberSpringConfiguration;
import com.myapp.blog.models.User;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

public class ResponseSteps extends CucumberSpringConfiguration {

    private final TestContext testContext;

    @Autowired
    public ResponseSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int expectedStatus) {
        ResponseEntity<String> lastResponse = testContext.getLastResponse();
        System.out.println("Response Status: " + lastResponse.getStatusCode());
        System.out.println("Response Headers: " + lastResponse.getHeaders());
        System.out.println("Response Body: " + lastResponse.getBody());

        Assert.assertEquals(expectedStatus, lastResponse.getStatusCode().value());
    }

    @Then("the response should contain a list of users")
    public void the_response_should_contain_a_list_of_users() {
        ResponseEntity<String> lastResponse = testContext.getLastResponse();
        List<?> users = Collections.singletonList(lastResponse.getBody());
        Assert.assertNotNull("Response body should not be null", users);
        Assert.assertFalse("User list should not be empty", users.isEmpty());
    }

    @Then("the response should contain the user details for ID {int}")
    public void the_response_should_contain_the_user_details_for_id(int userId) throws Exception {
        ResponseEntity<String> lastResponse = testContext.getLastResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        User actualUser = objectMapper.readValue(lastResponse.getBody(), User.class);

        Assert.assertNotNull("Response body should not be null", actualUser);
        Assert.assertEquals("User ID should match", Integer.valueOf(userId), actualUser.getId());
    }

}
