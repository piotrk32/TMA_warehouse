package com.example.tma_warehouse.repositories;

import com.example.tma_warehouse.models.administrator.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
}
