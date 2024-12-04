package org.example.sd_94vs1.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sd_94vs1.entity.Blog;
import org.example.sd_94vs1.entity.Review;
import org.example.sd_94vs1.entity.User;
import org.example.sd_94vs1.exception.BadRequestException;
import org.example.sd_94vs1.exception.ResourceNotFoundException;
import org.example.sd_94vs1.model.request.UpsertReviewRequest;
import org.example.sd_94vs1.repository.BlogRepository;
import org.example.sd_94vs1.repository.ReviewRepository;
import org.example.sd_94vs1.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BlogRepository blogRepository;
    private final HttpSession session;
    private final UserRepository userRepository;
    private final BlogService blogService;

    public List<Review> getReviewsOfMovie(String blogCode) {
        return reviewRepository.findByBlogCodeOrderByCreatedAtDesc(blogCode);
    }
    private String generateReviewCode() {
        // Tạo mã review tự động (có thể thêm logic tạo mã unique)
        return "rv" + System.currentTimeMillis();
    }
    public Review createReview(UpsertReviewRequest request, String blogCode, String userCode) {
        User user = userRepository.findById(userCode)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại."));

        Blog blog = blogService.getBlogById(blogCode);

        Review review = Review.builder()
                .reviewCode(generateReviewCode())
                .content(request.getContent())
                .rating(request.getRating())
                .blog(blog)
                .user(user)
                .build();

        return reviewRepository.save(review);
    }

    public Review updateReview(String reviewCode, UpsertReviewRequest request, String blogCode) {
        Blog blog = blogRepository.findById(blogCode)
                .orElseThrow(() -> new EntityNotFoundException("Blog not found"));

        Review review = reviewRepository.findById(reviewCode)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setBlog(blog);

        return reviewRepository.save(review);
    }

    public void deleteReview(String reviewCode) {
        // Lấy thông tin user từ trong session với key "currentUser"
        User currentUser = (User) session.getAttribute("currentUser");

        // Tìm review theo reviewCode
        Review review = reviewRepository.findById(reviewCode) // Kiểm tra xem review có tồn tại không
                .orElseThrow(() -> new ResourceNotFoundException("Review không tồn tại"));

        // Kiểm tra xem user có phải là người tạo review không
        if (!review.getUser().getUserCode().equals(currentUser.getUserCode())) {
            throw new BadRequestException("Bạn không có quyền xóa review này");
        }

        // Xóa review
        reviewRepository.delete(review);
    }



}
