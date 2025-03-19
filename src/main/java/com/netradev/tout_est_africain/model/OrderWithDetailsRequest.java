package com.netradev.tout_est_africain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderWithDetailsRequest {
    private OrderDTO order;
    private List<OderDetailsDTO> details;
}
