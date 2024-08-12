package com._pi.benepick.global.common.jwt.service;

import com._pi.benepick.global.common.jwt.dto.JwtResponse.JwtPairDTO;

public interface JwtQueryService {
    JwtPairDTO findJwtPair(String accessToken);

}
