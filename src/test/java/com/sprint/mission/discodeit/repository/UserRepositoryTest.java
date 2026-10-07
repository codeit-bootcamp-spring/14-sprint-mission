package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TestEntityManager em;

  private User saveUser(String username, String email) {
    User user = new User(username, email, "password", null);
    new UserStatus(user, Instant.now());
    return userRepository.save(user);
  }

  @Test
  @DisplayName("username으로 사용자를 찾는다")
  void findByUsername_success() {
    User saved = saveUser("alice", "alice@example.com");
    em.flush();
    em.clear();

    Optional<User> found = userRepository.findByUsername("alice");

    assertThat(found).isPresent();
    assertThat(found.get().getId()).isEqualTo(saved.getId());
  }

  @Test
  @DisplayName("없는 username이면 빈 Optional을 반환한다")
  void findByUsername_notFound() {
    saveUser("alice", "alice@example.com");

    assertThat(userRepository.findByUsername("nobody")).isEmpty();
  }

  @Test
  @DisplayName("이메일 중복 여부를 확인한다")
  void existsByEmail() {
    saveUser("alice", "alice@example.com");

    assertThat(userRepository.existsByEmail("alice@example.com")).isTrue();
    assertThat(userRepository.existsByEmail("bob@example.com")).isFalse();
  }

  @Test
  @DisplayName("수정 시 자기 자신은 이메일 중복에서 제외한다")
  void existsByEmailAndIdNot() {
    User alice = saveUser("alice", "alice@example.com");
    User bob = saveUser("bob", "bob@example.com");

    assertThat(userRepository.existsByEmailAndIdNot("alice@example.com", alice.getId())).isFalse();
    assertThat(userRepository.existsByEmailAndIdNot("alice@example.com", bob.getId())).isTrue();
  }

  @Test
  @DisplayName("사용자 목록을 상태와 함께 fetch join으로 조회한다")
  void findAllWithProfileAndStatus_success() {
    saveUser("alice", "alice@example.com");
    saveUser("bob", "bob@example.com");
    em.flush();
    em.clear();

    List<User> users = userRepository.findAllWithProfileAndStatus();

    assertThat(users).hasSize(2);
    assertThat(users).allSatisfy(user -> assertThat(user.getStatus()).isNotNull());
  }

  @Test
  @DisplayName("사용자가 없으면 빈 목록을 반환한다")
  void findAllWithProfileAndStatus_empty() {
    assertThat(userRepository.findAllWithProfileAndStatus()).isEmpty();
  }
}
