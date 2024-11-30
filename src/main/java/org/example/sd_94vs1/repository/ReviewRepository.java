package org.example.sd_94vs1.repository;

import org.example.sd_94vs1.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ReviewRepository extends JpaRepository<Review, String> {
    @Query("SELECT r FROM Review r WHERE r.blog.blogCode = :blogCode ORDER BY r.createdAt DESC")
    List<Review> findByBlogCodeOrderByCreatedAtDesc(@Param("blogCode") String blogCode);



}
