package com.example.tma_warehouse.security.services;

import com.example.tma_warehouse.exceptions.GoogleTokenException;
import com.example.tma_warehouse.exceptions.UnauthorizedException;
import com.example.tma_warehouse.services.user.UserService;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.example.tma_warehouse.security.utils.CookieUtils.*;
import static com.example.tma_warehouse.security.utils.JwtUtils.JSON_FACTORY;
import static com.example.tma_warehouse.security.utils.JwtUtils.extractPayload;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;



    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String tokenUri;
    private final static String REVOKE_URL = "https://oauth2.googleapis.com/revoke";

    public GoogleTokenResponse exchangeAuthorizationCode(String authorizationCode) {
        try {
            return new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(), JSON_FACTORY, tokenUri,
                    clientId, clientSecret, authorizationCode, redirectUri).execute();
        } catch (IOException exception) {
            throw new GoogleTokenException("Authorization code", "Invalid authorization code.");
        }
    }

    public GoogleTokenResponse renewTokens(String refreshToken, String email) {
        try {
            return new GoogleRefreshTokenRequest(
                    new NetHttpTransport(), JSON_FACTORY, refreshToken,
                    clientId, clientSecret).execute();
        } catch (IOException exception) {
            revoke(email);
            throw new UnauthorizedException("Id Token", "User has been logged out for security reasons." +
                    " Error while refreshing Id Token.");
        }
    }

    public void verifyGoogleIdToken(GoogleIdToken googleIdToken) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JSON_FACTORY)
                .setAudience(Collections.singletonList(clientId))
                .build();
        try {
            if (!verifier.verify(googleIdToken)) {
                throw new GoogleTokenException("Google Id Token", "Invalid google id token.");
            }
        } catch (GeneralSecurityException | IOException exception) {
            throw new GoogleTokenException("Google Id Token", "Error verifying Google Id Token");
        }
    }

    public Cookie setupSession(String userEmail, GoogleTokenResponse tokens) {
        userService.setTokens(userEmail, tokens);
        return createCookie(PAYLOAD_COOKIE_NAME, extractPayload(tokens.getIdToken()),
                COOKIE_MAX_AGE_1_DAY, COOKIE_DEFAULT_PATH);
    }


    public void revoke(String email) {
        String accessToken = userService.getAccessTokenByEmail(email);
        userService.removeTokens(email);
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("token", accessToken);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForEntity(REVOKE_URL, parameters, String.class);
        } catch (Exception exception) {
            throw new GoogleTokenException("Access Token", "Error revoking Google Access Token");
        }
    }

}

