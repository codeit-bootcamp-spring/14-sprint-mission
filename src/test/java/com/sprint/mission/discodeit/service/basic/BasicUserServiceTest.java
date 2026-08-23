package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.auth.dto.AuthLoginRequestDto;
import com.sprint.mission.discodeit.auth.application.basic.BasicAuthService;
import com.sprint.mission.discodeit.user.application.UserStatusService;
import com.sprint.mission.discodeit.user.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.UserResponseDto;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequestDto;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.common.exception.AuthenticationFailedException;
import com.sprint.mission.discodeit.common.exception.NoSuchElementException;
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

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class BasicUserServiceTest {


    private UserService userService;

    private AuthService authService;

    @BeforeEach
    void setUp(){
        UserRepository userRepository = new JCFUserRepository();
        UserStatusRepository userStatusRepository = new JCFUserStatusRepository();
        userService = new BasicUserService(
                userRepository,
                new JCFUserStatusRepository(),
                new JCFBinaryContentRepository()
        );
        authService = new BasicAuthService(
                userRepository,
                userStatusRepository
        );
    }

    @Test
    void 유저를_생성하면_조회할_수_있다(){
        UserResponseDto created = userService.create(new UserCreateRequestDto("김양현", "yyy2724@naver.com", "2724"),
                new MockMultipartFile(
                        "profile",
                        new byte[]{1,2,3,4}
                ));
        UserResponseDto found = userService.find(created.id());

        assertEquals(created.id(), found.id());
        assertEquals("김양현", found.username());
        assertEquals("yyy2724@naver.com", found.email());

    }

    @Test
    void 유저를_생성하면_전체조회시_포함되어_있다(){

        UserResponseDto created = userService.create(new UserCreateRequestDto("김양횬", "y@naver.com", "2724"),
                new MockMultipartFile(
                        "profile",
                        new byte[]{1,2,3,4,5,6,7}
                ));

        boolean 포함되어_있는가 = userService.findAll().stream()
                .anyMatch(user -> user.id().equals(created.id()));

        assertTrue(포함되어_있는가);
    }

    @Test
    void 유저를_생성하고_업데이트하면_조회할_수_있으며_로그인할_수_있다_그리고_비밀번호_다를_시_에러를_낸다(){
        UserResponseDto created = userService.create(new UserCreateRequestDto("김양햔", "hyan@naver.com", "1234"),
                new MockMultipartFile(
                        "profile",
                        new byte[]{1,2,3,4,5,6,7,8}
                ));

        UserUpdateRequestDto request = new UserUpdateRequestDto(created.profileId(),"새김양현",
                "new@naver.com", "password");

        userService.update(created.id(), request, null);

        UserResponseDto found = userService.find(created.id());
        assertEquals("새김양현", found.username());
        assertEquals("new@naver.com", found.email());

        AuthLoginRequestDto login = new AuthLoginRequestDto("새김양현", "password");
        assertDoesNotThrow(() -> authService.login(login));
        assertThrows(AuthenticationFailedException.class, () -> authService.login(new AuthLoginRequestDto("새김양현", "1234")));
    }

    @Test
    void 유저를_생성하면_유저명과_이메일로_조회할_수_있다(){
        UserResponseDto created = userService.create(new UserCreateRequestDto("김양현", "yyy2724@naver.com", "2724"),
                new MockMultipartFile(
                        "profile",
                        new byte[]{1,2,3,4}
                )
        );

        User findUserName = userService.findByUsername(created.username()).orElseThrow();
        User findEmail = userService.findByEmail(created.email()).orElseThrow();

        assertEquals("김양현", findUserName.getUserName());
        assertEquals("yyy2724@naver.com", findEmail.getEmail());

    }

    @Test
    void 유저를_생성하고_삭제하면_관련된_모든_것을_삭제_할_수_있다(){
        byte[] image = {1,2,3,4};
        UserResponseDto created = userService.create(new UserCreateRequestDto("김양현", "yyy2724@naver.com", "2724"),
                new MockMultipartFile(
                        "profile",
                        new byte[]{1,2,3,4}
                )
        );

        userService.delete(created.id());

        assertThrows(NoSuchElementException.class, () ->userService.find(created.id()));
    }


}