package com.netradev.tout_est_africain.repos;

import com.netradev.tout_est_africain.domain.Categorie;
import com.netradev.tout_est_africain.domain.Product;
import com.netradev.tout_est_africain.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findFirstByCategorie(Categorie categorie);

    Product findFirstBySeller(User user);

    // Pour les produits mis en avant
    List<Product> findTop10ByOrderByAverageRatingDesc();
    List<Product> findTop10ByOrderByDateCreatedDesc();

    // Pour les produits par catégorie ou vendeur
    List<Product> findByCategorie(Categorie categorie);
    List<Product> findBySeller(User seller);

}
