package com._pi.benepick.global.common.resolver;

import com._pi.benepick.domain.members.entity.Members;
import com._pi.benepick.domain.members.repository.MembersRepository;
import com._pi.benepick.global.common.annotation.MemberObject;
import com._pi.benepick.global.common.jwt.JwtTokenProvider;
import com._pi.benepick.global.common.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;
    private final MembersRepository membersRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(MemberObject.class) != null &&
            parameter.getParameterType().equals(Members.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = CookieUtils.getCookieValue(webRequest.getNativeRequest(HttpServletRequest.class), "accessToken");

        if(token == null)
            return null;

        return membersRepository.findById(jwtTokenProvider.getUserPk(token)).orElse(null);
    }
}
