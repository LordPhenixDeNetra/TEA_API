package com.netradev.tout_est_africain.repos;

import com.netradev.tout_est_africain.domain.Role;
import com.netradev.tout_est_africain.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByRoleName(RoleType roleName);

}
