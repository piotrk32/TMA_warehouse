package com.example.tma_warehouse.controllers.coordinator;

import com.example.tma_warehouse.models.item.dtos.ItemInputDTO;
import com.example.tma_warehouse.models.item.dtos.ItemResponseDTO;
import com.example.tma_warehouse.services.item.ItemFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coordinator/items")
@RequiredArgsConstructor
@Tag(name = "Coorinator  Item Controller", description = "Controller for item management by coordinators")
public class CoordinatorItemController {

    private final ItemFacade itemFacade;

    @Operation(summary = "Create new item", description = "Creates a new item from the provided payload")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successful creation of item",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItemResponseDTO.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - returns map of errors",
                    content = @Content(
                            mediaType = "application/json"
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Coordinator not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    ))
    })
    @PostMapping("")
    public ResponseEntity<ItemResponseDTO> createItem(@RequestBody ItemInputDTO itemInputDTO) {
        ItemResponseDTO itemResponseDTO = itemFacade.createItem(itemInputDTO);
        return new ResponseEntity<>(itemResponseDTO, HttpStatus.CREATED);
    }


}
