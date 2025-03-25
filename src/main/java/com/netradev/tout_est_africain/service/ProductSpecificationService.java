package com.netradev.tout_est_africain.service;

import com.netradev.tout_est_africain.domain.Product;
import com.netradev.tout_est_africain.domain.ProductSpecification;
import com.netradev.tout_est_africain.model.ProductSpecificationDTO;
import com.netradev.tout_est_africain.repos.ProductRepository;
import com.netradev.tout_est_africain.repos.ProductSpecificationRepository;
import com.netradev.tout_est_africain.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSpecificationService {

    private final ProductSpecificationRepository productSpecificationRepository;
    private final ProductRepository productRepository;

    public ProductSpecificationService(
        final ProductSpecificationRepository productSpecificationRepository,
        final ProductRepository productRepository) {
        this.productSpecificationRepository = productSpecificationRepository;
        this.productRepository = productRepository;
    }

    public List<ProductSpecificationDTO> findAll() {
        final List<ProductSpecification> specifications = productSpecificationRepository.findAll(Sort.by("id"));
        return specifications.stream()
            .map(spec -> mapToDTO(spec, new ProductSpecificationDTO()))
            .toList();
    }

    public List<ProductSpecificationDTO> findByProductId(final Long productId) {
        final List<ProductSpecification> specifications = productSpecificationRepository.findByProductId(productId);
        return specifications.stream()
            .map(spec -> mapToDTO(spec, new ProductSpecificationDTO()))
            .toList();
    }

    public ProductSpecificationDTO get(final Long id) {
        return productSpecificationRepository.findById(id)
            .map(spec -> mapToDTO(spec, new ProductSpecificationDTO()))
            .orElseThrow(NotFoundException::new);
    }

    public Long create(final ProductSpecificationDTO specificationDTO) {
        final ProductSpecification specification = new ProductSpecification();
        mapToEntity(specificationDTO, specification);
        return productSpecificationRepository.save(specification).getId();
    }

    public void update(final Long id, final ProductSpecificationDTO specificationDTO) {
        final ProductSpecification specification = productSpecificationRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        mapToEntity(specificationDTO, specification);
        productSpecificationRepository.save(specification);
    }

    public void delete(final Long id) {
        productSpecificationRepository.deleteById(id);
    }

    private ProductSpecificationDTO mapToDTO(final ProductSpecification specification,
                                             final ProductSpecificationDTO specificationDTO) {
        specificationDTO.setId(specification.getId());
        specificationDTO.setProductId(specification.getProduct() != null ? specification.getProduct().getId() : null);
        specificationDTO.setName(specification.getName());
        specificationDTO.setValue(specification.getValue());
        return specificationDTO;
    }

    private ProductSpecification mapToEntity(final ProductSpecificationDTO specificationDTO,
                                             final ProductSpecification specification) {
        if (specificationDTO.getProductId() != null) {
            final Product product = productRepository.findById(specificationDTO.getProductId())
                .orElseThrow(() -> new NotFoundException("product not found"));
            specification.setProduct(product);
        }
        specification.setName(specificationDTO.getName());
        specification.setValue(specificationDTO.getValue());
        return specification;
    }
}
