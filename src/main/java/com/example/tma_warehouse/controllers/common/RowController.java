package com.example.tma_warehouse.controllers.common;

import com.example.tma_warehouse.models.RowRequest.dtos.RowRequestResponseDTO;
import com.example.tma_warehouse.services.rowrequest.RowRequestFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common/row")
@RequiredArgsConstructor
@Tag(name = "Common Row Controller", description = "Controller for common row management")
public class RowController {
    private final RowRequestFacade rowRequestFacade;

    @Operation(
            summary = "Get a RowRequest by ID",
            description = "Retrieves detailed information about a RowRequest by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the row request",
                            content = @Content(
                                    schema = @Schema(implementation = RowRequestResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "RowRequest not found"
                    )
            }
    )
    @GetMapping("/{rowRequestId}")
    public ResponseEntity<RowRequestResponseDTO> getRowRequestById(
            @Parameter(description = "ID of the RowRequest to retrieve", required = true)
            @PathVariable Long rowRequestId) {

        RowRequestResponseDTO rowRequest = rowRequestFacade.getRowRequestById(rowRequestId);
        return ResponseEntity.ok(rowRequest);
    }
}
