package com.myapp.blog.steps;

import com.myapp.blog.config.CucumberSpringConfiguration;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GetAllUsersSteps extends CucumberSpringConfiguration {

    private Response response;
    ResponseEntity<String> lastResponse;

    @Value("${server.port}")
    private int port;

    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String endpoint) {
        String username = "user";
        String password = "userPass";

        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication(username, password)  // ✅ Use RestTemplateBuilder for authentication
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(username, password);  // ✅ Correctly encode Basic Auth

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // ✅ Print Headers Before Request
        System.out.println("Sending GET request to: http://localhost:" + port + endpoint);
        System.out.println("Headers: " + headers.toString());

        lastResponse = restTemplate.exchange(
                "http://localhost:" + port + endpoint,
                HttpMethod.GET,
                entity,
                String.class
        );

        // ✅ Print Response Headers
        System.out.println("Response Headers: " + lastResponse.getHeaders().toString());
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int expectedStatus) {
        Assert.assertEquals(expectedStatus, response.getStatusCode());
    }

    @Then("the response should contain a list of users")
    public void the_response_should_contain_a_list_of_users() {
        response.then().body("$.size()", greaterThanOrEqualTo(0));
    }
}
