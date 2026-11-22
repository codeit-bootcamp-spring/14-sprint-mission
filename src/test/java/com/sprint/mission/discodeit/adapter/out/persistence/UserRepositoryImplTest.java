package com.sprint.mission.discodeit.adapter.out.persistence;

import com.sprint.mission.discodeit.adapter.out.persistence.config.QueryDslConfig;
import com.sprint.mission.discodeit.adapter.out.persistence.user.UserRepositoryImpl;
import com.sprint.mission.discodeit.core.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.core.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@Import({
        QueryDslConfig.class,
        UserRepositoryImpl.class,
})
@DataJpaTest(
        excludeAutoConfiguration = DataJpaRepositoriesAutoConfiguration.class
)
class UserRepositoryImplTest {
    @Autowired
    TestEntityManager em;

    @Autowired
    UserRepositoryImpl userRepository;

    @Test
    @DisplayName("User 다건 조회 성공")
    void givenSavedUsers_whenFindByIdIn_thenReturnMatchingUsers() {
        // given
        BinaryContent profile = BinaryContent.of("profile.png", "image/png", new byte[]{1});
        User user1 = User.createUser("test1", "test1@gmail.com", "1234", profile);
        User user2 = User.createUser("test2", "test2@gmail.com", "1234", null);
        User user3 = User.createUser("test3", "test3@gmail.com", "1234", null);
        User user4 = User.createUser("test4", "test4@gmail.com", "1234", null);
        List<User> users = List.of(user1, user2, user3, user4);
        users.forEach(em::persist);
        em.flush();
        em.clear();

        // when
        UUID invalidId = UUID.randomUUID();
        List<User> found = userRepository.findByIdIn(List.of(user1.getId(), user2.getId(), invalidId));

        // then
        assertThat(found).hasSize(2);
        assertThat(found)
                .extracting(User::getId)
                .containsAll(List.of(user1.getId(), user2.getId()));
    }

    @Test
    @DisplayName(value = "User 전체 조회 성공")
    void givenSavedUsers_whenFindAllUser_thenReturnAllUser() {
        List<User> users = List.of(
                User.createUser("test1", "test1@gmail.com", "1234", null),
                User.createUser("test2", "test2@gmail.com", "1234", null)
        );
        users.forEach(em::persist);
        em.flush();
        em.clear();


        List<User> found = userRepository.findAll();


        assertThat(found).hasSize(users.size());
        assertThat(found)
                .extracting(User::getId)
                .containsAll(users.stream().map(User::getId).toList());
    }
}