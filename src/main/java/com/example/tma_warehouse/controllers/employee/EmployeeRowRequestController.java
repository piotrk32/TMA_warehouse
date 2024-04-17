package com.example.tma_warehouse.controllers.employee;

import com.example.tma_warehouse.exceptions.ErrorMessage;
import com.example.tma_warehouse.models.RowRequest.dtos.RowRequestInputDTO;
import com.example.tma_warehouse.models.RowRequest.dtos.RowRequestResponseDTO;
import com.example.tma_warehouse.security.services.FineGrainServices;
import com.example.tma_warehouse.services.rowrequest.RowRequestFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee/rows")
@RequiredArgsConstructor
@Tag(name = "Employee  Row Controller", description = "Controller for rows management by employee")
public class EmployeeRowRequestController {

    private final RowRequestFacade rowRequestFacade;
    private final FineGrainServices fineGrainServices;

    @Operation(summary = "Get a row by ID", description = "Retrieves a row request details by its ID.")
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

    @Operation(
            summary = "Remove an item from a request",
            description = "Removes an item associated with a specific RowRequest ID from an existing request. This action cannot be performed if the request is already approved.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Item successfully removed from the request",
                            content = @Content(schema = @Schema(implementation = ResponseEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - if either the request or the row request does not exist, or if the row request does not belong to the given request",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden - if the request is already approved",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/{requestId}/{rowRequestId}")
    public ResponseEntity<?> removeItemFromRequest(
            @Parameter(description = "ID of the request from which to remove the item", required = true)
            @PathVariable Long requestId,

            @Parameter(description = "ID of the RowRequest that identifies the item to be removed", required = true)
            @PathVariable Long rowRequestId) {

        rowRequestFacade.removeItemFromRequest(requestId, rowRequestId);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Update existing row", description = "Updates a request based on the provided ID and payload")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful update of request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RowRequestResponseDTO.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - returns map of errors",
                    content = @Content(
                            mediaType = "application/json"
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Request not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    ))
    })
    @PatchMapping("/{rowRequestId}")
    public ResponseEntity<RowRequestResponseDTO> updateRowRequestById(
            @PathVariable Long rowRequestId,
            @RequestBody RowRequestInputDTO inputDTO,
            Authentication authentication) {

        Long employeeId = fineGrainServices.getEmployeeIdFromAuthentication(authentication);
        RowRequestResponseDTO updatedRowRequest = rowRequestFacade.updateRowRequestById(rowRequestId, inputDTO, employeeId);

        return ResponseEntity.ok(updatedRowRequest);
    }


}
