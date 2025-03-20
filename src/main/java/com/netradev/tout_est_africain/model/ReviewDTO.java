package com.netradev.tout_est_africain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private Long id;
    private Long reviewerId;
    private String reviewerName;
    private Long targetUserId;
    private String targetUserName;
    private Long productId;
    private String productName;
    private Long orderId;
    private Integer rating;
    private String comment;
    private ReviewType reviewType;
}
