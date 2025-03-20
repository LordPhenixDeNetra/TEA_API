package com.netradev.tout_est_africain.service;

import com.netradev.tout_est_africain.domain.Order;
import com.netradev.tout_est_africain.domain.Product;
import com.netradev.tout_est_africain.domain.Review;
import com.netradev.tout_est_africain.domain.User;
import com.netradev.tout_est_africain.model.ReviewDTO;
import com.netradev.tout_est_africain.model.ReviewType;
import com.netradev.tout_est_africain.repos.OrderRepository;
import com.netradev.tout_est_africain.repos.ProductRepository;
import com.netradev.tout_est_africain.repos.ReviewRepository;
import com.netradev.tout_est_africain.repos.UserRepository;
import com.netradev.tout_est_africain.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final BadgeService badgeService;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository,
                         ProductRepository productRepository, OrderRepository orderRepository,
                         BadgeService badgeService) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.badgeService = badgeService;
    }

    // CRUD operations
    public List<ReviewDTO> findAllReviews() {
        final List<Review> reviews = reviewRepository.findAll(Sort.by("id"));
        return reviews.stream()
            .map(review -> mapToDTO(review, new ReviewDTO()))
            .toList();
    }

    public ReviewDTO getReview(Long id) {
        return reviewRepository.findById(id)
            .map(review -> mapToDTO(review, new ReviewDTO()))
            .orElseThrow(NotFoundException::new);
    }

    public Long createReview(ReviewDTO reviewDTO) {
        Review review = new Review();
        mapToEntity(reviewDTO, review);
        Long reviewId = reviewRepository.save(review).getId();

        // Update user ratings based on the review type
        if (review.getTargetUser() != null) {
            updateUserRatings(review.getTargetUser());
            badgeService.checkAndAwardBadges(review.getTargetUser());
        }

        return reviewId;
    }

    public void updateReview(Long id, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(NotFoundException::new);

        User targetUserBefore = review.getTargetUser();
        mapToEntity(reviewDTO, review);
        reviewRepository.save(review);

        // Update user ratings if target user changed or remained the same
        if (targetUserBefore != null) {
            updateUserRatings(targetUserBefore);
            badgeService.checkAndAwardBadges(targetUserBefore);
        }

        if (review.getTargetUser() != null &&
            (targetUserBefore == null || !targetUserBefore.getId().equals(review.getTargetUser().getId()))) {
            updateUserRatings(review.getTargetUser());
            badgeService.checkAndAwardBadges(review.getTargetUser());
        }
    }

    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(NotFoundException::new);

        User targetUser = review.getTargetUser();
        reviewRepository.deleteById(id);

        if (targetUser != null) {
            updateUserRatings(targetUser);
            badgeService.checkAndAwardBadges(targetUser);
        }
    }

    // Specialized methods
    public List<ReviewDTO> findReviewsByUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(NotFoundException::new);
        List<Review> reviews = reviewRepository.findByTargetUser(user);
        return reviews.stream()
            .map(review -> mapToDTO(review, new ReviewDTO()))
            .toList();
    }

    public List<ReviewDTO> findReviewsByProduct(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(NotFoundException::new);
        List<Review> reviews = reviewRepository.findByProduct(product);
        return reviews.stream()
            .map(review -> mapToDTO(review, new ReviewDTO()))
            .toList();
    }

    public List<ReviewDTO> findReviewsByOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(NotFoundException::new);
        List<Review> reviews = reviewRepository.findByOrder(order);
        return reviews.stream()
            .map(review -> mapToDTO(review, new ReviewDTO()))
            .toList();
    }

    // Rating calculation
    private void updateUserRatings(User user) {
        // Update seller rating
        Double sellerRating = reviewRepository.getAverageRatingByUserAndType(user, ReviewType.SELLER);
        if (sellerRating != null) {
            user.setSellerRating(sellerRating);
        }

        // Update buyer rating
        Double buyerRating = reviewRepository.getAverageRatingByUserAndType(user, ReviewType.BUYER);
        if (buyerRating != null) {
            user.setBuyerRating(buyerRating);
        }

        // Update shipper rating
        Double shipperRating = reviewRepository.getAverageRatingByUserAndType(user, ReviewType.SHIPPER);
        if (shipperRating != null) {
            user.setShipperRating(shipperRating);
        }

        userRepository.save(user);
    }

    // Mapping methods
    private ReviewDTO mapToDTO(Review review, ReviewDTO reviewDTO) {
        reviewDTO.setId(review.getId());

        if (review.getReviewer() != null) {
            reviewDTO.setReviewerId(review.getReviewer().getId());
            reviewDTO.setReviewerName(review.getReviewer().getFirstname() + " " + review.getReviewer().getLastname());
        }

        if (review.getTargetUser() != null) {
            reviewDTO.setTargetUserId(review.getTargetUser().getId());
            reviewDTO.setTargetUserName(review.getTargetUser().getFirstname() + " " + review.getTargetUser().getLastname());
        }

        if (review.getProduct() != null) {
            reviewDTO.setProductId(review.getProduct().getId());
            reviewDTO.setProductName(review.getProduct().getLibelle());
        }

        if (review.getOrder() != null) {
            reviewDTO.setOrderId(review.getOrder().getId());
        }

        reviewDTO.setRating(review.getRating());
        reviewDTO.setComment(review.getComment());
        reviewDTO.setReviewType(review.getReviewType());
//        reviewDTO.setDateCreated(review.getDateCreated());

        return reviewDTO;
    }

    private Review mapToEntity(ReviewDTO reviewDTO, Review review) {
        if (reviewDTO.getReviewerId() != null) {
            review.setReviewer(userRepository.findById(reviewDTO.getReviewerId())
                .orElseThrow(() -> new NotFoundException("Reviewer not found")));
        }

        if (reviewDTO.getTargetUserId() != null) {
            review.setTargetUser(userRepository.findById(reviewDTO.getTargetUserId())
                .orElseThrow(() -> new NotFoundException("Target user not found")));
        }

        if (reviewDTO.getProductId() != null) {
            review.setProduct(productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found")));
        }

        if (reviewDTO.getOrderId() != null) {
            review.setOrder(orderRepository.findById(reviewDTO.getOrderId())
                .orElseThrow(() -> new NotFoundException("Order not found")));
        }

        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setReviewType(reviewDTO.getReviewType());

        return review;
    }

}
