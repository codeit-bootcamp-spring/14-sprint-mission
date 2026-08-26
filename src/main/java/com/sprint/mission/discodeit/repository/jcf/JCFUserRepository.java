package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;


@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFUserRepository extends JCFRepository<User> implements UserRepository {

    private JCFUserRepository() {
       super();
    };

    @Override
    public Optional<User> findByUserName(String userName) {
        return findAll().stream().filter(user -> user.getUserName().equals(userName))
            .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return findAll().stream()
            .filter(user -> user.getEmail().equals(email))
            .findFirst();
    }
}
