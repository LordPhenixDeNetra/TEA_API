package com.netradev.tout_est_africain.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CategorieDTO {

    private Long id;

    @Size(max = 255)
    private String categorieName;

}
