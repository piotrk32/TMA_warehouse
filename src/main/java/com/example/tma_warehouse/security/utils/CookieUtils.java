package com.example.tma_warehouse.security.utils;

import com.example.tma_warehouse.exceptions.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieUtils {

    public static final String PAYLOAD_COOKIE_NAME = "PAYLOAD";
    public static final String COOKIE_DEFAULT_PATH = "/";
    public static final Integer COOKIE_MAX_AGE_1_DAY = 24 * 60 * 60;

    public static String formatCookieHeader(Cookie cookie) {
        return cookie.getName() + "=" + cookie.getValue() + "; Path=" + cookie.getPath() + "; Max-Age=" + cookie.getMaxAge();
    }

    public static Cookie createCookie(String name, String value, Integer maxAge, String path) {
        Cookie cookie = new Cookie(name, value);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true); // for HTTPS
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        return cookie;
    }

    public static Cookie extractCookieFromCookies(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        throw new EntityNotFoundException("Cookie", "No cookie found with name: " + name);
    }
}
