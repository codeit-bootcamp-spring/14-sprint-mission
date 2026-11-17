package com.sprint.mission.discodeit.user.repository;

import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import com.sprint.mission.discodeit.common.RepositoryTestConfig;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(RepositoryTestConfig.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager em;

    private User saveUser(String username, String email, boolean withProfile) {
        User user = User.create(username, email, "password1234");
        if (withProfile) {
            user.updateProfile(new BinaryContent("profile.png", 3L, "image/png"));
        }
        user.updateUserStatus(UserStatus.create(user));
        return em.persist(user);
    }

    @Nested
    @DisplayName("findByUserName")
    class FindByUserName {

        @Test
        @DisplayName("성공 - username으로 사용자를 찾는다")
        void findByUserName_success() {
            saveUser("kim", "kim@test.com", false);
            em.flush();
            em.clear();

            Optional<User> result = userRepository.findByUserName("kim");

            assertThat(result).isPresent();
            assertThat(result.get().getEmail()).isEqualTo("kim@test.com");
            assertThat(result.get().getCreatedAt()).isNotNull();   // JPA Auditing 동작 확인
        }

        @Test
        @DisplayName("실패 - 없는 username이면 빈 Optional")
        void findByUserName_notFound() {
            saveUser("kim", "kim@test.com", false);
            em.flush();
            em.clear();

            assertThat(userRepository.findByUserName("lee")).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByEmail")
    class FindByEmail {

        @Test
        @DisplayName("성공 - email로 사용자를 찾는다")
        void findByEmail_success() {
            saveUser("kim", "kim@test.com", false);
            em.flush();
            em.clear();

            Optional<User> result = userRepository.findByEmail("kim@test.com");

            assertThat(result).isPresent();
            assertThat(result.get().getUserName()).isEqualTo("kim");
        }

        @Test
        @DisplayName("실패 - 없는 email이면 빈 Optional")
        void findByEmail_notFound() {
            saveUser("kim", "kim@test.com", false);
            em.flush();
            em.clear();

            assertThat(userRepository.findByEmail("none@test.com")).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAll (@EntityGraph)")
    class FindAll {

        @Test
        @DisplayName("성공 - 프로필과 사용자 상태를 한 번에 함께 조회한다")
        void findAll_fetchesProfileAndStatus() {
            saveUser("kim", "kim@test.com", true);
            saveUser("lee", "lee@test.com", false);
            em.flush();
            em.clear();

            List<User> result = userRepository.findAll();

            assertThat(result).hasSize(2);
            User kim = result.stream().filter(u -> u.getUserName().equals("kim")).findFirst().orElseThrow();
            assertThat(Hibernate.isInitialized(kim.getProfile())).isTrue();
            assertThat(Hibernate.isInitialized(kim.getUserStatus())).isTrue();
            assertThat(kim.getProfile().getFileName()).isEqualTo("profile.png");
        }

        @Test
        @DisplayName("실패(경계) - 사용자가 없으면 빈 목록")
        void findAll_empty() {
            assertThat(userRepository.findAll()).isEmpty();
        }
    }
}
