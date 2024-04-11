package com.example.tma_warehouse.repositories;

import com.example.tma_warehouse.models.request.Request;
import com.example.tma_warehouse.models.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
