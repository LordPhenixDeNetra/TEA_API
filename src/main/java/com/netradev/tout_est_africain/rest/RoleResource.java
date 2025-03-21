package com.netradev.tout_est_africain.rest;

import com.netradev.tout_est_africain.model.RoleDTO;
import com.netradev.tout_est_africain.service.RoleService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/roles", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class RoleResource {

    private final RoleService roleService;

    public RoleResource(final RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRole(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(roleService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createRole(@RequestBody @Valid final RoleDTO roleDTO) {
        final Long createdId = roleService.create(roleDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateRole(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final RoleDTO roleDTO) {
        roleService.update(id, roleDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRole(@PathVariable(name = "id") final Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
