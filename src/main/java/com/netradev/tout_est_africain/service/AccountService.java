package com.netradev.tout_est_africain.service;

import com.netradev.tout_est_africain.domain.User;
import com.netradev.tout_est_africain.model.AccountStatus;
import com.netradev.tout_est_africain.model.UserReputationDTO;
import com.netradev.tout_est_africain.repos.OrderRepository;
import com.netradev.tout_est_africain.repos.ProductRepository;
import com.netradev.tout_est_africain.repos.UserRepository;
import com.netradev.tout_est_africain.util.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final BadgeService badgeService;

    public AccountService(UserRepository userRepository, OrderRepository orderRepository,
                          ProductRepository productRepository, BadgeService badgeService) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.badgeService = badgeService;
    }

    // Account status management
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(NotFoundException::new);
        user.setActive(true);
        user.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
    }

    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(NotFoundException::new);
        user.setActive(false);
        user.setAccountStatus(AccountStatus.DEACTIVATED);
        userRepository.save(user);
    }

    public void suspendUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(NotFoundException::new);
        user.setActive(false);
        user.setAccountStatus(AccountStatus.SUSPENDED);
        userRepository.save(user);
    }

    public void restrictUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(NotFoundException::new);
        user.setActive(true); // Restricted users can still use the platform but with limitations
        user.setAccountStatus(AccountStatus.RESTRICTED);
        userRepository.save(user);
    }

    // Account reputation management
    public UserReputationDTO getUserReputation(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(NotFoundException::new);

        UserReputationDTO reputationDTO = new UserReputationDTO();
        reputationDTO.setUserId(user.getId());
        reputationDTO.setUserName(user.getFirstname() + " " + user.getLastname());
        reputationDTO.setSellerRating(user.getSellerRating());
        reputationDTO.setBuyerRating(user.getBuyerRating());
        reputationDTO.setShipperRating(user.getShipperRating());
        reputationDTO.setSuccessfulSales(user.getSuccessfulSales());
        reputationDTO.setSuccessfulPurchases(user.getSuccessfulPurchases());
        reputationDTO.setSuccessfulDeliveries(user.getSuccessfulDeliveries());

        // Get badges
        reputationDTO.setBadges(badgeService.getUserBadges(userId));

        // Check if verified (this could be based on having a specific badge)
        reputationDTO.setVerified(user.getBadges().stream()
            .anyMatch(badge -> badge.getBadge().getCode().equals("VERIFIED_ACCOUNT")));

        return reputationDTO;
    }

    // Event handlers for stats updates
    public void incrementSuccessfulSale(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(NotFoundException::new);
        user.setSuccessfulSales(user.getSuccessfulSales() + 1);
        userRepository.save(user);
        badgeService.checkAndAwardBadges(user);
    }

    public void incrementSuccessfulPurchase(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(NotFoundException::new);
        user.setSuccessfulPurchases(user.getSuccessfulPurchases() + 1);
        userRepository.save(user);
        badgeService.checkAndAwardBadges(user);
    }

    public void incrementSuccessfulDelivery(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(NotFoundException::new);
        user.setSuccessfulDeliveries(user.getSuccessfulDeliveries() + 1);
        userRepository.save(user);
        badgeService.checkAndAwardBadges(user);
    }

    // Verification methods
    public void verifyUser(Long userId) {
        // This could involve adding a specific verification badge
        badgeService.awardBadge(userId, "VERIFIED_ACCOUNT");
    }

    public void unverifyUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(NotFoundException::new);

        // Find and remove the verification badge
        user.getBadges().stream()
            .filter(badge -> badge.getBadge().getCode().equals("VERIFIED_ACCOUNT"))
            .findFirst()
            .ifPresent(badge -> badgeService.removeBadge(userId, badge.getBadge().getId()));
    }
}
