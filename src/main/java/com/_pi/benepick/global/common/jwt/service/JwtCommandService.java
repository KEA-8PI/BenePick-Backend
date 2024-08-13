package com._pi.benepick.global.common.jwt.service;

import com._pi.benepick.global.common.jwt.dto.JwtResponse.JwtPairDTO;

public interface JwtCommandService {

    JwtPairDTO createJwtPair(String accessToken, String refreshToken);
    JwtPairDTO deleteJwtPair(String accessToken);

}
