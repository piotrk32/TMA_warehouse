package com.example.tma_warehouse.repositories;

import com.example.tma_warehouse.models.RowRequest.RowRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RowRequestRepository extends JpaRepository<RowRequest, Long>, JpaSpecificationExecutor<RowRequest> {

    Page<RowRequest> findByRequestId(Long requestId, Pageable pageable);

}
