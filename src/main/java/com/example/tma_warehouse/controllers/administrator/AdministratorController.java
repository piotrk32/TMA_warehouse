package com.example.tma_warehouse.controllers.administrator;

import com.example.tma_warehouse.models.item.dtos.ItemResponseDTO;
import com.example.tma_warehouse.models.user.User;
import com.example.tma_warehouse.models.user.dtos.UserResponseDTO;
import com.example.tma_warehouse.models.user.dtos.UserRolesUpdateDTO;
import com.example.tma_warehouse.services.user.UserFacade;
import com.example.tma_warehouse.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/administrator")
@RequiredArgsConstructor
@Tag(name = "Administrator Controller", description = "Functionalities intended for administrators")
public class AdministratorController {

    private final UserService userService;
    private final UserFacade userFacade;
    private static final Logger log = LoggerFactory.getLogger(AdministratorController.class);

    @Operation(summary = "Change authorities", description = "Changes user's authority")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successful change authorities",
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
                    description = "Not Found ",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    ))
    })
    @PostMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<?> updateUserRoles(@PathVariable Long userId, @RequestBody UserRolesUpdateDTO rolesUpdateDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Current user: " + auth.getName() + ", Roles: " + auth.getAuthorities());

        User updatedUser = userService.updateUserRoles(userId, rolesUpdateDTO.getRoles());

        return ResponseEntity.ok("User roles updated successfully for " + updatedUser.getEmail());
    }

    @Operation(summary = "Get All Users", description = "Returns a list of all users with detailed information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "404", description = "Not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    @GetMapping("/dtos")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> userDtos = userFacade.getAllUsers();
        return ResponseEntity.ok(userDtos);
    }
}
