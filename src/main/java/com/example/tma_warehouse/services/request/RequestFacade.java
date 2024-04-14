package com.example.tma_warehouse.services.request;

import com.example.tma_warehouse.models.item.Item;
import com.example.tma_warehouse.models.item.dtos.ItemInputDTO;
import com.example.tma_warehouse.models.item.dtos.ItemResponseDTO;
import com.example.tma_warehouse.models.request.Request;
import com.example.tma_warehouse.models.request.dtos.RequestInputDTO;
import com.example.tma_warehouse.models.request.dtos.RequestResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.example.tma_warehouse.models.item.dtos.ItemMapper.mapToItemResponseDTO;
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



}
