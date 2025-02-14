Feature: Blog Post Management
#----------------------- USER -----------------------------
#GET	/jpa/users	                    Get all users
#GET	/jpa/users/{id}	                Get user by ID
#POST	/jpa/users	                    Create a new user
#DELETE	/jpa/users/{id}	                Delete user by ID

  Scenario: User - Successfully get all users
    When I send a GET request to "/jpa/users"
    Then the response status should be 200
    And the response should contain details

  Scenario: User - Successfully get a user by ID
    Given a user with ID 5 exists in the database
    When I send a GET request to "/jpa/users/{id}" with ID 5
    Then the response status should be 200
    And the response should contain the user details for ID 5

  Scenario: User - Successfully create users from JSON file
    When I send a POST request to "/jpa/users" with user details from "testdata/users.json"
    Then the response status should be 200

  Scenario: User - Successfully delete a user by ID
    Given a user with ID 5 exists in the database
    When I send a DELETE request to "/jpa/users/{id}" with ID 5
    Then the response status should be 204
    And the user with ID 5 should not be present in the database

#----------------------- CATEGORY -----------------------------
# GET	    /jpa/categories	                Get all categories
# GET	    /jpa/categories/{id}	        Get category by ID
# POST	    /jpa/categories	                Create a new category
# DELETE	/jpa/categories/{id}	        Delete category by ID

  Scenario: Category - Successfully get all categories
    When I send a GET request to "/jpa/categories"
    Then the response status should be 200
    And the response should contain details

  Scenario: Category - Successfully get category by ID
    Given a category with ID 2 exists in the database
    When I send a GET request to "/jpa/categories/{id}" with ID 2
    Then the response status should be 200
    And the response should contain the category details for ID 2

  Scenario: Category - Successfully delete a category by ID
    Given a category with ID 12 exists in the database
    When I send a DELETE request to "/jpa/categories/{id}" with ID 12
    Then the response status should be 204
    And the category with ID 12 should not be present in the database

  Scenario: Category - Successfully create a new category
    When I send a POST request to "/jpa/categories" with category details from "testdata/categories.json"
    Then the response status should be 200

#----------------------- POST -----------------------------
#GET	/jpa/posts	                    Get all posts
#GET	/jpa/posts/{id}	                Get post by ID
#DELETE	/jpa/posts/{id}	                Delete post by ID
#GET	/jpa/users/{userId}/posts	    Get posts by user ID
#POST	/jpa/users/{userId}/posts	    Create a new post for a user

  Scenario: Post - Successfully get all posts
    When I send a GET request to "/jpa/posts"
    Then the response status should be 200
    And the response should contain details

  Scenario: Post - Successfully get post by ID
    Given a post with ID 1 exists in the database
    When I send a GET request to "/jpa/posts/{id}" with ID 1
    Then the response status should be 200
    And the response should contain the post details for ID 1

  Scenario: Post - Successfully delete a post by ID
    Given a post with ID 3 exists in the database
    When I send a DELETE request to "/jpa/posts/{id}" with ID 3
    Then the response status should be 204
    And the post with ID 3 should not be present in the database

  Scenario: Post - Successfully get posts by user ID
    Given a user with ID 2 exists in the database
    When I send a GET request to "/jpa/users/{userId}/posts" with ID 2
    Then the response status should be 200
    And the response should contain details

  Scenario: Post - Successfully create a new blog post for a specific user
    Given a user with ID 2 exists in the database
    When I send a POST request to "/jpa/users/{userId}/posts" with ID 2 and post details from "testdata/posts.json"
    Then the response status should be 200

#----------------------- COMMENTS -----------------------------
#GET	/jpa/comments	                Get all comments
#GET	/jpa/comments/{id}	            Get comment by ID
#POST	/jpa/posts/{postId}/comments	Create a comment for a post
#DELETE	/jpa/comments/{id}	            Delete comment by ID

#----------------------- LIKES -----------------------------
#GET	/jpa/likes	                    Get all likes
#POST	/jpa/posts/{postId}/likes	    Like a post
#DELETE	/jpa/likes/{id}	                Remove a like by ID