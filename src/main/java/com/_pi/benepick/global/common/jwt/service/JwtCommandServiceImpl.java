package com._pi.benepick.global.common.jwt.service;

import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.jwt.dto.JwtResponse.JwtPairDTO;
import com._pi.benepick.global.common.jwt.entity.JwtTokens;
import com._pi.benepick.global.common.jwt.repository.JwtTokensRepository;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtCommandServiceImpl implements JwtCommandService{
    private final JwtTokensRepository jwtTokensRepository;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;
    @Override
    public JwtPairDTO createJwtPair(String accessToken, String refreshToken) {
        JwtTokens jwtTokens = JwtTokens.builder()
                .id(accessToken)
                .refreshToken(refreshToken)
                .ttl(refreshTokenExpiration)
                .build();

        jwtTokensRepository.save(jwtTokens);

        return JwtPairDTO.from(jwtTokens);
    }

    @Override
    public JwtPairDTO deleteJwtPair(String accessToken) {
        JwtTokens tokens = jwtTokensRepository.findById(accessToken)
                .orElseThrow(() -> new ApiException(ErrorStatus._INVALID_TOKEN));

        jwtTokensRepository.delete(tokens);
        return JwtPairDTO.from(tokens);
    }
}
