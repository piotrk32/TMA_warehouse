package com.example.tma_warehouse.security.filters;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.example.tma_warehouse.exceptions.UnauthorizedException;
import com.example.tma_warehouse.repositories.UserRepository;
import com.example.tma_warehouse.security.services.AuthenticationService;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.tma_warehouse.controllers.AuthenticationController.LOGIN_PATH;
import static com.example.tma_warehouse.security.utils.CookieUtils.PAYLOAD_COOKIE_NAME;
import static com.example.tma_warehouse.security.utils.CookieUtils.extractCookieFromCookies;

import static com.example.tma_warehouse.security.utils.JwtUtils.extractEmailFromPayload;
import static com.example.tma_warehouse.security.utils.JwtUtils.isTokenExpired;


@Component
@RequiredArgsConstructor
public class PayloadAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals(LOGIN_PATH) ||
                path.startsWith("/swagger-ui/") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/v3/api-docs") ||
                path.equals("/swagger-ui.html");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String payload = extractCookieFromCookies(request, PAYLOAD_COOKIE_NAME).getValue();
        String email = extractEmailFromPayload(payload);

        verifyPayload(response, payload, email);

        UserDetails userDetails = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private void verifyPayload(HttpServletResponse response, String payloadFromCookie, String email) {
        String encodedIdToken = userRepository.findIdTokenByEmail(email);
        ensureUserIsLoggedIn(encodedIdToken);

        // Decode the JWT to check for expiration and validate the payload
        DecodedJWT idTokenFromDb = JWT.decode(encodedIdToken);

        // Check if the token is expired and renew it if necessary
        if (isTokenExpired(idTokenFromDb)) {
            // Renew the session and replace the old cookie with a new one
            Cookie newPayloadCookie = renewSession(response, email);
            payloadFromCookie = newPayloadCookie.getValue(); // Update the local variable to reflect the new token
            idTokenFromDb = JWT.decode(userRepository.findIdTokenByEmail(email)); // Decode the new token
        }

        // Verify that the payload in the user's cookie matches the payload in the database
        // This is critical to prevent token tampering
        if (!idTokenFromDb.getPayload().equals(payloadFromCookie)) {
            // If the payloads do not match, revoke the tokens and log out the user
            authenticationService.revoke(email);
            throw new UnauthorizedException("Id Token", "User has been logged out for security reasons. " +
                    "Id Token has been tampered with.");
        }
    }

    private void ensureUserIsLoggedIn(String encodedIdToken) {
        if (encodedIdToken == null) {
            throw new UnauthorizedException("Id Token", "User is not logged in.");
        }
    }

    private Cookie renewSession(HttpServletResponse response, String email) {
        // Obtain the refresh token from the user repository
        String refreshToken = userRepository.findRefreshTokenByEmail(email);
        // Request new tokens using the refresh token
        GoogleTokenResponse tokenResponse = authenticationService.renewTokens(refreshToken, email);

        // Set up the new session and create a new cookie with the updated payload
        return authenticationService.setupSession(email, tokenResponse);
    }

    private void refreshUserAuthorities(UserDetails userDetails) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                userDetails, currentAuth.getCredentials(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

}
