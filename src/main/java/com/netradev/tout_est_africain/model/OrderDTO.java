package com.netradev.tout_est_africain.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    @OrderUuidUnique
    private String uuid;

    private Long buyer;

    private List<Long> products;

}
