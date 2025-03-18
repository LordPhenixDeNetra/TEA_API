package com.netradev.tout_est_africain.rest;

import com.netradev.tout_est_africain.model.CategorieDTO;
import com.netradev.tout_est_africain.service.CategorieService;
import com.netradev.tout_est_africain.util.ReferencedException;
import com.netradev.tout_est_africain.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class CategorieResource {

    private final CategorieService categorieService;

    public CategorieResource(final CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    @GetMapping
    public ResponseEntity<List<CategorieDTO>> getAllCategories() {
        return ResponseEntity.ok(categorieService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategorieDTO> getCategorie(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(categorieService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCategorie(
            @RequestBody @Valid final CategorieDTO categorieDTO) {
        final Long createdId = categorieService.create(categorieDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCategorie(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CategorieDTO categorieDTO) {
        categorieService.update(id, categorieDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCategorie(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = categorieService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        categorieService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
