package com.netradev.tout_est_africain.service;

import com.netradev.tout_est_africain.domain.Categorie;
import com.netradev.tout_est_africain.domain.Product;
import com.netradev.tout_est_africain.domain.User;
import com.netradev.tout_est_africain.model.ProductDTO;
import com.netradev.tout_est_africain.repos.CategorieRepository;
import com.netradev.tout_est_africain.repos.OrderRepository;
import com.netradev.tout_est_africain.repos.ProductRepository;
import com.netradev.tout_est_africain.repos.UserRepository;
import com.netradev.tout_est_africain.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategorieRepository categorieRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public ProductService(final ProductRepository productRepository,
            final CategorieRepository categorieRepository, final UserRepository userRepository,
            final OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.categorieRepository = categorieRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public List<ProductDTO> findAll() {
        final List<Product> products = productRepository.findAll(Sort.by("id"));
        return products.stream()
                .map(product -> mapToDTO(product, new ProductDTO()))
                .toList();
    }

    public ProductDTO get(final Long id) {
        return productRepository.findById(id)
                .map(product -> mapToDTO(product, new ProductDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ProductDTO productDTO) {
        final Product product = new Product();
        mapToEntity(productDTO, product);
        return productRepository.save(product).getId();
    }

    public void update(final Long id, final ProductDTO productDTO) {
        final Product product = productRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(productDTO, product);
        productRepository.save(product);
    }

    public void delete(final Long id) {
        final Product product = productRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        orderRepository.findAllByProducts(product)
                .forEach(order -> order.getProducts().remove(product));
        productRepository.delete(product);
    }

    private ProductDTO mapToDTO(final Product product, final ProductDTO productDTO) {
        productDTO.setId(product.getId());
        productDTO.setLibelle(product.getLibelle());
        productDTO.setStock(product.getStock());
        productDTO.setPrice(product.getPrice());
        productDTO.setCategorie(product.getCategorie() == null ? null : product.getCategorie().getId());
        productDTO.setSeller(product.getSeller() == null ? null : product.getSeller().getId());
        return productDTO;
    }

    private Product mapToEntity(final ProductDTO productDTO, final Product product) {
        product.setLibelle(productDTO.getLibelle());
        product.setStock(productDTO.getStock());
        final Categorie categorie = productDTO.getCategorie() == null ? null : categorieRepository.findById(productDTO.getCategorie())
                .orElseThrow(() -> new NotFoundException("categorie not found"));
        product.setCategorie(categorie);
        final User seller = productDTO.getSeller() == null ? null : userRepository.findById(productDTO.getSeller())
                .orElseThrow(() -> new NotFoundException("seller not found"));
        product.setSeller(seller);
        return product;
    }

}
