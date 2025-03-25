package com.netradev.tout_est_africain.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSpecificationDTO {

    private Long id;

    private Long productId;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(max = 255)
    private String value;
}
