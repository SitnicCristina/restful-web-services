#HTTP	Endpoint	                    Description
#------------------------------------------------------------------
#GET	/jpa/users	                    Get all users
#GET	/jpa/users/{id}	                Get user by ID
#POST	/jpa/users	                    Create a new user
#DELETE	/jpa/users/{id}	                Delete user by ID
#GET	/jpa/posts	                    Get all posts
#GET	/jpa/posts/{id}	                Get post by ID
#GET	/jpa/users/{userId}/posts	    Get posts by user ID
#POST	/jpa/users/{userId}/posts	    Create a new post for a user
#DELETE	/jpa/posts/{id}	                Delete post by ID
#GET	/jpa/comments	                Get all comments
#GET	/jpa/comments/{id}	            Get comment by ID
#POST	/jpa/posts/{postId}/comments	Create a comment for a post
#DELETE	/jpa/comments/{id}	            Delete comment by ID
#GET	/jpa/likes	                    Get all likes
#POST	/jpa/posts/{postId}/likes	    Like a post
#DELETE	/jpa/likes/{id}	                Remove a like by ID
#GET	/jpa/categories	                Get all categories
#GET	/jpa/categories/{id}	        Get category by ID
#POST	/jpa/categories	                Create a new category
#DELETE	/jpa/categories/{id}	        Delete category by ID

Feature: Users via API using JSON Data

  Scenario: Successfully get all users
    When I send a GET request to "/jpa/users"
    Then the response status should be 200
    And the response should contain a list of users

  Scenario: Successfully create users from JSON file
    When I send a POST request to "/jpa/users" with user details from "testdata/users.json"
    Then the response should have status 201
    And the user should be present in the database
