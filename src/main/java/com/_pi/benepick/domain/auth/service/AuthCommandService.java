package com._pi.benepick.domain.auth.service;

import com._pi.benepick.domain.auth.dto.AuthRequest.AuthLoginRequestDTO;
import com._pi.benepick.domain.auth.dto.AuthResponse.AuthLoginResponseDTO;
import com._pi.benepick.domain.auth.dto.AuthResponse.AuthLogoutResponseDTO;
import com._pi.benepick.domain.members.entity.Members;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthCommandService {
    AuthLoginResponseDTO login(AuthLoginRequestDTO requestDTO, HttpServletResponse response);
    AuthLogoutResponseDTO logout(Members members, HttpServletRequest request, HttpServletResponse response);

}
