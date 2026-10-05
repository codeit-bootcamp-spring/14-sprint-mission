package com.sprint.mission.discodeit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserUnitTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private BinaryContentRepository binaryContentRepository;
    @Mock
    private UserStatusRepository userStatusRepository;

    private BasicUserService userService;

    @BeforeEach
    void setUp() {
        userService = new BasicUserService(
            userRepository,
            binaryContentRepository,
            userStatusRepository,
            new UserMapper()
        );
    }

    @Test
    @DisplayName("정상적인 입력을 받은 유저는 생성이 된다")
    void success_newUser_correctRequest() {
        //given
        UserCreateRequest userCreateRequest = new UserCreateRequest(
            "Kimoo",
            "kim@gmail.com",
            "1234"
        );
        // 어짜피 서비스의 반환을 검증하기떄문에 유저 객체를 직접 만들필요가 없음
        given(userRepository.save(any(User.class)))
            .willAnswer(returnsFirstArg());
        //when
        UserDto userDto = userService.create(userCreateRequest, Optional.empty());

        //then
        assertEquals("Kimoo", userDto.username());
        assertEquals("kim@gmail.com", userDto.email());
    }

}
