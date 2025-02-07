package com.myapp.blog.controllers;

import com.myapp.blog.models.Comment;
import com.myapp.blog.repositories.CommentRepository;
import com.myapp.blog.repositories.PostRepository;
import com.myapp.blog.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/jpa/comments")
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @GetMapping("/jpa/comments/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Integer id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/jpa/posts/{postId}/comments")
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment, @PathVariable Integer postId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        comment.setPost(post.get());
        Comment savedComment = commentRepository.save(comment);
        return ResponseEntity.ok(savedComment);
    }

    @DeleteMapping("/jpa/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id) {
        commentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
