package com.netradev.tout_est_africain.rest;

import com.netradev.tout_est_africain.model.BadgeDTO;
import com.netradev.tout_est_africain.model.UserBadgeDTO;
import com.netradev.tout_est_africain.service.BadgeService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/badges", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class BadgeResource {

    private final BadgeService badgeService;

    public BadgeResource(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    @GetMapping
    public ResponseEntity<List<BadgeDTO>> getAllBadges() {
        return ResponseEntity.ok(badgeService.findAllBadges());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BadgeDTO> getBadge(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(badgeService.getBadge(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createBadge(@RequestBody @Valid final BadgeDTO badgeDTO) {
        final Long createdId = badgeService.createBadge(badgeDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateBadge(@PathVariable(name = "id") final Long id,
                                            @RequestBody @Valid final BadgeDTO badgeDTO) {
        badgeService.updateBadge(id, badgeDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteBadge(@PathVariable(name = "id") final Long id) {
        badgeService.deleteBadge(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserBadgeDTO>> getUserBadges(@PathVariable(name = "userId") final Long userId) {
        return ResponseEntity.ok(badgeService.getUserBadges(userId));
    }

    @PostMapping("/award/{userId}/{badgeCode}")
    public ResponseEntity<Void> awardBadge(
        @PathVariable(name = "userId") final Long userId,
        @PathVariable(name = "badgeCode") final String badgeCode) {
        badgeService.awardBadge(userId, badgeCode);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove/{userId}/{badgeId}")
    public ResponseEntity<Void> removeBadge(
        @PathVariable(name = "userId") final Long userId,
        @PathVariable(name = "badgeId") final Long badgeId) {
        badgeService.removeBadge(userId, badgeId);
        return ResponseEntity.ok().build();
    }
}
