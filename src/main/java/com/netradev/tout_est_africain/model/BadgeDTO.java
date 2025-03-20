package com.netradev.tout_est_africain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadgeDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String iconUrl;
    private BadgeCategory category;
}
