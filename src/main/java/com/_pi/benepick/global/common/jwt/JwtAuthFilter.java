package com._pi.benepick.global.common.jwt;


import com._pi.benepick.global.common.jwt.dto.JwtResponse.JwtPairDTO;
import com._pi.benepick.global.common.jwt.entity.JwtTokens;
import com._pi.benepick.global.common.utils.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        // 클라이언트의 API 요청 헤더에서 토큰 추출
        String accessToken = CookieUtils.getCookieValue(request, "accessToken");
        String refreshToken = CookieUtils.getCookieValue(request, "refreshToken");

        // 유효성 검사 후 SecurityContext에 저장
        if (accessToken != null) {
            if (jwtTokenProvider.validateAccessToken(accessToken)) {
                //토큰이 유효함
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else if(refreshToken != null){
                // 리프래시토큰으로 액세스토큰 재발급
                JwtPairDTO newToken = jwtTokenProvider.refreshAccessToken(accessToken, refreshToken);
                if (newToken != null) {
//                   // 새로운 액세스 토큰이 발급되면 SecurityContext에 저장
                    Authentication authentication = jwtTokenProvider.getAuthentication(newToken.getAccessToken());
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // 응답 쿠키에 새로운 액세스토큰 전달
                    // redis에 기존 refresh token 삭제 및 새로운 refresh token 저장
                    Cookie accessTokenCookie = jwtTokenProvider.createAccessTokenCookie(newToken.getAccessToken());
                    Cookie refreshTokenCookie = jwtTokenProvider.createRefreshTokenCookie(newToken.getRefreshToken());

                    // localhost에서도 테스트하기 위해 추가
//                    ResponseCookie localAccessTokenCookie = jwtTokenProvider.createLocalHostAccessTokenCookie(newToken.getAccessToken());
//                    ResponseCookie localRefreshTokenCookie = jwtTokenProvider.createLocalHostRefreshTokenCookie(newToken.getRefreshToken());
//                    response.addHeader("Set-Cookie", localAccessTokenCookie.toString());
//                    response.addHeader("Set-Cookie", localRefreshTokenCookie.toString());

                    response.addCookie(accessTokenCookie);
                    response.addCookie(refreshTokenCookie);
                }
            }
        }

        // 다음 필터링
        filterChain.doFilter(request, response);

    }
}
