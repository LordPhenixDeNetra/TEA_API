package com.netradev.tout_est_africain.repos;

import com.netradev.tout_est_africain.domain.Product;
import com.netradev.tout_est_africain.domain.ProductSpecification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductSpecificationRepository extends JpaRepository<ProductSpecification, Long> {
    List<ProductSpecification> findByProductId(Long productId);
    void deleteByProduct(Product product);
}
