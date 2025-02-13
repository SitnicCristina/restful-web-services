Feature: Blog Post Management
#----------------------- USER -----------------------------
#GET	/jpa/users	                    Get all users
#GET	/jpa/users/{id}	                Get user by ID
#POST	/jpa/users	                    Create a new user
#DELETE	/jpa/users/{id}	                Delete user by ID

  Scenario: User - Successfully get all users
    When I send a GET request to "/jpa/users"
    Then the response status should be 200
    And the response should contain a list of users

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
# POST	    /jpa/categories	                Create a new category         +
# DELETE	/jpa/categories/{id}	        Delete category by ID

  Scenario: Category - Successfully get all categories
    When I send a GET request to "/jpa/categories"
    Then the response status should be 200
    And the response should contain a list of users

  Scenario: Category - Successfully get category by ID
    Given a category with ID 5 exists in the database
    When I send a GET request to "/jpa/categories/{id}" with ID 2
    Then the response status should be 200
    And the response should contain the user details for ID 2

  Scenario: Category - Successfully create a new category
    When I send a POST request to "/jpa/categories" with category details from "testdata/categories.json"
    Then the response status should be 200

  Scenario: Category - Successfully delete a category by ID
    Given a category with ID 11 exists in the database
    When I send a DELETE request to "/jpa/categories/{id}" with ID 11
    Then the response status should be 204
    And the category with ID 11 should not be present in the database

#----------------------- POST -----------------------------
#GET	/jpa/posts	                    Get all posts
#GET	/jpa/posts/{id}	                Get post by ID
#DELETE	/jpa/posts/{id}	                Delete post by ID
#GET	/jpa/users/{userId}/posts	    Get posts by user ID
#POST	/jpa/users/{userId}/posts	    Create a new post for a user

#----------------------- COMMENTS -----------------------------
#GET	/jpa/comments	                Get all comments
#GET	/jpa/comments/{id}	            Get comment by ID
#POST	/jpa/posts/{postId}/comments	Create a comment for a post
#DELETE	/jpa/comments/{id}	            Delete comment by ID

#----------------------- LIKES -----------------------------
#GET	/jpa/likes	                    Get all likes
#POST	/jpa/posts/{postId}/likes	    Like a post
#DELETE	/jpa/likes/{id}	                Remove a like by ID