package com.netradev.tout_est_africain.repos;

import com.netradev.tout_est_africain.domain.Role;
import com.netradev.tout_est_africain.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByRoles(Role role);

    List<User> findAllByRoles(Role role);

    boolean existsByUuidIgnoreCase(String uuid);

    boolean existsByEmailIgnoreCase(String email);

}
