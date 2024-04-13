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
                path.equals("/swagger-ui.html");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
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

        DecodedJWT idTokenFromDb = JWT.decode(encodedIdToken);

        if (isTokenExpired(idTokenFromDb)) {
            Cookie newPayloadCookie = renewSession(response, email);
            payloadFromCookie = newPayloadCookie.getValue();
            idTokenFromDb = JWT.decode(userRepository.findIdTokenByEmail(email));
        }

        if (!idTokenFromDb.getPayload().equals(payloadFromCookie)) {
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
        String refreshToken = userRepository.findRefreshTokenByEmail(email);

        GoogleTokenResponse tokenResponse = authenticationService.renewTokens(refreshToken, email);

        Cookie newPayloadCookie = authenticationService.setupSession(email, tokenResponse);
        response.addCookie(newPayloadCookie);
        return newPayloadCookie;
    }

}
