package com.netradev.tout_est_africain.repos;

import com.netradev.tout_est_africain.domain.Badge;
import com.netradev.tout_est_africain.model.BadgeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Optional<Badge> findByCode(String code);
    List<Badge> findByCategory(BadgeCategory category);
}
