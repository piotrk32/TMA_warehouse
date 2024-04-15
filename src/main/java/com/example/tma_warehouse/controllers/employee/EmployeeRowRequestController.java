package com.example.tma_warehouse.controllers.employee;

import com.example.tma_warehouse.models.RowRequest.dtos.RowRequestResponseDTO;
import com.example.tma_warehouse.services.rowrequest.RowRequestFacade;
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
@RequestMapping("/employee/rows")
@RequiredArgsConstructor
@Tag(name = "Employee  Row Controller", description = "Controller for rows management by employee")
public class EmployeeRowRequestController {

    private final RowRequestFacade rowRequestFacade;

    @Operation(summary = "Get a RowRequest by ID", description = "Retrieves a row request details by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the row request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RowRequestResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "RowRequest not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content)
    })
    @GetMapping("/{rowRequestId}")
    public ResponseEntity<RowRequestResponseDTO> getRowRequestById(@PathVariable Long rowRequestId) {
        RowRequestResponseDTO rowRequestResponseDTO = rowRequestFacade.getRowRequestById(rowRequestId);
        return ResponseEntity.ok(rowRequestResponseDTO);
    }



}
