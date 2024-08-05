package com._pi.benepick.domain.auth.dto;

import com._pi.benepick.domain.members.entity.Members;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthResponse {
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class AuthLoginResponseDTO{
        private String userID;
        private String role;

        public static AuthLoginResponseDTO from(Members members){
            return AuthLoginResponseDTO.builder()
                    .userID(members.getId())
                    .role(members.getRole().toString())
                    .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class AuthLogoutResponseDTO{
        private String accessToken;
    }

}
