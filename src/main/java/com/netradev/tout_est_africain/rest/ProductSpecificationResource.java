package com.netradev.tout_est_africain.rest;

import com.netradev.tout_est_africain.model.ProductSpecificationDTO;
import com.netradev.tout_est_africain.service.ProductSpecificationService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/productSpecifications", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class ProductSpecificationResource {

    private final ProductSpecificationService productSpecificationService;

    public ProductSpecificationResource(final ProductSpecificationService productSpecificationService) {
        this.productSpecificationService = productSpecificationService;
    }

    @GetMapping
    public ResponseEntity<List<ProductSpecificationDTO>> getAllProductSpecifications() {
        return ResponseEntity.ok(productSpecificationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductSpecificationDTO> getProductSpecification(
        @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(productSpecificationService.get(id));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductSpecificationDTO>> getProductSpecifications(
        @PathVariable(name = "productId") final Long productId) {
        return ResponseEntity.ok(productSpecificationService.findByProductId(productId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createProductSpecification(
        @RequestBody @Valid final ProductSpecificationDTO specificationDTO) {
        final Long createdId = productSpecificationService.create(specificationDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateProductSpecification(@PathVariable(name = "id") final Long id,
                                                           @RequestBody @Valid final ProductSpecificationDTO specificationDTO) {
        productSpecificationService.update(id, specificationDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteProductSpecification(@PathVariable(name = "id") final Long id) {
        productSpecificationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
