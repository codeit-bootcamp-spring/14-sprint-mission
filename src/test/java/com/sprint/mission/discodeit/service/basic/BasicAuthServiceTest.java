package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.auth.dto.AuthLoginRequestDto;
import com.sprint.mission.discodeit.auth.application.basic.BasicAuthService;
import com.sprint.mission.discodeit.user.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.UserResponseDto;
import com.sprint.mission.discodeit.common.exception.AuthenticationFailedException;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.binaryContent.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.user.repository.UserStatusRepository;
import com.sprint.mission.discodeit.user.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.user.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.auth.application.AuthService;
import com.sprint.mission.discodeit.user.application.UserService;
import com.sprint.mission.discodeit.user.application.basic.BasicUserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class BasicAuthServiceTest {

    private AuthService authService;
    private UserService userService;

    @BeforeEach
    void setUp(){
        UserRepository userRepository = new JCFUserRepository();
        UserStatusRepository userStatusRepository = new JCFUserStatusRepository();
        authService = new BasicAuthService(userRepository, userStatusRepository);
        userService = new BasicUserService(
                userRepository,
                userStatusRepository,
                new JCFBinaryContentRepository()
        );
    }

    @Test
    void 유저를_생성하면_로그인할_수_있다(){
        MultipartFile image = new MockMultipartFile(
                "profile",
                "profile",
                "contentType",
                new byte[]{1,2,3,4}
        );
        UserResponseDto created = userService.create(new UserCreateRequestDto("김양현", "yyy2724@naver.com", "2724"), image);

        authService.login(new AuthLoginRequestDto("김양현","2724"));

    }

    @Test
    void 유저를_생성하고_비밀번호를_틀리면_오류를_발생한다(){
        MultipartFile image = new MockMultipartFile(
                "profile",
                "profile",
                "contentType",
                new byte[]{1,2,3,4}
        );
        UserResponseDto created = userService.create(new UserCreateRequestDto("김양현", "yyy2724@naver.com", "2724"), image);

        assertThrows(AuthenticationFailedException.class,
                ()->authService.login(new AuthLoginRequestDto("김양현","1111")));
    }
}