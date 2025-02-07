package com.myapp.blog.repositories;

import com.myapp.blog.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

        // Custom method to find posts by user ID
        List<Post> findByUser_Id(Integer userId);
}
