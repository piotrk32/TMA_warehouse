package com.example.tma_warehouse.repositories;

import com.example.tma_warehouse.models.request.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
