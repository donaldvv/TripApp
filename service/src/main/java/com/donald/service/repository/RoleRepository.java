package com.donald.service.repository;
import com.donald.service.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface RoleRepository extends JpaRepository<com.donald.service.model.Role, Long> {

    Optional<com.donald.service.model.Role> findByName(Role name);

}