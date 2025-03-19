package com.netradev.tout_est_africain.repos;

import com.netradev.tout_est_africain.domain.Order;
import com.netradev.tout_est_africain.domain.Product;
import com.netradev.tout_est_africain.domain.User;
import java.util.List;

import com.netradev.tout_est_africain.model.OderStatus;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findFirstByBuyer(User user);

    Order findFirstByProducts(Product product);

    List<Order> findAllByProducts(Product product);

    boolean existsByUuidIgnoreCase(String uuid);

    List<Order> findByStatus(OderStatus status);

    List<Order> findByBuyerAndStatus(User buyer, OderStatus status);

}
