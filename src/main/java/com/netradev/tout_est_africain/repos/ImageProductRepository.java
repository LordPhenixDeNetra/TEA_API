package com.netradev.tout_est_africain.repos;

import com.netradev.tout_est_africain.domain.ImageProduct;
import com.netradev.tout_est_africain.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ImageProductRepository extends JpaRepository<ImageProduct, Long> {
    List<ImageProduct> findByProductId(Long productId);
    void deleteByProduct(Product product);
}
