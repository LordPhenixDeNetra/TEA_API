package com.netradev.tout_est_africain.rest;

import com.netradev.tout_est_africain.model.OderDetailsDTO;
import com.netradev.tout_est_africain.service.OderDetailsService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/oderDetailss", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class OderDetailsResource {

    private final OderDetailsService oderDetailsService;

    public OderDetailsResource(final OderDetailsService oderDetailsService) {
        this.oderDetailsService = oderDetailsService;
    }

    @GetMapping
    public ResponseEntity<List<OderDetailsDTO>> getAllOderDetailss() {
        return ResponseEntity.ok(oderDetailsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OderDetailsDTO> getOderDetails(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(oderDetailsService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createOderDetails(
            @RequestBody @Valid final OderDetailsDTO oderDetailsDTO) {
        final Long createdId = oderDetailsService.create(oderDetailsDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateOderDetails(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final OderDetailsDTO oderDetailsDTO) {
        oderDetailsService.update(id, oderDetailsDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteOderDetails(@PathVariable(name = "id") final Long id) {
        oderDetailsService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
