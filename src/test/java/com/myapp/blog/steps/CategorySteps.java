package com.myapp.blog.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.myapp.blog.config.CucumberSpringConfiguration;
import com.myapp.blog.models.Category;
import com.myapp.blog.repositories.CategoryRepository;
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
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CategorySteps extends CucumberSpringConfiguration {

    @Autowired
    private TestContext testContext;

    @Autowired
    private CategoryRepository categoryRepository;

    private ResponseEntity<String> lastResponse;
    private List<Category> categoryFromJson;

    @Value("${server.port}")
    private int port;

    @Value("${username.value}")
    private String username;

    @Value("${password.value}")
    private String password;

    @When("I send a POST request to {string} with category details from {string}")
    public void i_send_a_post_request_with_category_details_from(String endpoint, String jsonFilePath) throws IOException {
        // Load JSON file using ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(jsonFilePath);
        if (inputStream == null) {
            throw new RuntimeException("Test data file not found: " + jsonFilePath);
        }

        categoryFromJson = objectMapper.readValue(inputStream, new TypeReference<List<Category>>() {});

        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication(username, password)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(username, password);

        for (Category category : categoryFromJson) {
            HttpEntity<Category> entity = new HttpEntity<>(category, headers);

            System.out.println("Sending POST request to: http://localhost:" + port + endpoint);
            System.out.println("Headers: " + headers);
            System.out.println("Request Body: " + category);

            lastResponse = restTemplate.exchange(
                    "http://localhost:" + port + endpoint,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            testContext.setLastResponse(lastResponse);
        }
    }

    //   DataBase checks
    @Given("a category with ID {int} exists in the database")
    public void a_category_with_id_exists_in_the_database(int categoryId) {
        validateCategoryInDatabaseById(categoryId);
    }

    @Then("the category with ID {int} should not be present in the database")
    public void the_category_with_id_should_not_be_present_in_the_database(int categoryId) {
        Optional<Category> categoryInDb = categoryRepository.findById(categoryId);
        Assert.assertFalse("Category should not be found in the database", categoryInDb.isPresent());
    }

    public void validateCategoryInDatabase(int categoryId, String expectedName) {
        Optional<Category> categoryInDb = categoryRepository.findById(categoryId);
        Assert.assertTrue("Category should be found in the database", categoryInDb.isPresent());
        Assert.assertEquals("Category name should match", expectedName, categoryInDb.get().getName());
    }

    public void validateCategoryInDatabaseById(int categoryId) {
        Optional<Category> categoryInDb = categoryRepository.findById(categoryId);
        Assert.assertTrue("Category should be found in the database", categoryInDb.isPresent());
    }

}
