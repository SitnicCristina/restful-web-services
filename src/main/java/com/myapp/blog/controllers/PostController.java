package com.myapp.blog.controllers;

import com.myapp.blog.models.Post;
import com.myapp.blog.repositories.PostRepository;
import com.myapp.blog.repositories.UserRepository;
import com.myapp.blog.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/jpa/posts")
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @GetMapping("/jpa/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Integer id) {
        Optional<Post> post = postRepository.findById(id);
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/jpa/users/{userId}/posts")
    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Post> posts = postRepository.findByUser_Id(userId);
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/jpa/users/{userId}/posts")
    public ResponseEntity<Post> createPost(@RequestBody Post post, @PathVariable Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        post.setUser(user.get());
        Post savedPost = postRepository.save(post);
        return ResponseEntity.ok(savedPost);
    }

    @DeleteMapping("/jpa/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
