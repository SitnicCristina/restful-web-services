package com.myapp.blog.steps.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.blog.config.CucumberSpringConfiguration;
import com.myapp.blog.models.Category;
import com.myapp.blog.models.Post;
import com.myapp.blog.models.User;
import com.myapp.blog.models.Comment;
import com.myapp.blog.utils.JsonUtils;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

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

        JsonUtils.printPrettyJson(lastResponse);

        Assert.assertEquals(expectedStatus, lastResponse.getStatusCode().value());
    }

    @Then("the response should contain details")
    public void the_response_should_contain_details() throws Exception {
        ResponseEntity<String> lastResponse = testContext.getLastResponse();
        Assert.assertNotNull("Response should not be null", lastResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        List<?> details = objectMapper.readValue(lastResponse.getBody(), new TypeReference<List<?>>() {});

        Assert.assertNotNull("Response body should not be null", details);
        Assert.assertFalse("Response list should not be empty", details.isEmpty());
    }

    @Then("the response should contain the user details for ID {int}")
    public void the_response_should_contain_the_user_details_for_id(int userId) throws Exception {
        ResponseEntity<String> lastResponse = testContext.getLastResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        User actualUser = objectMapper.readValue(lastResponse.getBody(), User.class);

        Assert.assertNotNull("Response body should not be null", actualUser);
        Assert.assertEquals("User ID should match", Integer.valueOf(userId), actualUser.getId());
    }

    @Then("the response should contain the category details for ID {int}")
    public void the_response_should_contain_the_category_details_for_id(int categoryId) throws Exception {
        ResponseEntity<String> lastResponse = testContext.getLastResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        Category actualCategory = objectMapper.readValue(lastResponse.getBody(), Category.class);

        Assert.assertNotNull("Response body should not be null", actualCategory);
        Assert.assertEquals("User ID should match", Integer.valueOf(categoryId), actualCategory.getId());
    }

    @Then("the response should contain the post details for ID {int}")
    public void the_response_should_contain_the_post_details_for_id(int postId) throws Exception {
        ResponseEntity<String> lastResponse = testContext.getLastResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        Post actualPost = objectMapper.readValue(lastResponse.getBody(), Post.class);

        Assert.assertNotNull("Response body should not be null", actualPost);
        Assert.assertEquals("User ID should match", Integer.valueOf(postId), actualPost.getId());
    }

    @Then("the response should contain the comment details for ID {int}")
    public void the_response_should_contain_the_comment_details_for_id(int commentId) throws Exception {
        ResponseEntity<String> lastResponse = testContext.getLastResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        Comment actualComment = objectMapper.readValue(lastResponse.getBody(), Comment.class);

        Assert.assertNotNull("Response body should not be null", actualComment);
        Assert.assertEquals("User ID should match", Integer.valueOf(commentId), actualComment.getId());
    }

}
