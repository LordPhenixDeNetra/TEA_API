package com.netradev.tout_est_africain.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class OderDetailsDTO {

    private Long id;

    @NotNull
    private Long productId;

    @NotNull
    private Long oderId;

    // Ajouter un champ pour le prix unitaire
    @NotNull
    private BigDecimal unitPrice;

    // Pour faciliter l'affichage, on peut ajouter des champs supplémentaires
    // qui ne sont pas directement persistés
    private String productName;
    private String sellerName;
    private String buyerName;

    @NotNull
    private Long quantity;

    @NotNull
    private Long sellerId;

    private Long deliveryPersonId;

    @NotNull
    private Long buyerId;

}
