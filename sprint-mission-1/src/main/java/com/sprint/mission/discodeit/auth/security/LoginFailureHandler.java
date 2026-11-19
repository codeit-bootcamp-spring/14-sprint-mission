package com.sprint.mission.discodeit.auth.security;

import com.sprint.mission.discodeit.global.exception.ExceptionType;
import com.sprint.mission.discodeit.global.security.SecurityErrorWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final SecurityErrorWriter errorWriter;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException {
        // 무차별 대입 시도를 추적할 수 있게 남긴다. 비밀번호는 절대 로그에 남기지 않는다
        log.warn("로그인 실패: username={}, ip={}, reason={}",
            request.getParameter("username"), request.getRemoteAddr(),
            exception.getClass().getSimpleName());
        errorWriter.write(response, ExceptionType.LOGIN_UNAUTHORIZED);
    }
}
