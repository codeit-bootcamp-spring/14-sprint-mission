package com.sprint.mission.discodeit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.common.exception.user.EmailAlreadyExistsException;
import com.sprint.mission.discodeit.common.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import java.util.Optional;
import java.util.UUID;
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

    @Test
    @DisplayName("중복된 이메일로 유저를 생성하면 회원가입에 실패한다")
    void fial_newUser_DuplicatedEmail() { /* 단위 테스트 이기때문에 실제 db에 유저 저장하고 중복 생성은 하면 안됌 */

        //given
        UserCreateRequest userCreateRequest = new UserCreateRequest(
            "Kimoo",
            "kim@gmail.com",
            "1234"
        );
        given(userRepository.existsByEmail("kim@gmail.com"))
            .willReturn(true);
        //when,then
        assertThrows(
            EmailAlreadyExistsException.class,
            () -> userService.create(userCreateRequest, Optional.empty())
        );
    }

    @Test
    @DisplayName("정상적인 업데이트 요청을 받으면 유저를 업데이트 한다.")
    void success_updateUser_CorrectRequest() { /* 단위 테스트 이기때문에 실제 db에 유저 저장하고 중복 생성은 하면 안됌 */

        //given
        UUID uuid = UUID.randomUUID();
        User target = new User(
            "kimoo",
            "kim@gmail.com",
            "1234",
            null
        );

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
            "newKimoo",
            "newkim@gmail.com",
            "new1234"
        );

        // 아이디로 유저 가져오기
        given(userRepository.findById(uuid))
            .willReturn(Optional.of(target));
        // 아이디랑 이메일이 중복이면 정상 생성이 안되니까 false로 주기
        given(userRepository.existsByUsername("newKimoo"))
            .willReturn(false);
        given(userRepository.existsByEmail("newkim@gmail.com"))
            .willReturn(false);
        //when
        UserDto dto = userService.update(uuid, userUpdateRequest, Optional.empty());

        //then
        assertEquals("newKimoo", dto.username());
        assertEquals("newkim@gmail.com", dto.email());
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 업데이트 요청시 업데이트에 실패한다.")
    void fail_updateUser_DuplicatedEmail() { /* 단위 테스트 이기때문에 실제 db에 유저 저장하고 중복 생성은 하면 안됌 */

        //given
        UUID uuid = UUID.randomUUID();
        User target = new User(
            "kimoo",
            "kim@gmail.com",
            "1234",
            null
        );

        User other = new User(
            "leemoo",
            "leemoo@gmail.com",
            "1234",
            null
        );

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
            "newKimoo",
            "leemoo@gmail.com",
            "new1234"
        );

        // 아이디로 유저 가져오기
        given(userRepository.findById(uuid))
            .willReturn(Optional.of(target));
        // 이메일 중복 걸리기
        given(userRepository.existsByEmail(other.getEmail()))
            .willReturn(true);
        //when,then
        assertThrows(EmailAlreadyExistsException.class,
            () -> userService.update(uuid, userUpdateRequest, Optional.empty()));
    }

    @Test
    @DisplayName("정상적인 유저삭제 요청을 받으면 유저를 삭제한다.")
    void success_deleteUser_CorrectRequest() {
        //given
        User target = new User(
            "kimoo",
            "kim@gmail.com",
            "1234",
            null
        );

        given(userRepository.findById(target.getId()))
            .willReturn(Optional.of(target));

        //when
        userService.delete(target.getId());

        //then
        then(userRepository).should().deleteById(target.getId());
    }

    @Test
    @DisplayName("존재하지 않는 유저삭제 요청을 받으면 예외를 반환한다.")
    void fail_deleteUser_noUser() {
        //given
        UUID uuid = UUID.randomUUID();

        given(userRepository.findById(uuid))
            .willReturn(Optional.empty());

        //when,then
        assertThrows(UserNotFoundException.class,
            () -> userService.delete(uuid));
    }
}
