package com.netradev.tout_est_africain.rest;

import com.netradev.tout_est_africain.model.ImageProductDTO;
import com.netradev.tout_est_africain.service.ImageProductService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.io.File;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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

    @PostMapping("/upload")
    public ResponseEntity<ImageProductDTO> uploadImage(
        @RequestParam("productId") Long productId,
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "isPrimary", defaultValue = "false") Boolean isPrimary,
        @RequestParam(value = "alt", required = false) String alt,
        @RequestParam(value = "displayOrder", required = false) Integer displayOrder) {

        try {
            // Logique pour sauvegarder le fichier
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = "uploads/products/" + fileName;

            // Créer le répertoire si nécessaire
            File dir = new File("uploads/products");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Sauvegarder le fichier
            File dest = new File(filePath);
            file.transferTo(dest);

            // Créer l'enregistrement d'image
            ImageProductDTO imageDTO = new ImageProductDTO();
            imageDTO.setProductId(productId);
            imageDTO.setImage(filePath);
            imageDTO.setIsPrimary(isPrimary);
            imageDTO.setAlt(alt);
            imageDTO.setDisplayOrder(displayOrder);

            Long imageId = imageProductService.create(imageDTO);
            imageDTO.setId(imageId);

            return ResponseEntity.ok(imageDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
