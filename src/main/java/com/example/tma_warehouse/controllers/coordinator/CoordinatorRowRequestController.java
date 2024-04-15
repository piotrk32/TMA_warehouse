package com.example.tma_warehouse.controllers.coordinator;

import com.example.tma_warehouse.models.RowRequest.RowRequest;
import com.example.tma_warehouse.models.RowRequest.dtos.RowRequestDTO;
import com.example.tma_warehouse.models.RowRequest.dtos.RowRequestResponseDTO;
import com.example.tma_warehouse.models.item.dtos.ItemResponseDTO;
import com.example.tma_warehouse.models.request.dtos.RequestRequestDTO;
import com.example.tma_warehouse.models.request.dtos.RequestResponseDTO;
import com.example.tma_warehouse.services.rowrequest.RowRequestFacade;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coordinator/rows")
@RequiredArgsConstructor
@Tag(name = "Coorinator  Rows Controller", description = "Controller for rows management by coordinators")
public class CoordinatorRowRequestController {

    private final RowRequestFacade rowRequestFacade;

    @Operation(summary = "Show all rows", description = "Functionality lets user to show all available rows")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful rows acquisition",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = RowRequestResponseDTO.class)
                            )
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - temporally returns map of errors or ErrorMessage",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    @GetMapping("/filtered")
    public ResponseEntity<Page<RowRequestResponseDTO>> getAllRows(
            @Parameter(description = "Page number of the results", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Size of the page", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "The sorting parameter of the results", example = "createdAt") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "The direction of sorting", example = "ASC") @RequestParam(defaultValue = "ASC") String sortDir,
            @Parameter(description = "Filter by Request ID", example = "1") @RequestParam(required = false) Long requestId,
            @Parameter(description = "Filter by Item ID", example = "1") @RequestParam(required = false) Long itemId
    ) {

        Page<RowRequestResponseDTO> rowRequests = rowRequestFacade.getFilteredRowRequests(page, size, sortBy, sortDir, requestId, itemId);
        return ResponseEntity.ok(rowRequests);
    }

    @Operation(summary = "Show all rows by requestId", description = "Functionality lets user to show all available rows for specific request")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful rows acquisition",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = RowRequestResponseDTO.class)
                            )
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - temporally returns map of errors or ErrorMessage",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })

    @GetMapping("/request/{requestId}")
    public ResponseEntity<Page<RowRequestResponseDTO>> getRowsForRequest(
            @Parameter(description = "ID of the request to fetch rows for", required = true)
            @RequestParam Long requestId,

            @Parameter(description = "Page number of the result set", example = "0")
            @RequestParam int page,

            @Parameter(description = "Number of records per page", example = "10")
            @RequestParam int size,

            @Parameter(description = "Column to sort by", example = "createdAt")
            @RequestParam String sortBy,

            @Parameter(description = "Direction of sort (ASC or DESC)", example = "ASC")
            @RequestParam String sortDir
    ) {

        Page<RowRequestResponseDTO> rows = rowRequestFacade.getRowsForRequest(page, size, sortBy, sortDir, requestId);
        return ResponseEntity.ok(rows);
    }
}
