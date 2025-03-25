package com.netradev.tout_est_africain.service;

import com.netradev.tout_est_africain.domain.ImageProduct;
import com.netradev.tout_est_africain.domain.Product;
import com.netradev.tout_est_africain.model.ImageProductDTO;
import com.netradev.tout_est_africain.repos.ImageProductRepository;
import com.netradev.tout_est_africain.repos.ProductRepository;
import com.netradev.tout_est_africain.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ImageProductService {

    private final ImageProductRepository imageProductRepository;
    private final ProductRepository productRepository;

    public ImageProductService(final ImageProductRepository imageProductRepository,
                               final ProductRepository productRepository) {
        this.imageProductRepository = imageProductRepository;
        this.productRepository = productRepository;
    }

//    public List<ImageProductDTO> findAll() {
//        final List<ImageProduct> imageProducts = imageProductRepository.findAll(Sort.by("id"));
//        return imageProducts.stream()
//                .map(imageProduct -> mapToDTO(imageProduct, new ImageProductDTO()))
//                .toList();
//    }

    public List<ImageProductDTO> findAll() {
        final List<ImageProduct> imageProducts = imageProductRepository.findAll(Sort.by("id"));
        return imageProducts.stream()
            .map(imageProduct -> mapToDTO(imageProduct, new ImageProductDTO()))
            .toList();
    }

    public List<ImageProductDTO> findByProductId(final Long productId) {
        final List<ImageProduct> imageProducts = imageProductRepository.findByProductId(productId);
        return imageProducts.stream()
            .map(imageProduct -> mapToDTO(imageProduct, new ImageProductDTO()))
            .toList();
    }

//    public ImageProductDTO get(final Long id) {
//        return imageProductRepository.findById(id)
//                .map(imageProduct -> mapToDTO(imageProduct, new ImageProductDTO()))
//                .orElseThrow(NotFoundException::new);
//    }

    public ImageProductDTO get(final Long id) {
        return imageProductRepository.findById(id)
            .map(imageProduct -> mapToDTO(imageProduct, new ImageProductDTO()))
            .orElseThrow(NotFoundException::new);
    }
//
//    public Long create(final ImageProductDTO imageProductDTO) {
//        final ImageProduct imageProduct = new ImageProduct();
//        mapToEntity(imageProductDTO, imageProduct);
//        return imageProductRepository.save(imageProduct).getId();
//    }
//
//    public void update(final Long id, final ImageProductDTO imageProductDTO) {
//        final ImageProduct imageProduct = imageProductRepository.findById(id)
//                .orElseThrow(NotFoundException::new);
//        mapToEntity(imageProductDTO, imageProduct);
//        imageProductRepository.save(imageProduct);
//    }
//
//    public void delete(final Long id) {
//        imageProductRepository.deleteById(id);
//    }
//
//

    public Long create(final ImageProductDTO imageProductDTO) {
        final ImageProduct imageProduct = new ImageProduct();
        mapToEntity(imageProductDTO, imageProduct);
        return imageProductRepository.save(imageProduct).getId();
    }

    public void update(final Long id, final ImageProductDTO imageProductDTO) {
        final ImageProduct imageProduct = imageProductRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        mapToEntity(imageProductDTO, imageProduct);
        imageProductRepository.save(imageProduct);
    }

    public void delete(final Long id) {
        imageProductRepository.deleteById(id);
    }

//    private ImageProductDTO mapToDTO(final ImageProduct imageProduct,
//            final ImageProductDTO imageProductDTO) {
//        imageProductDTO.setId(imageProduct.getId());
//        imageProductDTO.setProductId(imageProduct.getProductId());
//        imageProductDTO.setImage(imageProduct.getImage());
//        return imageProductDTO;
//    }
//
//    private ImageProduct mapToEntity(final ImageProductDTO imageProductDTO,
//            final ImageProduct imageProduct) {
//        imageProduct.setProductId(imageProductDTO.getProductId());
//        imageProduct.setImage(imageProductDTO.getImage());
//        return imageProduct;
//    }

    private ImageProductDTO mapToDTO(final ImageProduct imageProduct,
                                     final ImageProductDTO imageProductDTO) {
        imageProductDTO.setId(imageProduct.getId());
        imageProductDTO.setProductId(imageProduct.getProduct() != null ? imageProduct.getProduct().getId() : null);
        imageProductDTO.setImage(imageProduct.getImage());
        imageProductDTO.setIsPrimary(imageProduct.getIsPrimary());
        imageProductDTO.setAlt(imageProduct.getAlt());
        imageProductDTO.setDisplayOrder(imageProduct.getDisplayOrder());
        return imageProductDTO;
    }

    private ImageProduct mapToEntity(final ImageProductDTO imageProductDTO,
                                     final ImageProduct imageProduct) {
        if (imageProductDTO.getProductId() != null) {
            final Product product = productRepository.findById(imageProductDTO.getProductId())
                .orElseThrow(() -> new NotFoundException("product not found"));
            imageProduct.setProduct(product);
        }
        imageProduct.setImage(imageProductDTO.getImage());
        imageProduct.setIsPrimary(imageProductDTO.getIsPrimary());
        imageProduct.setAlt(imageProductDTO.getAlt());
        imageProduct.setDisplayOrder(imageProductDTO.getDisplayOrder());
        return imageProduct;
    }

}
