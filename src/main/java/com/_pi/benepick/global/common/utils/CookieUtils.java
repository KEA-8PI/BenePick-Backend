package com._pi.benepick.global.common.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {

    public static Cookie getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    // 쿠키 값 읽기 메서드
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    // 쿠키 삭제 메서드
    public static void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static Cookie createCookie(String name, String value, int maxAge, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        cookie.setSecure(true);
//        cookie.setAttribute("SameSite", "None"); //프론트 개발용. 배포시 제거
        return cookie;
    }

    public static ResponseCookie createLocalhostCookie(String name, String value, int maxAge, String path) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
            .path(path)
            .maxAge(maxAge)
            .httpOnly(true)
            .secure(true) //프론트 개발용. 배포시 제거
            .domain("benepick.kro.kr") //프론트 개발용. 배포시 제거
//            .sameSite("None") //프론트 개발용. 배포시 제거
            .build();

        return cookie;
    }
}
