package com.example.tma_warehouse.security.utils;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.tma_warehouse.exceptions.GoogleTokenException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.util.Base64;
import java.util.Date;

public class JwtUtils {

    public static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public static String extractHeader(String idToken) {
        return splitByDots(idToken)[0];
    }

    public static String extractPayload(String idToken) {
        return splitByDots(idToken)[1];
    }

    public static String extractSignature(String idToken) {
        return splitByDots(idToken)[2];
    }

    private static String[] splitByDots(String token) {
        return token.split("\\.");
    }

    public static String extractEmailFromPayload(String payload) {
        try {
            String decodedPayload = new String(Base64.getDecoder().decode(payload));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(decodedPayload);
            return node.get("email").asText();
        } catch (Exception exception) {
            throw new GoogleTokenException("JWT - Id Token", "Failed to decode JWT payload.");
        }
    }

    public static boolean isTokenExpired(DecodedJWT idTokenFromDb) {
        return idTokenFromDb.getExpiresAt().before(new Date());
    }
}
