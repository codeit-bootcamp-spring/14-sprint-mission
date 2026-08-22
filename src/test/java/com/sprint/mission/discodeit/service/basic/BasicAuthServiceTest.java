package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.auth.dto.AuthLoginRequestDto;
import com.sprint.mission.discodeit.auth.application.basic.BasicAuthService;
import com.sprint.mission.discodeit.user.dto.UserRequestDto;
import com.sprint.mission.discodeit.user.dto.UserResponseDto;
import com.sprint.mission.discodeit.common.exception.AuthenticationFailedException;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.binaryContent.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.user.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.user.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.auth.application.AuthService;
import com.sprint.mission.discodeit.user.application.UserService;
import com.sprint.mission.discodeit.user.application.basic.BasicUserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class BasicAuthServiceTest {

    private AuthService authService;
    private UserService userService;

    @BeforeEach
    void setUp(){
        UserRepository userRepository = new JCFUserRepository();
        authService = new BasicAuthService(userRepository);
        userService = new BasicUserService(
                userRepository,
                new JCFUserStatusRepository(),
                new JCFBinaryContentRepository()
        );
    }

    @Test
    void 유저를_생성하면_로그인할_수_있다(){
        byte[] image = {1,2,3,4};
        UserResponseDto created = userService.create(new UserRequestDto("김양현", "yyy2724@naver.com", "2724", image));

        authService.login(new AuthLoginRequestDto("김양현","2724"));

    }

    @Test
    void 유저를_생성하고_비밀번호를_틀리면_오류를_발생한다(){
        byte[] image = {1,2,3,4};
        UserResponseDto created = userService.create(new UserRequestDto("김양현", "yyy2724@naver.com", "2724", image));

        assertThrows(AuthenticationFailedException.class,
                ()->authService.login(new AuthLoginRequestDto("김양현","1111")));
    }
}