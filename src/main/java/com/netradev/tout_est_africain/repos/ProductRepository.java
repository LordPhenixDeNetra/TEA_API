package com.netradev.tout_est_africain.repos;

import com.netradev.tout_est_africain.domain.Categorie;
import com.netradev.tout_est_africain.domain.Product;
import com.netradev.tout_est_africain.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findFirstByCategorie(Categorie categorie);

    Product findFirstBySeller(User user);

}
