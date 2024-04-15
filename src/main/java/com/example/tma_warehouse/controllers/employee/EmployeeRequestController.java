package com.example.tma_warehouse.controllers.employee;

import com.example.tma_warehouse.exceptions.ErrorMessage;
import com.example.tma_warehouse.models.RowRequest.dtos.RowRequestInputDTO;
import com.example.tma_warehouse.models.RowRequest.dtos.RowRequestResponseDTO;
import com.example.tma_warehouse.models.request.dtos.RequestInputDTO;
import com.example.tma_warehouse.models.request.dtos.RequestResponseDTO;
import com.example.tma_warehouse.services.request.RequestFacade;
import com.example.tma_warehouse.services.rowrequest.RowRequestFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee/requests")
@RequiredArgsConstructor
@Tag(name = "Employee  Request Controller", description = "Controller for request management by employee")
public class EmployeeRequestController {

    private final RequestFacade requestFacade;
    private final RowRequestFacade rowRequestFacade;

    @Operation(summary = "Create new request", description = "Creates a new request from the provided payload")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successful creation of request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RequestResponseDTO.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - returns map of errors",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Employee not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    ))
    })
    @PostMapping("")
    public ResponseEntity<com.example.tma_warehouse.utils.ApiResponse<RequestResponseDTO>> createRequest(@RequestBody RequestInputDTO requestInputDTO) {
        RequestResponseDTO requestResponseDTO = requestFacade.createRequest(requestInputDTO);
        com.example.tma_warehouse.utils.ApiResponse<RequestResponseDTO> response = new com.example.tma_warehouse.utils.ApiResponse<>(requestResponseDTO, "Request created");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update existing request", description = "Updates a request based on the provided ID and payload")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful update of request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RequestResponseDTO.class)
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
    @PutMapping("/{requestId}")
    public ResponseEntity<RequestResponseDTO> updateRequest(@PathVariable Long requestId,
                                                            @RequestBody RequestInputDTO requestInputDTO) {
        RequestResponseDTO updatedRequest = requestFacade.updateRequestById(requestId, requestInputDTO);
        return ResponseEntity.ok(updatedRequest);
    }

    @Operation(summary = "Add item to existing request", description = "Adds an item to an existing request based on the provided payload")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful addition of item to request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RowRequestResponseDTO.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - returns map of errors",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Request or item not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    ))
    })
    @PostMapping("/{requestId}/items")
    public ResponseEntity<com.example.tma_warehouse.utils.ApiResponse<RowRequestResponseDTO>> addItemToRequest(
            @PathVariable Long requestId,
            @Valid @RequestBody RowRequestInputDTO rowRequestInputDTO) {

        RowRequestResponseDTO rowRequestResponseDTO = rowRequestFacade.addItemToRequest(requestId, rowRequestInputDTO);
        com.example.tma_warehouse.utils.ApiResponse<RowRequestResponseDTO> response = new com.example.tma_warehouse.utils.ApiResponse<>(rowRequestResponseDTO, "Item added to request");
        return ResponseEntity.ok(response);
    }


}
