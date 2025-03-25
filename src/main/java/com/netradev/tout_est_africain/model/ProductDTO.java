package com.netradev.tout_est_africain.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
public class ProductDTO {

    private Long id;

    @Size(max = 255)
    private String libelle;

    private Integer stock;

    private Long categorie;

    private Long seller;

    @NotNull
    private BigDecimal price;

    // Nouveaux champs
    private String description;

    private String origin;

    private String materials;

    private String dimensions;

    private Double weight;

    private Boolean isHandmade;

    private Boolean isOrganic;

    private Boolean isFairTrade;

    private Double averageRating;

    // Liste des images associées
    private List<ImageProductDTO> images;

    // Liste des spécifications
    private List<ProductSpecificationDTO> specifications;

}
