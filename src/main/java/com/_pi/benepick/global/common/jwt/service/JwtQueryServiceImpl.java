package com._pi.benepick.global.common.jwt.service;

import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.jwt.dto.JwtResponse.JwtPairDTO;
import com._pi.benepick.global.common.jwt.repository.JwtTokensRepository;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtQueryServiceImpl implements JwtQueryService{
    private final JwtTokensRepository jwtTokensRepository;
    @Override
    public JwtPairDTO findJwtPair(String accessToken) {
        return jwtTokensRepository.findById(accessToken)
                .map(JwtPairDTO::from)
            .orElseThrow(() -> new ApiException(ErrorStatus._INVALID_TOKEN));
    }
}
