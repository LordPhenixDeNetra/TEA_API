package com.netradev.tout_est_africain.service;

import com.netradev.tout_est_africain.domain.ImageProduct;
import com.netradev.tout_est_africain.model.ImageProductDTO;
import com.netradev.tout_est_africain.repos.ImageProductRepository;
import com.netradev.tout_est_africain.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ImageProductService {

    private final ImageProductRepository imageProductRepository;

    public ImageProductService(final ImageProductRepository imageProductRepository) {
        this.imageProductRepository = imageProductRepository;
    }

    public List<ImageProductDTO> findAll() {
        final List<ImageProduct> imageProducts = imageProductRepository.findAll(Sort.by("id"));
        return imageProducts.stream()
                .map(imageProduct -> mapToDTO(imageProduct, new ImageProductDTO()))
                .toList();
    }

    public ImageProductDTO get(final Long id) {
        return imageProductRepository.findById(id)
                .map(imageProduct -> mapToDTO(imageProduct, new ImageProductDTO()))
                .orElseThrow(NotFoundException::new);
    }

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

    private ImageProductDTO mapToDTO(final ImageProduct imageProduct,
            final ImageProductDTO imageProductDTO) {
        imageProductDTO.setId(imageProduct.getId());
        imageProductDTO.setProductId(imageProduct.getProductId());
        imageProductDTO.setImage(imageProduct.getImage());
        return imageProductDTO;
    }

    private ImageProduct mapToEntity(final ImageProductDTO imageProductDTO,
            final ImageProduct imageProduct) {
        imageProduct.setProductId(imageProductDTO.getProductId());
        imageProduct.setImage(imageProductDTO.getImage());
        return imageProduct;
    }

}
