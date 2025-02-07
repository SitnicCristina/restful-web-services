package com.myapp.blog.controllers;

import com.myapp.blog.models.Like;
import com.myapp.blog.models.Post;
import com.myapp.blog.repositories.LikeRepository;
import com.myapp.blog.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class LikeController {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/jpa/likes")
    public List<Like> getAllLikes() {
        return likeRepository.findAll();
    }

    @PostMapping("/jpa/posts/{postId}/likes")
    public ResponseEntity<Like> createLike(@RequestBody Like like, @PathVariable Integer postId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        like.setPost(post.get());
        Like savedLike = likeRepository.save(like);
        return ResponseEntity.ok(savedLike);
    }

    @DeleteMapping("/jpa/likes/{id}")
    public ResponseEntity<Void> deleteLike(@PathVariable Integer id) {
        likeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
