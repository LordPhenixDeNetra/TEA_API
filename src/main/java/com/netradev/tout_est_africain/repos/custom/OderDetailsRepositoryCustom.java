package com.netradev.tout_est_africain.repos.custom;

import com.netradev.tout_est_africain.domain.OderDetails;

import java.math.BigDecimal;
import java.util.List;

public interface OderDetailsRepositoryCustom {
    List<OderDetails> findByOderId(Long oderId);
    BigDecimal calculateOrderTotal(Long oderId);
}
