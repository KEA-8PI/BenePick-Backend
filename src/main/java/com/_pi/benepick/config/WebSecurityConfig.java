package com._pi.benepick.config;

import com._pi.benepick.domain.members.entity.Role;
import com._pi.benepick.global.common.jwt.JwtAuthFilter;
import com._pi.benepick.global.common.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    private static final String[] SWAGGER_PERMIT_URL_ARRAY = {
        /* swagger v2 */
        "/v2/api-docs",
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui.html",
        "/webjars/**",
        "/api-docs/**",
        /* swagger v3 */
        "/v3/api-docs/**",
        "/swagger-ui/**",
    };
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) //csrf 비활성화
                .cors(AbstractHttpConfigurer::disable)
                // 세션 사용 안함
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 회원가입, 로그인 관련 API는 Jwt 인증 없이 접근 가능
                .authorizeHttpRequests((requests) ->
                        requests.requestMatchers("/auth/login").permitAll()
                        //Swagger 관련 권한 설정
                        .requestMatchers(SWAGGER_PERMIT_URL_ARRAY).permitAll()
                        // USER 권한이 있어야 요청할 수 있음
                        .requestMatchers("/auth/test").hasAnyRole(Role.MEMBER.name(), Role.ADMIN.name())
                        .anyRequest().authenticated())
                // Http 요청에 대한 Jwt 유효성 선 검사
                .addFilterBefore(new JwtAuthFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
