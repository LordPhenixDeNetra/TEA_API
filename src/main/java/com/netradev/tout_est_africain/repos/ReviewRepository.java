package com.netradev.tout_est_africain.repos;

import com.netradev.tout_est_africain.domain.Order;
import com.netradev.tout_est_africain.domain.Product;
import com.netradev.tout_est_africain.domain.Review;
import com.netradev.tout_est_africain.domain.User;
import com.netradev.tout_est_africain.model.ReviewType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByTargetUser(User targetUser);
    List<Review> findByReviewer(User reviewer);
    List<Review> findByProduct(Product product);
    List<Review> findByOrder(Order order);
    List<Review> findByTargetUserAndReviewType(User targetUser, ReviewType reviewType);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.targetUser = :user AND r.reviewType = :reviewType")
    Double getAverageRatingByUserAndType(@Param("user") User user, @Param("reviewType") ReviewType reviewType);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.targetUser = :user AND r.reviewType = :reviewType AND r.rating >= 4")
    Integer countPositiveReviewsByUserAndType(@Param("user") User user, @Param("reviewType") ReviewType reviewType);

}
