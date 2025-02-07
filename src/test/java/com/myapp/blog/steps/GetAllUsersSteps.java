package com.myapp.blog.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GetAllUsersSteps {

    private Response response;

    @Value("${server.port}")
    private int port;

    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String endpoint) {
        response = given()
                .baseUri("http://localhost:" + port)
                .header("Accept", "application/json")
                .when()
                .get(endpoint)
                .then()
                .extract().response();
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
