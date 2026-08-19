package com.sprint.mission.discodeit.domain.repository.map.user;

import com.sprint.mission.discodeit.domain.entity.User;
import com.sprint.mission.discodeit.domain.repository.UserRepository;
import com.sprint.mission.discodeit.domain.repository.map.AbstractMapCrudRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class UserMapCrudRepositoryImpl extends AbstractMapCrudRepository<User> implements UserRepository {

    @Override
    public Optional<User> findByEmail(String email) {
        List<User> allEntity = super.findAllEntity();
        return allEntity.stream()
            .filter(entity -> entity.getEmail().equals(email))
            .findFirst();       //얘가 알아서 옵셔널로 감싸서 리턴해준다함
    }

    @Override
    public boolean existAllById(List<UUID> idList) {
        List<User> allEntity = super.findAllEntity();
        log.info("repository ------------------ {}", allEntity);
        Set<UUID> userIds = allEntity.stream()
            .map(User::getId)
            .collect(Collectors.toSet());

        return userIds.containsAll(idList);
    }
}
