package com.example.tma_warehouse.services.request;

import com.example.tma_warehouse.models.request.Request;
import com.example.tma_warehouse.models.request.dtos.RequestInputDTO;
import com.example.tma_warehouse.models.request.dtos.RequestMapper;
import com.example.tma_warehouse.models.request.dtos.RequestRequestDTO;
import com.example.tma_warehouse.models.request.dtos.RequestResponseDTO;
import com.example.tma_warehouse.models.request.enums.RequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import static com.example.tma_warehouse.models.request.dtos.RequestMapper.mapToRequestResponseDTO;

@Component
@RequiredArgsConstructor
public class RequestFacade {

    private final RequestService requestService;


    public RequestResponseDTO getRequestById(Long requestId) {
        Request request = requestService.getRequestById(requestId);
        return mapToRequestResponseDTO(request);
    }

    public RequestResponseDTO createRequest(RequestInputDTO requestInputDTO) {
        Request request = requestService.createRequest(requestInputDTO);
        return mapToRequestResponseDTO(request);
    }

    public RequestResponseDTO updateRequestById(Long requestId, RequestInputDTO requestInputDTO) {
        Request updatedRequest = requestService.updateRequestById(requestInputDTO, requestId);
        return mapToRequestResponseDTO(updatedRequest);
    }

    public void deleteRequestById(Long requestId) {
        requestService.deleteRequest(requestId);
    }

    public RequestResponseDTO changeRequestStatus(Long requestId, RequestStatus newStatus) {
        Request request = requestService.changeRequestStatus(requestId, newStatus);
        return mapToRequestResponseDTO(request);  // Convert the updated Request entity to a DTO
    }

    public Page<RequestResponseDTO> getRequests(RequestRequestDTO requestRequestDTO) {
        return requestService.getRequests(requestRequestDTO).map(RequestMapper::mapToRequestResponseDTO);
    }







}
