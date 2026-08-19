package com.sprint.mission.discodeit.web.controller.resolver;

import com.sprint.mission.discodeit.domain.entity.User;
import com.sprint.mission.discodeit.domain.service.user.UserService;
import com.sprint.mission.discodeit.web.controller.session.SessionConst;
import com.sprint.mission.discodeit.global.annotation.Login;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean hasMemberType = User.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasMemberType;
    }

    @Nullable
    @Override
    public User resolveArgument(MethodParameter parameter,
        @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
        @Nullable WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        if(session == null){
            return null;
        }

        UUID userId = (UUID)session.getAttribute(SessionConst.LOGIN_USER);
        return userService.findById(userId);
    }
}
