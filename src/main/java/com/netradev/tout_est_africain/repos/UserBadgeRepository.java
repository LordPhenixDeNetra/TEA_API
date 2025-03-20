package com.netradev.tout_est_africain.repos;

import com.netradev.tout_est_africain.domain.Badge;
import com.netradev.tout_est_africain.domain.User;
import com.netradev.tout_est_africain.domain.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    List<UserBadge> findByUser(User user);
    boolean existsByUserAndBadge(User user, Badge badge);
}
