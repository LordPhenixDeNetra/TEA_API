package com.netradev.tout_est_africain.service;

import com.netradev.tout_est_africain.domain.Badge;
import com.netradev.tout_est_africain.domain.User;
import com.netradev.tout_est_africain.domain.UserBadge;
import com.netradev.tout_est_africain.model.BadgeDTO;
import com.netradev.tout_est_africain.model.RoleType;
import com.netradev.tout_est_africain.model.UserBadgeDTO;
import com.netradev.tout_est_africain.repos.BadgeRepository;
import com.netradev.tout_est_africain.repos.UserBadgeRepository;
import com.netradev.tout_est_africain.repos.UserRepository;
import com.netradev.tout_est_africain.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final UserRepository userRepository;

    public BadgeService(BadgeRepository badgeRepository, UserBadgeRepository userBadgeRepository,
                        UserRepository userRepository) {
        this.badgeRepository = badgeRepository;
        this.userBadgeRepository = userBadgeRepository;
        this.userRepository = userRepository;
    }

    // CRUD for Badge
    public List<BadgeDTO> findAllBadges() {
        return badgeRepository.findAll(Sort.by("id")).stream()
            .map(badge -> mapToDTO(badge, new BadgeDTO()))
            .toList();
    }

    public BadgeDTO getBadge(Long id) {
        return badgeRepository.findById(id)
            .map(badge -> mapToDTO(badge, new BadgeDTO()))
            .orElseThrow(NotFoundException::new);
    }

    public Long createBadge(BadgeDTO badgeDTO) {
        Badge badge = new Badge();
        mapToEntity(badgeDTO, badge);
        return badgeRepository.save(badge).getId();
    }

    public void updateBadge(Long id, BadgeDTO badgeDTO) {
        Badge badge = badgeRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        mapToEntity(badgeDTO, badge);
        badgeRepository.save(badge);
    }

    public void deleteBadge(Long id) {
        badgeRepository.deleteById(id);
    }

    // UserBadge operations
    public List<UserBadgeDTO> getUserBadges(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(NotFoundException::new);
        return userBadgeRepository.findByUser(user).stream()
            .map(userBadge -> mapToDTO(userBadge, new UserBadgeDTO()))
            .toList();
    }

    public void awardBadge(Long userId, String badgeCode) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found"));
        Badge badge = badgeRepository.findByCode(badgeCode)
            .orElseThrow(() -> new NotFoundException("Badge not found"));

        if (userBadgeRepository.existsByUserAndBadge(user, badge)) {
            return; // Badge already awarded
        }

        UserBadge userBadge = new UserBadge();
        userBadge.setUser(user);
        userBadge.setBadge(badge);
        userBadge.setDateAwarded(OffsetDateTime.now());
        userBadgeRepository.save(userBadge);
    }

    public void removeBadge(Long userId, Long badgeId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found"));
        Badge badge = badgeRepository.findById(badgeId)
            .orElseThrow(() -> new NotFoundException("Badge not found"));

        userBadgeRepository.findByUser(user).stream()
            .filter(ub -> ub.getBadge().getId().equals(badgeId))
            .findFirst()
            .ifPresent(userBadgeRepository::delete);
    }

    // Badge award logic
    public void checkAndAwardBadges(User user) {
        checkSellerBadges(user);
        checkBuyerBadges(user);
        checkShipperBadges(user);
        checkGeneralBadges(user);
    }

    private void checkSellerBadges(User user) {
        boolean isSeller = user.getRoles().stream()
            .anyMatch(role -> role.getRoleName() == RoleType.SELLER);

        if (!isSeller) return;

        if (user.getSuccessfulSales() >= 1) {
            awardBadge(user.getId(), "SELLER_BEGINNER");
        }

        if (user.getSuccessfulSales() >= 10) {
            awardBadge(user.getId(), "SELLER_EXPERIENCED");
        }

        if (user.getSuccessfulSales() >= 50) {
            awardBadge(user.getId(), "SELLER_EXPERT");
        }

        if (user.getSuccessfulSales() >= 100) {
            awardBadge(user.getId(), "SELLER_MASTER");
        }

        if (user.getSellerRating() >= 4.8 && user.getSuccessfulSales() >= 10) {
            awardBadge(user.getId(), "FIVE_STAR_SELLER");
        }
    }

    private void checkBuyerBadges(User user) {
        boolean isBuyer = user.getRoles().stream()
            .anyMatch(role -> role.getRoleName() == RoleType.CUSTOMER);

        if (!isBuyer) return;

        if (user.getSuccessfulPurchases() >= 5) {
            awardBadge(user.getId(), "LOYAL_CUSTOMER");
        }

        if (user.getSuccessfulPurchases() >= 20) {
            awardBadge(user.getId(), "PREMIUM_CUSTOMER");
        }
    }

    private void checkShipperBadges(User user) {
        boolean isShipper = user.getRoles().stream()
            .anyMatch(role -> role.getRoleName() == RoleType.SHIPPER);

        if (!isShipper) return;

        if (user.getSuccessfulDeliveries() >= 10) {
            awardBadge(user.getId(), "RELIABLE_SHIPPER");
        }

        if (user.getSuccessfulDeliveries() >= 50) {
            awardBadge(user.getId(), "EXPERT_SHIPPER");
        }

        if (user.getSuccessfulDeliveries() >= 100) {
            awardBadge(user.getId(), "PREMIUM_SHIPPER");
        }

        if (user.getShipperRating() >= 4.8 && user.getSuccessfulDeliveries() >= 20) {
            awardBadge(user.getId(), "FIVE_STAR_SHIPPER");
        }
    }

    private void checkGeneralBadges(User user) {
        // Account age badge (requires calculating account age)
        long accountAgeDays = java.time.Duration.between(
            user.getDateCreated(), OffsetDateTime.now()).toDays();

        if (accountAgeDays >= 365) {
            awardBadge(user.getId(), "ONE_YEAR_MEMBER");
        }

        if (accountAgeDays >= 730) {
            awardBadge(user.getId(), "TWO_YEAR_MEMBER");
        }
    }

    // Mapping methods
    private BadgeDTO mapToDTO(Badge badge, BadgeDTO badgeDTO) {
        badgeDTO.setId(badge.getId());
        badgeDTO.setCode(badge.getCode());
        badgeDTO.setName(badge.getName());
        badgeDTO.setDescription(badge.getDescription());
        badgeDTO.setIconUrl(badge.getIconUrl());
        badgeDTO.setCategory(badge.getCategory());
        return badgeDTO;
    }

    private Badge mapToEntity(BadgeDTO badgeDTO, Badge badge) {
        badge.setCode(badgeDTO.getCode());
        badge.setName(badgeDTO.getName());
        badge.setDescription(badgeDTO.getDescription());
        badge.setIconUrl(badgeDTO.getIconUrl());
        badge.setCategory(badgeDTO.getCategory());
        return badge;
    }

    private UserBadgeDTO mapToDTO(UserBadge userBadge, UserBadgeDTO userBadgeDTO) {
        userBadgeDTO.setId(userBadge.getId());
        userBadgeDTO.setUserId(userBadge.getUser().getId());
        userBadgeDTO.setUserName(userBadge.getUser().getFirstname() + " " + userBadge.getUser().getLastname());
        userBadgeDTO.setBadgeId(userBadge.getBadge().getId());
        userBadgeDTO.setBadgeName(userBadge.getBadge().getName());
        userBadgeDTO.setBadgeDescription(userBadge.getBadge().getDescription());
        userBadgeDTO.setBadgeIconUrl(userBadge.getBadge().getIconUrl());
//        userBadgeDTO.setDateAwarded(userBadge.getDateAwarded());
        return userBadgeDTO;
    }
}
