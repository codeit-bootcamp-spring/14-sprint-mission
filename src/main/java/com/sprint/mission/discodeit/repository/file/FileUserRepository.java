package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserRepository extends FileRepository<User> implements UserRepository {


    public FileUserRepository(
        @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory) {
        super(fileDirectory, "user");
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return findAll().stream()
            .filter(user -> user.getUserName().equals(userName))
            .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return findAll().stream()
            .filter(user -> user.getEmail().equals(email))
            .findFirst();
    }


}
