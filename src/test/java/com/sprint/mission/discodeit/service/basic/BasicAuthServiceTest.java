package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.authLogin.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.user.UserRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.exception.AuthenticationFailedException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
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