package com.example.tma_warehouse.controllers.coordinator;

import com.example.tma_warehouse.exceptions.ErrorMessage;
import com.example.tma_warehouse.models.item.dtos.ItemResponseDTO;
import com.example.tma_warehouse.models.request.dtos.RequestRequestDTO;
import com.example.tma_warehouse.models.request.dtos.RequestResponseDTO;
import com.example.tma_warehouse.models.request.enums.RequestStatus;
import com.example.tma_warehouse.services.request.RequestFacade;
import io.swagger.v3.oas.annotations.Operation;
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
    public ResponseEntity<RequestResponseDTO> changeRequestStatus(@PathVariable Long requestId,
                                                                  @RequestParam RequestStatus newStatus) {
        RequestResponseDTO response = requestFacade.changeRequestStatus(requestId, newStatus);
        return ResponseEntity.ok(response);
    }

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
    public ResponseEntity<Page<RequestResponseDTO>> getRequests(
            @ModelAttribute @Valid RequestRequestDTO requestRequestDTO) {
        Page<RequestResponseDTO> requestResponseDTOPage = requestFacade.getItems(requestRequestDTO);
        return new ResponseEntity<>(requestResponseDTOPage, HttpStatus.OK);
    }
}
