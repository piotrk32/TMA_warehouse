package com.example.tma_warehouse.controllers.coordinator;

import com.example.tma_warehouse.exceptions.EntityNotFoundException;
import com.example.tma_warehouse.models.item.dtos.ItemInputDTO;
import com.example.tma_warehouse.models.item.dtos.ItemRequestDTO;
import com.example.tma_warehouse.models.item.dtos.ItemResponseDTO;
import com.example.tma_warehouse.services.item.ItemFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.ErrorMessage;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Update existing item", description = "Updates an item based on the provided ID and payload")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful update of item",
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
                    description = "Not Found - Item not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    ))
    })
    @PutMapping("/update/{itemId}")
    public ResponseEntity<ItemResponseDTO> updateItemById(@PathVariable Long itemId,
                                                          @RequestBody ItemInputDTO itemInputDTO) {
        ItemResponseDTO updatedItem = itemFacade.updateItemById(itemId, itemInputDTO);
        return ResponseEntity.ok(updatedItem);
    }

    @Operation(summary = "Delete existing item", description = "Deletes an item based on the provided ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Successful deletion of item"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Item not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            )
    })
    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<?> deleteItemById(@PathVariable Long itemId) {
        try {
            itemFacade.deleteItemById(itemId);
            return ResponseEntity.noContent().build(); // 204 No Content status
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new com.example.tma_warehouse.exceptions.ErrorMessage("Item Not Found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new com.example.tma_warehouse.exceptions.ErrorMessage("Internal Server Error", e.getMessage()));
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Show all items", description = "Functionality lets user to show all available items")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful offering acquisition",
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
    public ResponseEntity<Page<ItemResponseDTO>> getItems(
            @ModelAttribute @Valid ItemRequestDTO itemRequestDTO) {
        Page<ItemResponseDTO> itemResponseDTOPage = itemFacade.getItems(itemRequestDTO);
        return new ResponseEntity<>(itemResponseDTOPage, HttpStatus.OK);
    }






}
