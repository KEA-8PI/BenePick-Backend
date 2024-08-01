package com._pi.benepick.domain.auth.service;

import com._pi.benepick.domain.auth.dto.AuthRequest.AuthLoginRequestDTO;
import com._pi.benepick.domain.auth.dto.AuthResponse.AuthLoginResponseDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthQueryService {
    AuthLoginResponseDTO login(AuthLoginRequestDTO requestDTO, HttpServletResponse response);

}
