package com.netradev.tout_est_africain.repos.custom;

import com.netradev.tout_est_africain.domain.OderDetails;
import com.netradev.tout_est_africain.repos.OderDetailsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.List;

public class OderDetailsRepositoryImpl implements OderDetailsRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<OderDetails> findByOderId(Long oderId) {
        return entityManager.createQuery(
                "SELECT od FROM OderDetails od WHERE od.oderId = :oderId",
                OderDetails.class)
            .setParameter("oderId", oderId)
            .getResultList();
    }

    @Override
    public BigDecimal calculateOrderTotal(Long oderId) {
        return (BigDecimal) entityManager.createQuery(
                "SELECT SUM(od.unitPrice * od.quantity) FROM OderDetails od WHERE od.oderId = :oderId")
            .setParameter("oderId", oderId)
            .getSingleResult();
    }
}
