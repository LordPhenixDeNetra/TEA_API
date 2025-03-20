package com.netradev.tout_est_africain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserReputationDTO {
    private Long userId;
    private String userName;
    private Double sellerRating;
    private Double buyerRating;
    private Double shipperRating;
    private Integer successfulSales;
    private Integer successfulPurchases;
    private Integer successfulDeliveries;
    private List<UserBadgeDTO> badges;
    private boolean verified;
}
