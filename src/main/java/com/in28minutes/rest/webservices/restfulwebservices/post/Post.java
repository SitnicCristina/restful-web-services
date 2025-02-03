package com.in28minutes.rest.webservices.restfulwebservices.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.in28minutes.rest.webservices.restfulwebservices.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Ensure consistency with User entity
    private Integer id;

    @Size(min = 10, message = "Description should have at least 10 characters")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")  // Explicitly defining foreign key column
    @JsonIgnore
    private User user;

    // Default constructor
    public Post() {}

    // Constructor with parameters
    public Post(String description, User user) {
        this.description = description;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
