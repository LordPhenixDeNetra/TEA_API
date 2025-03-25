package com.netradev.tout_est_africain.service;

import com.netradev.tout_est_africain.domain.*;
import com.netradev.tout_est_africain.model.ProductDTO;
import com.netradev.tout_est_africain.model.ImageProductDTO;
import com.netradev.tout_est_africain.model.ProductSpecificationDTO;
import com.netradev.tout_est_africain.repos.*;
import com.netradev.tout_est_africain.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategorieRepository categorieRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ImageProductRepository imageProductRepository;
    private final ProductSpecificationRepository productSpecificationRepository;

    public ProductService(final ProductRepository productRepository,
            final CategorieRepository categorieRepository, final UserRepository userRepository,
            final OrderRepository orderRepository, final ImageProductRepository imageProductRepository,
            final ProductSpecificationRepository productSpecificationRepository) {
        this.productRepository = productRepository;
        this.categorieRepository = categorieRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.imageProductRepository = imageProductRepository;
        this.productSpecificationRepository = productSpecificationRepository;
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

//    public Long create(final ProductDTO productDTO) {
//        final Product product = new Product();
//        mapToEntity(productDTO, product);
//        return productRepository.save(product).getId();
//    }

    public Long create(final ProductDTO productDTO) {
        final Product product = new Product();
        mapToEntity(productDTO, product);
        final Product savedProduct = productRepository.save(product);

        // Traiter les images si présentes
        if (productDTO.getImages() != null && !productDTO.getImages().isEmpty()) {
            for (ImageProductDTO imageDTO : productDTO.getImages()) {
                ImageProduct image = new ImageProduct();
                image.setProduct(savedProduct);
                image.setImage(imageDTO.getImage());
                image.setIsPrimary(imageDTO.getIsPrimary());
                image.setAlt(imageDTO.getAlt());
                image.setDisplayOrder(imageDTO.getDisplayOrder());
                imageProductRepository.save(image);
            }
        }

        // Traiter les spécifications si présentes
        if (productDTO.getSpecifications() != null && !productDTO.getSpecifications().isEmpty()) {
            for (ProductSpecificationDTO specDTO : productDTO.getSpecifications()) {
                ProductSpecification spec = new ProductSpecification();
                spec.setProduct(savedProduct);
                spec.setName(specDTO.getName());
                spec.setValue(specDTO.getValue());
                productSpecificationRepository.save(spec);
            }
        }

        return savedProduct.getId();
    }

//    public void update(final Long id, final ProductDTO productDTO) {
//        final Product product = productRepository.findById(id)
//                .orElseThrow(NotFoundException::new);
//        mapToEntity(productDTO, product);
//        productRepository.save(product);
//    }

    public void update(final Long id, final ProductDTO productDTO) {
        final Product product = productRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        mapToEntity(productDTO, product);
        productRepository.save(product);

        // Mise à jour des images (approche simple: supprimer et recréer)
        if (productDTO.getImages() != null) {
            // Supprimer les images existantes
            imageProductRepository.deleteByProduct(product);

            // Ajouter les nouvelles images
            for (ImageProductDTO imageDTO : productDTO.getImages()) {
                ImageProduct image = new ImageProduct();
                image.setProduct(product);
                image.setImage(imageDTO.getImage());
                image.setIsPrimary(imageDTO.getIsPrimary());
                image.setAlt(imageDTO.getAlt());
                image.setDisplayOrder(imageDTO.getDisplayOrder());
                imageProductRepository.save(image);
            }
        }

        // Mise à jour des spécifications (approche simple: supprimer et recréer)
        if (productDTO.getSpecifications() != null) {
            // Supprimer les spécifications existantes
            productSpecificationRepository.deleteByProduct(product);

            // Ajouter les nouvelles spécifications
            for (ProductSpecificationDTO specDTO : productDTO.getSpecifications()) {
                ProductSpecification spec = new ProductSpecification();
                spec.setProduct(product);
                spec.setName(specDTO.getName());
                spec.setValue(specDTO.getValue());
                productSpecificationRepository.save(spec);
            }
        }
    }

    public void delete(final Long id) {
        final Product product = productRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        orderRepository.findAllByProducts(product)
                .forEach(order -> order.getProducts().remove(product));
        productRepository.delete(product);
    }


    /**
     * Récupère les produits mis en avant (par exemple les mieux notés ou les plus récents)
     */
    public List<ProductDTO> findFeaturedProducts() {
        // Option 1: Récupérer les produits avec la meilleure note moyenne
        List<Product> featuredProducts = productRepository.findTop10ByOrderByAverageRatingDesc();

        // Option 2: Si vous n'avez pas de champ averageRating, vous pouvez utiliser les produits les plus récents
        // List<Product> featuredProducts = productRepository.findTop10ByOrderByDateCreatedDesc();

        return featuredProducts.stream()
            .map(product -> mapToDTO(product, new ProductDTO()))
            .toList();
    }

    /**
     * Récupère les produits d'une catégorie spécifique
     */
    public List<ProductDTO> findByCategory(Long categoryId) {
        Categorie categorie = categorieRepository.findById(categoryId)
            .orElseThrow(() -> new NotFoundException("Catégorie non trouvée"));

        List<Product> products = productRepository.findByCategorie(categorie);

        return products.stream()
            .map(product -> mapToDTO(product, new ProductDTO()))
            .toList();
    }

    /**
     * Récupère les produits d'un vendeur spécifique
     */
    public List<ProductDTO> findBySeller(Long sellerId) {
        User seller = userRepository.findById(sellerId)
            .orElseThrow(() -> new NotFoundException("Vendeur non trouvé"));

        List<Product> products = productRepository.findBySeller(seller);

        return products.stream()
            .map(product -> mapToDTO(product, new ProductDTO()))
            .toList();
    }

    /**
     * Recherche des produits selon plusieurs critères
     */
    public List<ProductDTO> searchProducts(
        String query,
        Long categoryId,
        Boolean isOrganic,
        Boolean isHandmade,
        Boolean isFairTrade,
        Double minPrice,
        Double maxPrice) {

        // Créer une spécification pour la recherche
        Specification<Product> spec = Specification.where(null);

        // Ajouter des critères de recherche en fonction des paramètres fournis
        if (query != null && !query.trim().isEmpty()) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.or(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("libelle")),
                        "%" + query.toLowerCase() + "%"
                    ),
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")),
                        "%" + query.toLowerCase() + "%"
                    )
                )
            );
        }

        if (categoryId != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("categorie").get("id"), categoryId)
            );
        }

        if (isOrganic != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isOrganic"), isOrganic)
            );
        }

        if (isHandmade != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isHandmade"), isHandmade)
            );
        }

        if (isFairTrade != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isFairTrade"), isFairTrade)
            );
        }

        if (minPrice != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice)
            );
        }

        if (maxPrice != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice)
            );
        }

        // Exécuter la recherche
        List<Product> products = productRepository.findAll((Sort) spec);

        return products.stream()
            .map(product -> mapToDTO(product, new ProductDTO()))
            .toList();
    }

//    private ProductDTO mapToDTO(final Product product, final ProductDTO productDTO) {
//        productDTO.setId(product.getId());
//        productDTO.setLibelle(product.getLibelle());
//        productDTO.setStock(product.getStock());
//        productDTO.setPrice(product.getPrice());
//        productDTO.setCategorie(product.getCategorie() == null ? null : product.getCategorie().getId());
//        productDTO.setSeller(product.getSeller() == null ? null : product.getSeller().getId());
//        return productDTO;
//    }

    private ProductDTO mapToDTO(final Product product, final ProductDTO productDTO) {
        productDTO.setId(product.getId());
        productDTO.setLibelle(product.getLibelle());
        productDTO.setStock(product.getStock());
        productDTO.setPrice(product.getPrice());
        productDTO.setCategorie(product.getCategorie() == null ? null : product.getCategorie().getId());
        productDTO.setSeller(product.getSeller() == null ? null : product.getSeller().getId());

        // Nouveaux champs
        productDTO.setDescription(product.getDescription());
        productDTO.setOrigin(product.getOrigin());
        productDTO.setMaterials(product.getMaterials());
        productDTO.setDimensions(product.getDimensions());
        productDTO.setWeight(product.getWeight());
        productDTO.setIsHandmade(product.getIsHandmade());
        productDTO.setIsOrganic(product.getIsOrganic());
        productDTO.setIsFairTrade(product.getIsFairTrade());
        productDTO.setAverageRating(product.getAverageRating());

        // Récupérer et mapper les images
        if (product.getImages() != null) {
            productDTO.setImages(product.getImages().stream()
                .map(image -> {
                    ImageProductDTO imageDTO = new ImageProductDTO();
                    imageDTO.setId(image.getId());
                    imageDTO.setProductId(product.getId());
                    imageDTO.setImage(image.getImage());
                    imageDTO.setIsPrimary(image.getIsPrimary());
                    imageDTO.setAlt(image.getAlt());
                    imageDTO.setDisplayOrder(image.getDisplayOrder());
                    return imageDTO;
                })
                .toList());
        }

        // Récupérer et mapper les spécifications
        if (product.getSpecifications() != null) {
            productDTO.setSpecifications(product.getSpecifications().stream()
                .map(spec -> {
                    ProductSpecificationDTO specDTO = new ProductSpecificationDTO();
                    specDTO.setId(spec.getId());
                    specDTO.setProductId(product.getId());
                    specDTO.setName(spec.getName());
                    specDTO.setValue(spec.getValue());
                    return specDTO;
                })
                .toList());
        }

        return productDTO;
    }


//    private Product mapToEntity(final ProductDTO productDTO, final Product product) {
//        product.setLibelle(productDTO.getLibelle());
//        product.setStock(productDTO.getStock());
//        product.setPrice(productDTO.getPrice());
//        final Categorie categorie = productDTO.getCategorie() == null ? null : categorieRepository.findById(productDTO.getCategorie())
//                .orElseThrow(() -> new NotFoundException("categorie not found"));
//        product.setCategorie(categorie);
//        final User seller = productDTO.getSeller() == null ? null : userRepository.findById(productDTO.getSeller())
//                .orElseThrow(() -> new NotFoundException("seller not found"));
//        product.setSeller(seller);
//        return product;
//    }

    private Product mapToEntity(final ProductDTO productDTO, final Product product) {
        product.setLibelle(productDTO.getLibelle());
        product.setStock(productDTO.getStock());
        product.setPrice(productDTO.getPrice());

        // Nouveaux champs
        product.setDescription(productDTO.getDescription());
        product.setOrigin(productDTO.getOrigin());
        product.setMaterials(productDTO.getMaterials());
        product.setDimensions(productDTO.getDimensions());
        product.setWeight(productDTO.getWeight());
        product.setIsHandmade(productDTO.getIsHandmade());
        product.setIsOrganic(productDTO.getIsOrganic());
        product.setIsFairTrade(productDTO.getIsFairTrade());

        final Categorie categorie = productDTO.getCategorie() == null ? null : categorieRepository.findById(productDTO.getCategorie())
            .orElseThrow(() -> new NotFoundException("categorie not found"));
        product.setCategorie(categorie);

        final User seller = productDTO.getSeller() == null ? null : userRepository.findById(productDTO.getSeller())
            .orElseThrow(() -> new NotFoundException("seller not found"));
        product.setSeller(seller);

        return product;
    }

}
