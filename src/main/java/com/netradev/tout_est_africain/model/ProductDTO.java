package com.netradev.tout_est_africain.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductDTO {

    private Long id;

    @Size(max = 255)
    private String libelle;

    private Integer stock;

    private Long categorie;

    private Long seller;

}
