package com.example.tma_warehouse.services.rowrequest;

import com.example.tma_warehouse.models.RowRequest.RowRequest;
import com.example.tma_warehouse.models.RowRequest.dtos.RowRequestInputDTO;
import com.example.tma_warehouse.models.RowRequest.dtos.RowRequestResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.example.tma_warehouse.models.RowRequest.dtos.RowRequestMapper.mapToRowRequestResponseDTO;

@Component
@RequiredArgsConstructor
public class RowRequestFacade {
    private final RowRequestService rowRequestService;


    public RowRequestResponseDTO addItemToRequest(Long requestId, RowRequestInputDTO rowRequestInputDTO) {
        // Add the item to the request using the service layer
        RowRequest rowRequest = rowRequestService.addItemToRequest(requestId, rowRequestInputDTO);

        // Map the RowRequest entity to RowRequestResponseDTO
        return mapToRowRequestResponseDTO(rowRequest);
    }
}
