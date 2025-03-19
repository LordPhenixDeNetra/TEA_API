package com.netradev.tout_est_africain.repos;

import com.netradev.tout_est_africain.domain.OderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OderDetailsRepository extends JpaRepository<OderDetails, Long> {
    List<OderDetails> findByOderId(Long oderId);
}
