package com._pi.benepick.domain.auth.controller;

import com._pi.benepick.domain.auth.dto.AuthRequest.AuthLoginRequestDTO;
import com._pi.benepick.domain.auth.dto.AuthResponse.AuthLoginResponseDTO;
import com._pi.benepick.domain.auth.service.AuthQueryService;
import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.global.common.annotation.MemberObject;
import com._pi.benepick.global.common.exception.ApiException;
import com._pi.benepick.global.common.jwt.JwtTokenProvider;
import com._pi.benepick.global.common.response.ApiResponse;
import com._pi.benepick.global.common.response.code.status.ErrorStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final MembersRepository membersRepository;
    private final AuthQueryService authQueryService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 아이디와 비밀번호를 입력받아 로그인 요청을 수행합니다.")
    public ApiResponse<AuthLoginResponseDTO> login(@RequestBody AuthLoginRequestDTO requestDTO, HttpServletResponse response){
        // 로그인 서비스로직 구현 후 아래의 코드는 삭제할 예정입니다.
        // 사용자 정보 검증 후 토큰 발급
        // 발급한 토큰을 레디스와 쿠키에 등록한다.
        return ApiResponse.onSuccess(authQueryService.login(requestDTO, response));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 - Mockup API", description = "사용자 아이디를 입력받아 로그아웃 요청을 수행합니다.")
    public ApiResponse<String> logout(@RequestBody AuthLoginRequestDTO requestDTO){
        return ApiResponse.onSuccess("로그아웃 되었습니다.");
    }

    @PostMapping("/refresh")
    @Operation(summary = "엑세스 토큰 갱신 - Mockup API", description = "리프레시 토큰을 입력받아 엑세스 토큰을 갱신합니다.")
    public ApiResponse<AuthLoginResponseDTO> refresh(){
        // 로그인 서비스로직 구현 후 아래의 코드는 삭제할 예정입니다.
        // 사용자 정보 검증 후 토큰 발급
        // 발급한 토큰을 레디스와 쿠키에 등록한다.
        Members member = membersRepository.findById("030eeb2a-c0a9-4c54-8f48-12e816a01985")
            .orElseThrow(() -> new ApiException(ErrorStatus._UNAUTHORIZED));
        return ApiResponse.onSuccess(AuthLoginResponseDTO.from(member));
    }

    @GetMapping("/test")
    public ApiResponse<String> testPostRequest(@MemberObject Members members) {
        return ApiResponse.onSuccess("Get request successful from " + members.getName());
    }

}
