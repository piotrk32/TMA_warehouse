package com.example.tma_warehouse.repositories;

import com.example.tma_warehouse.models.RowRequest.RowRequest;
import com.example.tma_warehouse.models.request.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RowRequestRepository extends JpaRepository<RowRequest, Long>, JpaSpecificationExecutor<RowRequest> {
}
