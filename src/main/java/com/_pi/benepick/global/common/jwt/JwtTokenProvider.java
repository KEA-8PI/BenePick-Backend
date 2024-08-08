package com._pi.benepick.global.common.jwt;


import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.jwt.dto.JwtResponse.JwtPairDTO;
import com._pi.benepick.global.common.jwt.entity.JwtTokens;
import com._pi.benepick.global.common.jwt.repository.JwtTokensRepository;
import com._pi.benepick.global.common.jwt.service.JwtCommandService;
import com._pi.benepick.global.common.jwt.service.JwtQueryService;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${jwt.secretKey}") // application.properties 등에 보관한다.
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;

    private final UserDetailsService userDetailsService;
    private final JwtQueryService jwtQueryService;
    private final JwtCommandService jwtCommandService;


    // 토큰 생성
    public String createAccessToken(String userPk) {
        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
        return Jwts.builder()
            .setClaims(claims) // 정보 저장
            .setExpiration(new Date(System.currentTimeMillis() + (accessTokenExpiration))) // 토큰 유효시각 설정 (50시간)
            .signWith(SignatureAlgorithm.HS256, secretKey)  // 암호화 알고리즘과, secret 값
            .compact();
    }

    // 리프레시 토큰 생성
    public String createRefreshToken(String userPk) {
        Claims claims = Jwts.claims().setSubject(userPk);

        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(new Date(System.currentTimeMillis()+ (refreshTokenExpiration))) // 리프레시 토큰 유효시각 설정 (1주일)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    // 액세스 토큰 재발급
    public JwtPairDTO refreshAccessToken(String accessToken, String refreshToken) {
        if (validateRefreshToken(accessToken, refreshToken)) {
            String userPk = getUserPk(refreshToken);

            return jwtCommandService.createJwtPair(createAccessToken(userPk), createRefreshToken(userPk));
        }
        return null;
    }
    public void deleteRefreshToken(String accessToken) {
        jwtCommandService.deleteJwtPair(accessToken);
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // 토큰 유효성, 만료일자 확인,
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateAccessToken(String accessToken) {
        JwtPairDTO jwtTokens = jwtQueryService.findJwtPair(accessToken);

        return validateToken(jwtTokens.getAccessToken());
    }

    // 리프레시 토큰 유효성 확인
    public boolean validateRefreshToken(String accessToken, String refreshToken) {
        if (accessToken == null || refreshToken == null) {
            throw new ApiException(ErrorStatus._INVALID_TOKEN);
        }

        JwtPairDTO jwtTokens = jwtQueryService.findJwtPair(accessToken);
        if(!refreshToken.equals(jwtTokens.getRefreshToken())) {
            throw new ApiException(ErrorStatus._INVALID_TOKEN);
        }

        return validateToken(jwtTokens.getRefreshToken());
    }

    // Request의 Header에서 token 값 가져오기
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        String userId = this.getUserPk(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userId, token, userDetails.getAuthorities());
    }

    // 액세스 토큰 쿠키 생성
    public Cookie createAccessTokenCookie(String token) {
        Cookie cookie = new Cookie("accessToken", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) accessTokenExpiration);
        cookie.setPath("/");
        return cookie;
    }

    // 리프레시 토큰 쿠키 생성
    public Cookie createRefreshTokenCookie(String token) {
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) refreshTokenExpiration);
        cookie.setPath("/");
        return cookie;
    }
}
