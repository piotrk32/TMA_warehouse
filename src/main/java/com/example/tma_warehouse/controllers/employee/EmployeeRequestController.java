package com.example.tma_warehouse.controllers.employee;

import com.example.tma_warehouse.exceptions.ErrorMessage;
import com.example.tma_warehouse.models.item.dtos.ItemResponseDTO;
import com.example.tma_warehouse.models.request.dtos.RequestInputDTO;
import com.example.tma_warehouse.models.request.dtos.RequestResponseDTO;
import com.example.tma_warehouse.services.request.RequestFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee/requests")
@RequiredArgsConstructor
@Tag(name = "Employee  Rquest Controller", description = "Controller for request management by employee")
public class EmployeeRequestController {

    private final RequestFacade requestFacade;

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
    public ResponseEntity<RequestResponseDTO> createRequest(@RequestBody RequestInputDTO requestInputDTO) {
        RequestResponseDTO requestResponseDTO = requestFacade.createRequest(requestInputDTO);
        return new ResponseEntity<>(requestResponseDTO, HttpStatus.CREATED);
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


}
