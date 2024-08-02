package com._pi.benepick.global.common.jwt;


import com._pi.benepick.global.common.jwt.dto.JwtParameters.JwtPair;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        // 클라이언트의 API 요청 헤더에서 토큰 추출
        String token = jwtTokenProvider.resolveToken(request);

        // 유효성 검사 후 SecurityContext에 저장
        if (token != null) {
            if (jwtTokenProvider.validateToken(token)) {
                //토큰이 유효함
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else if (jwtTokenProvider.validateRefreshToken(token)) {
                // 리프래시토큰으로 액세스토큰 재발급
                JwtPair newToken = jwtTokenProvider.refreshAccessToken(token);
                if (newToken != null) {
//                   // 새로운 액세스 토큰이 발급되면 SecurityContext에 저장
                    Authentication authentication = jwtTokenProvider.getAuthentication(newToken.getAccessToken());
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // 응답 쿠키에 새로운 액세스토큰 전달
                    // redis에 기존 refresh token 삭제 및 새로운 refresh token 저장
                    Cookie accessTokenCookie = jwtTokenProvider.createAccessTokenCookie(newToken.getAccessToken());
                    Cookie refreshTokenCookie = jwtTokenProvider.createRefreshTokenCookie(newToken.getRefreshToken());

                    response.addCookie(accessTokenCookie);
                    response.addCookie(refreshTokenCookie);
                }
            }
        }

        // 다음 필터링
        filterChain.doFilter(request, response);

    }
}
