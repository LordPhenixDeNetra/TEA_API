package com.netradev.tout_est_africain.rest;

import com.netradev.tout_est_africain.model.ReviewDTO;
import com.netradev.tout_est_africain.service.ReviewService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class ReviewResource {

    private final ReviewService reviewService;

    public ReviewResource(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        return ResponseEntity.ok(reviewService.findAllReviews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(reviewService.getReview(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createReview(@RequestBody @Valid final ReviewDTO reviewDTO) {
        final Long createdId = reviewService.createReview(reviewDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateReview(@PathVariable(name = "id") final Long id,
                                             @RequestBody @Valid final ReviewDTO reviewDTO) {
        reviewService.updateReview(id, reviewDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteReview(@PathVariable(name = "id") final Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByUser(@PathVariable(name = "userId") final Long userId) {
        return ResponseEntity.ok(reviewService.findReviewsByUser(userId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByProduct(@PathVariable(name = "productId") final Long productId) {
        return ResponseEntity.ok(reviewService.findReviewsByProduct(productId));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByOrder(@PathVariable(name = "orderId") final Long orderId) {
        return ResponseEntity.ok(reviewService.findReviewsByOrder(orderId));
    }
}

