package com._pi.benepick.global.common.jwt.dto;

import com._pi.benepick.global.common.jwt.entity.JwtTokens;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class JwtResponse {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class JwtPairDTO {
        private String accessToken;
        private String refreshToken;

        public static JwtPairDTO from(JwtTokens jwtTokens){
            return JwtPairDTO.builder()
                    .accessToken(jwtTokens.getAccessToken())
                    .refreshToken(jwtTokens.getRefreshToken())
                    .build();
        }
    }

}
