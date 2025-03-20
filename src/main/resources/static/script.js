//const BASE_URL = "http://localhost/jpa"; // Use this localhost url for local environment
const BASE_URL = "https://webserviceh2hibernate.onrender.com";
let categoriesList = []; // Store categories globally

// Fetch and display all posts
async function fetchPosts() {
    try {
        console.log("Fetching posts from:", `${BASE_URL}/jpa/posts`);
        let response = await fetch(`${BASE_URL}/jpa/posts`, {
            method: "GET",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            }
        });

        if (!response.ok) {
            let errorMessage = await response.text();
            throw new Error(`Failed to fetch posts: ${response.status} ${errorMessage}`);
        }

        let posts = await response.json();
        console.log("Posts fetched successfully:", posts);

        let postList = document.getElementById("post-list");
        postList.innerHTML = ""; // Clear previous data

        if (posts.length === 0) {
            postList.innerHTML = "<li>No posts found.</li>";
            return;
        }

        posts.forEach(post => addPostToUI(post)); // Use function to display posts with delete buttons

    } catch (error) {
        console.error("Error fetching posts:", error);
        document.getElementById("post-list").innerHTML = `<li style="color: red;">Error loading posts: ${error.message}</li>`;
    }
}

// Fetch categories and populate dropdown
async function fetchCategories() {
    try {
        console.log("Fetching categories from:", `${BASE_URL}/jpa/categories`);
        let response = await fetch(`${BASE_URL}/jpa/categories`, {
            method: "GET",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            }
        });

        if (!response.ok) {
            throw new Error(`Failed to fetch categories: ${response.status}`);
        }

        let categories = await response.json();
        console.log("Categories fetched successfully:", categories);

        categoriesList = categories; // Store categories globally

        let categorySelect = document.getElementById("category-select");
        categorySelect.innerHTML = '<option value="">Select a category</option>'; // Reset dropdown

        categories.forEach(category => {
            let option = document.createElement("option");
            option.value = category.name;  // Store category name as value
            option.textContent = category.name;  // Show category name
            categorySelect.appendChild(option);
        });

    } catch (error) {
        console.error("Error fetching categories:", error);
        document.getElementById("category-select").innerHTML = '<option value="">Error loading categories</option>';
    }
}

// Add a new post
async function addPost() {
    let title = document.getElementById("title").value.trim();
    let content = document.getElementById("content").value.trim();
    let selectedCategoryName = document.getElementById("category-select").value;

    if (title.length < 5) {
        alert("Title should have at least 5 characters.");
        return;
    }
    if (content.length < 10) {
        alert("Content should have at least 10 characters.");
        return;
    }
    if (!selectedCategoryName) {
        alert("Please select a category.");
        return;
    }

    // Find the correct category ID
    let selectedCategory = categoriesList.find(category => category.name === selectedCategoryName);
    if (!selectedCategory) {
        alert("Invalid category selected.");
        return;
    }

    let postData = {
        title: title,
        content: content,
        user: { id: 1 }, // Hardcoded user ID
        category: { id: selectedCategory.id }
    };

    try {
        console.log("Adding post:", postData);

        let response = await fetch(`${BASE_URL}/jpa/users/1/posts`, {
            method: "POST",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            },
            body: JSON.stringify(postData)
        });

        if (!response.ok) {
            let errorMessage = await response.text();
            throw new Error(`Failed to add post: ${response.status} ${errorMessage}`);
        }

        let newPost = await response.json();
        console.log("Post added successfully:", newPost);

        newPost.category = selectedCategory; // Add category object to new post
        addPostToUI(newPost); // Add new post dynamically without reloading all posts
        document.getElementById("post-form").reset(); // Clear form fields

    } catch (error) {
        console.error("Error adding post:", error);
        alert(`Error adding post: ${error.message}`);
    }
}

// Add a post dynamically to the UI without fetching all posts
function addPostToUI(post) {
    let postList = document.getElementById("post-list");

    let listItem = document.createElement("li");
    listItem.setAttribute("data-id", post.id); // Store post ID
    listItem.innerHTML = `
        <h3>${post.title}</h3>
        <p><strong>Content:</strong> ${post.content}</p>
        <p><strong>Category:</strong> ${post.category.name}</p>
        <button onclick="deletePost(${post.id}, this)">Delete</button> <!-- Delete Button -->
    `;

    postList.appendChild(listItem);
}

// Delete a post
async function deletePost(postId, button) {
    try {
        let response = await fetch(`${BASE_URL}/jpa/posts/${postId}`, {
            method: "DELETE",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            }
        });

        if (!response.ok) {
            throw new Error(`Failed to delete post: ${response.status}`);
        }

        console.log(`Post ${postId} deleted successfully.`);
        button.parentElement.remove(); // Remove post from UI

    } catch (error) {
        console.error("Error deleting post:", error);
        alert(`Error deleting post: ${error.message}`);
    }
}
