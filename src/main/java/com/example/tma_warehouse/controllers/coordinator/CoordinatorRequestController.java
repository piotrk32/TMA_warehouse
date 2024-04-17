package com.example.tma_warehouse.controllers.coordinator;

import com.example.tma_warehouse.exceptions.ErrorMessage;
import com.example.tma_warehouse.models.item.dtos.ItemResponseDTO;
import com.example.tma_warehouse.models.request.dtos.RequestRequestDTO;
import com.example.tma_warehouse.models.request.dtos.RequestResponseDTO;
import com.example.tma_warehouse.models.request.enums.RequestStatus;
import com.example.tma_warehouse.services.request.RequestFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coordinator/requests")
@RequiredArgsConstructor
@Tag(name = "Coorinator  Request Controller", description = "Controller for request management by coordinators")
public class CoordinatorRequestController {

    private final RequestFacade requestFacade;

    @Operation(summary = "Change request status", description = "Changes request status")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful status change",
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
    @PatchMapping("/{requestId}/status")
    public ResponseEntity<RequestResponseDTO> changeRequestStatus(
            @Parameter(description = "ID of the request to change status", required = true)
            @PathVariable Long requestId,

            @Parameter(description = "New status to set for the request", required = true)
            @RequestParam RequestStatus newStatus,

            @Parameter(description = "Comment to include if the request is rejected, required only if the status is REJECTED")
            @RequestParam(required = false) String comment) { // Optional comment parameter

        // Pass both newStatus and comment to the facade method
        RequestResponseDTO response = requestFacade.changeRequestStatus(requestId, newStatus, comment);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/all")
    @Operation(summary = "Show all requests", description = "Functionality lets user to show all available requests")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful request acquisition",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ItemResponseDTO.class)
                            )
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - temporally returns map of errors or ErrorMessage",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    @PreAuthorize("hasRole('ROLE_COORDINATOR')")
    public ResponseEntity<Page<RequestResponseDTO>> getRequests(
            @ModelAttribute @Valid RequestRequestDTO requestRequestDTO) {
        Page<RequestResponseDTO> requestResponseDTOPage = requestFacade.getRequests(requestRequestDTO);
        return new ResponseEntity<>(requestResponseDTOPage, HttpStatus.OK);
    }
}
