package com.netradev.tout_est_africain.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ImageProductDTO {

    private Long id;

    private Long productId;

    @Size(max = 255)
    private String image;

    // Nouveaux champs
    private Boolean isPrimary;

    private String alt;

    private Integer displayOrder;

}
