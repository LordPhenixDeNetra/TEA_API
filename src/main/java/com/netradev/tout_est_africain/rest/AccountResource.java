package com.netradev.tout_est_africain.rest;

import com.netradev.tout_est_africain.model.UserReputationDTO;
import com.netradev.tout_est_africain.service.AccountService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class AccountResource {

    private final AccountService accountService;

    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{userId}/reputation")
    public ResponseEntity<UserReputationDTO> getUserReputation(@PathVariable(name = "userId") final Long userId) {
        return ResponseEntity.ok(accountService.getUserReputation(userId));
    }

    @PatchMapping("/{userId}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable(name = "userId") final Long userId) {
        accountService.activateUser(userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable(name = "userId") final Long userId) {
        accountService.deactivateUser(userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}/suspend")
    public ResponseEntity<Void> suspendUser(@PathVariable(name = "userId") final Long userId) {
        accountService.suspendUser(userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}/restrict")
    public ResponseEntity<Void> restrictUser(@PathVariable(name = "userId") final Long userId) {
        accountService.restrictUser(userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}/verify")
    public ResponseEntity<Void> verifyUser(@PathVariable(name = "userId") final Long userId) {
        accountService.verifyUser(userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}/unverify")
    public ResponseEntity<Void> unverifyUser(@PathVariable(name = "userId") final Long userId) {
        accountService.unverifyUser(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/increment-sale")
    public ResponseEntity<Void> incrementSuccessfulSale(@PathVariable(name = "userId") final Long userId) {
        accountService.incrementSuccessfulSale(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/increment-purchase")
    public ResponseEntity<Void> incrementSuccessfulPurchase(@PathVariable(name = "userId") final Long userId) {
        accountService.incrementSuccessfulPurchase(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/increment-delivery")
    public ResponseEntity<Void> incrementSuccessfulDelivery(@PathVariable(name = "userId") final Long userId) {
        accountService.incrementSuccessfulDelivery(userId);
        return ResponseEntity.ok().build();
    }
}
