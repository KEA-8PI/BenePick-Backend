package com._pi.benepick.domain.auth.service;

import com._pi.benepick.domain.auth.dto.AuthRequest.AuthLoginRequestDTO;
import com._pi.benepick.domain.auth.dto.AuthResponse.AuthLoginResponseDTO;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.jwt.JwtTokenProvider;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthQueryServiceImpl implements AuthQueryService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MembersRepository membersRepository;

    @Override
    public AuthLoginResponseDTO login(AuthLoginRequestDTO requestDTO, HttpServletResponse response) {
        Members member = membersRepository.findById(requestDTO.getId())
                .orElseThrow(() -> new ApiException(ErrorStatus._MEMBER_NOT_FOUND));

        if (!member.getPassword().equals(requestDTO.getPassword())) {
            throw new ApiException(ErrorStatus._MEMBER_PASSWORD_NOT_MATCH);
        }

        Cookie accessTokenCookie = jwtTokenProvider.createAccessTokenCookie(jwtTokenProvider.createAccessToken(member.getId()));
        Cookie refreshTokenCookie = jwtTokenProvider.createRefreshTokenCookie(jwtTokenProvider.createRefreshToken(member.getId()));
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        return AuthLoginResponseDTO.from(member);
    }
}
