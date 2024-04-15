package com.example.tma_warehouse.controllers.common;

import com.example.tma_warehouse.models.request.dtos.RequestResponseDTO;
import com.example.tma_warehouse.services.request.RequestFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common/request")
@RequiredArgsConstructor
@Tag(name = "Common Request Controller", description = "Controller for common reques management")
public class RequestController {
    private final RequestFacade requestFacade;

    @Operation(summary = "Get a Request by ID", description = "Retrieves request details by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RequestResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Request not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content)
    })
    @GetMapping("/{requestId}")
    public ResponseEntity<RequestResponseDTO> getRequestById(@PathVariable Long requestId) {
        RequestResponseDTO requestResponseDTO = requestFacade.getRequestById(requestId);
        return ResponseEntity.ok(requestResponseDTO);
    }
}
