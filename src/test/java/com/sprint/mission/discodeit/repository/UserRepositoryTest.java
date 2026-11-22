package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(new User("tester", "tester@test.com", "1234", null));
    }

    @Test
    @DisplayName("username으로 사용자 조회 성공")
    void findByUsername_success() {
        Optional<User> result = userRepository.findByUsername("tester");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("tester@test.com");
        assertThat(result.get().getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("username으로 사용자 조회 실패 - 없는 username이면 비어 있다")
    void findByUsername_fail() {
        Optional<User> result = userRepository.findByUsername("nobody");

        assertThat(result).isEmpty();
    }
}
