package com._pi.benepick.domain.auth.service;

import com._pi.benepick.domain.auth.dto.AuthRequest.AuthLoginRequestDTO;
import com._pi.benepick.domain.auth.dto.AuthRequest.AuthLogoutRequestDTO;
import com._pi.benepick.domain.auth.dto.AuthResponse.AuthLoginResponseDTO;
import com._pi.benepick.domain.auth.dto.AuthResponse.AuthLogoutResponseDTO;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.jwt.JwtTokenProvider;
import com._pi.benepick.global.common.jwt.dto.JwtResponse.JwtPairDTO;
import com._pi.benepick.global.common.jwt.entity.JwtTokens;
import com._pi.benepick.global.common.jwt.repository.JwtTokensRepository;
import com._pi.benepick.global.common.jwt.service.JwtCommandService;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthCommandServiceImpl implements AuthCommandService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MembersRepository membersRepository;
    private final JwtCommandService jwtCommandService;

    @Override
    public AuthLoginResponseDTO login(AuthLoginRequestDTO requestDTO, HttpServletResponse response) {
        Members member = membersRepository.findById(requestDTO.getId())
                .orElseThrow(() -> new ApiException(ErrorStatus._MEMBERS_NOT_FOUND));

        if (!member.getPassword().equals(requestDTO.getPassword())) {
            throw new ApiException(ErrorStatus._MEMBER_PASSWORD_NOT_MATCH);
        }

        JwtPairDTO jwtTokens = jwtCommandService.createJwtPair(jwtTokenProvider.createAccessToken(
            member.getId()), jwtTokenProvider.createRefreshToken(member.getId()));

        Cookie accessTokenCookie = jwtTokenProvider.createAccessTokenCookie(jwtTokens.getAccessToken());
        Cookie refreshTokenCookie = jwtTokenProvider.createRefreshTokenCookie(jwtTokens.getRefreshToken());
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        return AuthLoginResponseDTO.from(member);
    }

    @Override
    public AuthLogoutResponseDTO logout(Members members, AuthLogoutRequestDTO requestDTO) {
        if(!members.getId().equals(jwtTokenProvider.getUserPk(requestDTO.getAccessToken()))) {
            throw new ApiException(ErrorStatus._FORBIDDEN);
        }

        jwtCommandService.deleteJwtPair(requestDTO.getAccessToken());
        return AuthLogoutResponseDTO.builder()
            .accessToken(requestDTO.getAccessToken())
            .build();
    }
}
