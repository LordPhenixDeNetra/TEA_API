package com.netradev.tout_est_africain.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoleDTO {

    private Long id;

    @RoleRoleNameUnique
    private RoleType roleName;

}
