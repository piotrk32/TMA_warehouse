package com.example.tma_warehouse.repositories;

import com.example.tma_warehouse.models.request.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long>, JpaSpecificationExecutor<Request> {

    List<Request> findAllByEmployeeId(Long employeeId);
}
