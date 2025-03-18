package com.netradev.tout_est_africain.rest;

import com.netradev.tout_est_africain.domain.Categorie;
import com.netradev.tout_est_africain.domain.User;
import com.netradev.tout_est_africain.model.ProductDTO;
import com.netradev.tout_est_africain.repos.CategorieRepository;
import com.netradev.tout_est_africain.repos.UserRepository;
import com.netradev.tout_est_africain.service.ProductService;
import com.netradev.tout_est_africain.util.CustomCollectors;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/products", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class ProductResource {

    private final ProductService productService;
    private final CategorieRepository categorieRepository;
    private final UserRepository userRepository;

    public ProductResource(final ProductService productService,
            final CategorieRepository categorieRepository, final UserRepository userRepository) {
        this.productService = productService;
        this.categorieRepository = categorieRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(productService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createProduct(@RequestBody @Valid final ProductDTO productDTO) {
        final Long createdId = productService.create(productDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateProduct(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ProductDTO productDTO) {
        productService.update(id, productDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") final Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categorieValues")
    public ResponseEntity<Map<Long, Long>> getCategorieValues() {
        return ResponseEntity.ok(categorieRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Categorie::getId, Categorie::getId)));
    }

    @GetMapping("/sellerValues")
    public ResponseEntity<Map<Long, String>> getSellerValues() {
        return ResponseEntity.ok(userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getUuid)));
    }

}
