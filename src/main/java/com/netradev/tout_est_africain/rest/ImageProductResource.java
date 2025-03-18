package com.netradev.tout_est_africain.rest;

import com.netradev.tout_est_africain.model.ImageProductDTO;
import com.netradev.tout_est_africain.service.ImageProductService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/imageProducts", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class ImageProductResource {

    private final ImageProductService imageProductService;

    public ImageProductResource(final ImageProductService imageProductService) {
        this.imageProductService = imageProductService;
    }

    @GetMapping
    public ResponseEntity<List<ImageProductDTO>> getAllImageProducts() {
        return ResponseEntity.ok(imageProductService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageProductDTO> getImageProduct(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(imageProductService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createImageProduct(
            @RequestBody @Valid final ImageProductDTO imageProductDTO) {
        final Long createdId = imageProductService.create(imageProductDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateImageProduct(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ImageProductDTO imageProductDTO) {
        imageProductService.update(id, imageProductDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteImageProduct(@PathVariable(name = "id") final Long id) {
        imageProductService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
