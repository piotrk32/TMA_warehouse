package com.example.tma_warehouse.repositories;

import com.example.tma_warehouse.models.role.Role;
import com.example.tma_warehouse.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
