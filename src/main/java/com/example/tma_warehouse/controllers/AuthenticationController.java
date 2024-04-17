package com.example.tma_warehouse.controllers;

import com.example.tma_warehouse.models.administrator.dtos.AdministratorInputDTO;
import com.example.tma_warehouse.models.administrator.dtos.AdministratorResponseDTO;
import com.example.tma_warehouse.models.coordinator.dtos.CoordinatorInputDTO;
import com.example.tma_warehouse.models.coordinator.dtos.CoordinatorResponseDTO;
import com.example.tma_warehouse.models.employee.dtos.EmployeeInputDTO;
import com.example.tma_warehouse.models.employee.dtos.EmployeeResponseDTO;
import com.example.tma_warehouse.models.user.dtos.UserRoleDTO;
import com.example.tma_warehouse.security.services.AuthenticationService;
import com.example.tma_warehouse.security.services.FineGrainServices;
import com.example.tma_warehouse.services.administrator.AdministratorFacade;
import com.example.tma_warehouse.services.coordinator.CoordinatorFacade;
import com.example.tma_warehouse.services.employee.EmployeeFacade;
import com.example.tma_warehouse.services.user.UserFacade;
import com.example.tma_warehouse.services.user.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static com.example.tma_warehouse.models.user.enums.Status.REGISTRATION_INCOMPLETE;
import static com.example.tma_warehouse.security.utils.CookieUtils.formatCookieHeader;
import static com.example.tma_warehouse.security.utils.JwtUtils.JSON_FACTORY;


@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "Functionalities intended for authentication")
public class AuthenticationController {

    private final EmployeeFacade employeeFacade;
    private final CoordinatorFacade coordinatorFacade;
    private final AuthenticationService authenticationService;
    private final FineGrainServices fineGrainServices;
    private final UserFacade userFacade;
    private final AdministratorFacade administratorFacade;
    private final UserService userService;

    public final static String LOGIN_FRONTEND_REDIRECT = "http://localhost:5173";
    public final static String LOGIN_PATH = "/login/oauth2/code/google";

    @Operation(summary = "OAuth2 Google login",
            description = "Logs the user in using google " +
                    "- exchanges the authorization code for tokens. If the user logs in for the first time, " +
                    "a new account is created for him with data that needs to be filled in. " +
                    "A session is also set up - tokens are saved to the database.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully logged in",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(implementation = String.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Invalid authorization code.",
                    content = @Content(
                            mediaType = "application/json"
                    )),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or expired Google Id Token.",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    @GetMapping(path = LOGIN_PATH)
    public ResponseEntity<String> authorize(@RequestParam("code") String authorizationCode) throws IOException {
        GoogleTokenResponse tokens = authenticationService.exchangeAuthorizationCode(authorizationCode);

        GoogleIdToken idToken = GoogleIdToken.parse(JSON_FACTORY, tokens.getIdToken());
        authenticationService.verifyGoogleIdToken(idToken);
        GoogleIdToken.Payload payload = idToken.getPayload();

        Cookie sessionCookie = authenticationService.setupSession(payload.getEmail(), tokens);
        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .header(HttpHeaders.SET_COOKIE, formatCookieHeader(sessionCookie))
                .location(URI.create(LOGIN_FRONTEND_REDIRECT))
                .body("Successfully logged in");
    }

    @Operation(summary = "OAuth2 Google logout",
            description = "Logs out the user, revokes the access token, and deletes all tokens from the database.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully logged out",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(implementation = String.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Possible causes: " +
                            "token not found for the user or error while revoking the token with Google, " +
                            "user was logged out anyway.",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    @GetMapping("/revoke")
    public ResponseEntity<String> revoke() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        authenticationService.revoke(email);
        return ResponseEntity.ok().body("Logged out");
    }

    @Operation(summary = "Create employee", description = "Creates a employee from the provided payload")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successful creation of employee",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponseDTO.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - temporally returns map of errors or ErrorMessage",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    @PostMapping("/register/employee")
    public ResponseEntity<EmployeeResponseDTO> registerEmployee(@RequestBody @Valid EmployeeInputDTO employeeInputDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        EmployeeResponseDTO employee = employeeFacade.createEmployee(employeeInputDTO, email);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @Operation(summary = "Create coordinator", description = "Creates a coordinator from the provided payload")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successful creation of coordinator",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CoordinatorResponseDTO.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - temporally returns map of errors or ErrorMessage",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    @PostMapping("/register/coordinator")
    public ResponseEntity<CoordinatorResponseDTO> registerCoordinator(@RequestBody @Valid CoordinatorInputDTO coordinatorInputDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        CoordinatorResponseDTO coordinatorResponseDTO = coordinatorFacade.createCoordinator(coordinatorInputDTO, email);
        return new ResponseEntity<>(coordinatorResponseDTO, HttpStatus.CREATED);
    }


    @Operation(summary = "Create administrator", description = "Creates a administrator from the provided payload")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successful creation of administrator",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CoordinatorResponseDTO.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - temporally returns map of errors or ErrorMessage",
                    content = @Content(
                            mediaType = "application/json"
                    ))
    })
    @PostMapping("/register/administrator")
    public ResponseEntity<AdministratorResponseDTO> registerAdministrator(@RequestBody @Valid AdministratorInputDTO administratorInputDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AdministratorResponseDTO administratorResponseDTO = administratorFacade.createAdministrator(administratorInputDTO, email);
        return new ResponseEntity<>(administratorResponseDTO, HttpStatus.CREATED);
    }




    @Operation(summary = "Get user info", description = "Retrieves user id and role")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful user data retrieval"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    ))
    })
    @GetMapping("/my-data")
    public ResponseEntity<UserRoleDTO> getRole() {
        List<? extends GrantedAuthority> role = (List<? extends GrantedAuthority>)
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        Long userId = fineGrainServices.getCurrentUserId();
        if (role.isEmpty()){
            return new ResponseEntity<>(new UserRoleDTO("ROLE_"+ REGISTRATION_INCOMPLETE, null), HttpStatus.OK);
        }
        return new ResponseEntity<>(new UserRoleDTO(role.get(0).getAuthority(), userId), HttpStatus.OK);
    }

    @Operation(summary = "Delete user by id", description = "Remove user from database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "No content - status changed"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found - Employee not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    ))
    })
    @DeleteMapping("/user/delete/{id}")
    @PreAuthorize("@fineGrainServices.compareSecurityEmailAndEmailByUserId(#id)")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userFacade.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
