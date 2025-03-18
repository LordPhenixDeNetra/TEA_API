package com.netradev.tout_est_africain.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OderDetailsDTO {

    private Long id;

    @NotNull
    private Long productId;

    @NotNull
    private Long oderId;

    @NotNull
    private Long quantity;

    @NotNull
    private Long sellerId;

    private Long deliveryPersonId;

    @NotNull
    private Long buyerId;

}
