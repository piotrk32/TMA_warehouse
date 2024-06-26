package com.example.tma_warehouse.services.rowrequest;

import com.example.tma_warehouse.models.RowRequest.RowRequest;
import com.example.tma_warehouse.models.RowRequest.dtos.RowRequestDTO;
import com.example.tma_warehouse.models.RowRequest.dtos.RowRequestInputDTO;
import com.example.tma_warehouse.models.RowRequest.dtos.RowRequestMapper;
import com.example.tma_warehouse.models.RowRequest.dtos.RowRequestResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import static com.example.tma_warehouse.models.RowRequest.dtos.RowRequestMapper.mapToRowRequestResponseDTO;

@Component
@RequiredArgsConstructor
public class RowRequestFacade {
    private final RowRequestService rowRequestService;

    public RowRequestResponseDTO getRowRequestById(Long rowRequestId) {
        RowRequest rowRequest = rowRequestService.getRowRequestById(rowRequestId);
        return mapToRowRequestResponseDTO(rowRequest);
    }


    public RowRequestResponseDTO addItemToRequest(Long requestId, RowRequestInputDTO rowRequestInputDTO) {
        RowRequest rowRequest = rowRequestService.addItemToRequest(requestId, rowRequestInputDTO);

        return mapToRowRequestResponseDTO(rowRequest);
    }
    public Page<RowRequestResponseDTO> getRows(RowRequestDTO rowRequestDTO) {
        return rowRequestService.getRowsRequest(rowRequestDTO).map(RowRequestMapper::mapToRowRequestResponseDTO);
    }

    public Page<RowRequestResponseDTO> getFilteredRowRequests(int page, int size, String sortBy, String sortDir, Long requestId, Long itemId) {
        return rowRequestService.getAllRows(page, size, sortBy, sortDir, requestId, itemId).map(RowRequestMapper::mapToRowRequestResponseDTO);
    }

    public Page<RowRequestResponseDTO> getRowsForRequest(int page, int size, String sortBy, String sortDir, Long requestId) {
        Page<RowRequest> rowRequests = rowRequestService.getRowsForRequest(page, size, sortBy, sortDir, requestId);

        return rowRequests.map(RowRequestMapper::mapToRowRequestResponseDTO);
    }

    public void removeItemFromRequest(Long requestId, Long rowRequestId) {
        rowRequestService.removeItemFromRequest(requestId, rowRequestId);
    }

    public RowRequestResponseDTO updateRowRequestById(Long rowRequestId, RowRequestInputDTO inputDTO, Long employeeId) {
        RowRequest updatedRowRequest = rowRequestService.updateRowRequestById(rowRequestId, inputDTO, employeeId);
        return mapToRowRequestResponseDTO(updatedRowRequest);
    }

}
