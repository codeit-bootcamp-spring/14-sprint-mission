package com.sprint.mission.discodeit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@DataJpaTest
@EnableJpaAuditing
public class UserSliceTest {
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        //given
            user = new User(
            "kimoo",
            "kim@gmail.com",
            "1234",
            null
        );
    }
    @Test
    @DisplayName("유저를 이름으로 찾는다")
    void success_findByUsername() {
        //given
        userRepository.saveAndFlush(user);

        //when
        Optional<User> result = userRepository.findByUsername("kimoo");

        //then
        assertTrue(result.isPresent());
        assertEquals("kim@gmail.com", result.get().getEmail());
    }

    @Test
    @DisplayName("존재하지 않는 유저의 이름으로 유저 찾으면 실패한다")
    void fail_findByUsername_NotFoundUser() {
        //given

        //when
        Optional<User> result = userRepository.findByUsername("kimoo");

        //then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("존재하는 이메일을 조회한다")
    void success_existsByEmail() {
        //given
        userRepository.saveAndFlush(user);

        //when
        boolean result = userRepository.existsByEmail("kim@gmail.com");

        //then
        assertTrue(result);
    }

    @Test
    @DisplayName("존재하지 않는 이메일을 조회한다")
    void fail_existsByEmail() {
        //given

        //when
        boolean result = userRepository.existsByEmail("unknown@gmail.com");

        //then
        assertFalse(result);
    }

    @Test
    @DisplayName("유저를 이름기준으로 내림차순해서 조회한다")
    void success_findAllByPageAndSort() {
        //given
        User user1 = new User(
            "pamoo",
            "pa@gmail.com",
            "1234",
            null
        );
        User user2 = new User(
            "leemoo",
            "lee@gmail.com",
            "1234",
            null
        );
        User user3 = new User(
            "choimoo",
            "choi@gmail.com",
            "1234",
            null
        );
        userRepository.saveAllAndFlush(List.of(user, user1, user2, user3));

        PageRequest pageRequest = PageRequest.of(
            0,
            10,
            Sort.by(Sort.Direction.ASC, "username")
        );

        //when
        Page<User> result = userRepository.findAll(pageRequest);

        //then
        assertEquals(4, result.getContent().size());
        assertEquals("choimoo", result.getContent().get(0).getUsername());
        assertEquals("kimoo", result.getContent().get(1).getUsername());
    }
}
