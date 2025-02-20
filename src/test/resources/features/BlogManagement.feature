Feature: Blog Post Management
#----------------------- USER -----------------------------
#POST	/jpa/users	                    Create a new user
#GET	/jpa/users	                    Get all users
#GET	/jpa/users/{id}	                Get user by ID
#DELETE	/jpa/users/{id}	                Delete user by ID

  Scenario: User - Successfully get all users
    When I send a GET request to "/jpa/users"
    Then the response status should be 200
    And the response should contain details

  Scenario: User - Successfully create users from JSON file
    When I send a POST request to "/jpa/users" with user details from "testdata/users.json"
    Then the response status should be 201

  Scenario: User - Successfully get a user by ID
    When I send a GET request to "/jpa/users/{id}" with ID 6
    Then the response status should be 200
    And the response should contain the user details for ID 6

  Scenario: User - Successfully delete a user by ID
    When I send a DELETE request to "/jpa/users/{id}" with ID 6
    Then the response status should be 204

#----------------------- CATEGORY -----------------------------
# POST	    /jpa/categories	                Create a new category
# GET	    /jpa/categories	                Get all categories
# GET	    /jpa/categories/{id}	        Get category by ID
# DELETE	/jpa/categories/{id}	        Delete category by ID

  Scenario: Category - Successfully get all categories
    When I send a GET request to "/jpa/categories"
    Then the response status should be 200
    And the response should contain details

  Scenario: Category - Successfully create a new category
    When I send a POST request to "/jpa/categories" with category details from "testdata/categories.json"
    Then the response status should be 200

  Scenario: Category - Successfully get category by ID
    When I send a GET request to "/jpa/categories/{id}" with ID 2
    Then the response status should be 200
    And the response should contain the category details for ID 2

  Scenario: Category - Successfully delete a category by ID
    When I send a DELETE request to "/jpa/categories/{id}" with ID 8
    Then the response status should be 204

#----------------------- POST -----------------------------
#POST	/jpa/users/{userId}/posts	    Create a new post for a user
#GET	/jpa/posts	                    Get all posts
#GET	/jpa/posts/{id}	                Get post by ID
#GET	/jpa/users/{userId}/posts	    Get posts by user ID
#DELETE	/jpa/posts/{id}	                Delete post by ID

  Scenario: Post - Successfully create a new blog post for a specific user
    When I send a GET request to "/jpa/users/{id}" with ID 2
    Then the response status should be 200
    When I send a POST request to "/jpa/users/{userId}/posts" with ID 2 and post details from "testdata/posts.json"
    Then the response status should be 200

  Scenario: Post - Successfully get all posts
    When I send a GET request to "/jpa/posts"
    Then the response status should be 200
    And the response should contain details

  Scenario: Post - Successfully get post by ID
    When I send a GET request to "/jpa/posts/{id}" with ID 1
    Then the response status should be 200
    And the response should contain the post details for ID 1

  Scenario: Post - Successfully get posts by user ID
    When I send a GET request to "/jpa/users/{id}" with ID 2
    Then the response status should be 200
    When I send a GET request to "/jpa/users/{userId}/posts" with ID 2
    Then the response status should be 200
    And the response should contain details

  Scenario: Post - Successfully delete a post by ID
    When I send a DELETE request to "/jpa/posts/{id}" with ID 6
    Then the response status should be 204

#----------------------- COMMENTS -----------------------------
#POST	/jpa/posts/{postId}/comments	Create a comment for a post
#GET	/jpa/comments	                Get all comments
#GET	/jpa/comments/{id}	            Get comment by ID
#DELETE	/jpa/comments/{id}	            Delete comment by ID

  Scenario: Comment - Successfully get all comments
    When I send a GET request to "/jpa/comments"
    Then the response status should be 200
    And the response should contain details

  Scenario: Comment - Successfully create a new comment for a specific post
    When I send a GET request to "/jpa/posts/{id}" with ID 1
    Then the response status should be 200
    When I send a POST request to "/jpa/posts/{postId}/comments" with ID 1 and comment details from "testdata/comments.json"
    Then the response status should be 200

  Scenario: Comment - Successfully get comment by ID
    When I send a GET request to "/jpa/comments/{id}" with ID 1
    Then the response status should be 200
    And the response should contain the comment details for ID 1

  Scenario: Comment - Successfully delete a comment by ID
    When I send a DELETE request to "/jpa/comments/{id}" with ID 7
    Then the response status should be 204

#----------------------- LIKES -----------------------------
#POST	/jpa/posts/{postId}/likes	    Like a post
#GET	/jpa/likes	                    Get all likes
#DELETE	/jpa/likes/{id}	                Remove a like by ID

  Scenario: Like - Successfully get all likes
    When I send a GET request to "/jpa/likes"
    Then the response status should be 200
    And the response should contain details

  Scenario: Like - Successfully create a new like for a specific post
    When I send a GET request to "/jpa/posts/{id}" with ID 1
    Then the response status should be 200
    When I send a POST request to "/jpa/posts/{postId}/likes" with ID 1 and like details from "testdata/likes.json"
    Then the response status should be 200

  Scenario: Like - Successfully delete a like by ID
    When I send a DELETE request to "/jpa/likes/{id}" with ID 2
    Then the response status should be 204