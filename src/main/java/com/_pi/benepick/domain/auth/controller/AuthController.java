package com._pi.benepick.domain.auth.controller;

import com._pi.benepick.domain.auth.dto.AuthRequest.AuthLoginRequestDTO;
import com._pi.benepick.domain.auth.dto.AuthResponse.AuthLoginResponseDTO;
import com._pi.benepick.domain.auth.dto.AuthResponse.AuthLogoutResponseDTO;
import com._pi.benepick.domain.auth.service.AuthCommandService;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.global.common.annotation.MemberObject;
import com._pi.benepick.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "인증 API")
public class AuthController {
    private final AuthCommandService authCommandService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 아이디와 비밀번호를 입력받아 로그인 요청을 수행합니다.")
    public ApiResponse<AuthLoginResponseDTO> login(@Valid @RequestBody AuthLoginRequestDTO requestDTO, HttpServletResponse response){
        return ApiResponse.onSuccess(authCommandService.login(requestDTO, response));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "사용자 아이디를 입력받아 로그아웃 요청을 수행합니다.")
    public ApiResponse<AuthLogoutResponseDTO> logout(@Parameter(hidden = true) @MemberObject Members members, HttpServletRequest request, HttpServletResponse response){
        return ApiResponse.onSuccess(authCommandService.logout(members, request, response));
    }

    @GetMapping("/test")
    public ApiResponse<String> testPostRequest(@Parameter(hidden = true) @MemberObject Members members) {
        return ApiResponse.onSuccess("Get request successful from " + members.getName());
    }

}
