package com.netradev.tout_est_africain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBadgeDTO {
    private Long id;
    private Long userId;
    private String userName; // Prénom + nom de l'utilisateur
    private Long badgeId;
    private String badgeName;
    private String badgeDescription;
    private String badgeIconUrl;
}
