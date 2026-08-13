package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserStatusRepository
        extends AbstractFileRepository<UserStatus>
        implements UserStatusRepository {

    private static final String USER_STATUS_FILENAME = "user-statuses.ser";

    private final Map<UUID, UserStatus> userStatusMap;

    public FileUserStatusRepository(
            @Value("${discodeit.repository.file-directory:.discodeit/objects}")
            String fileDirectory
    ) {
        super("UserStatus", fileDirectory, USER_STATUS_FILENAME);
        this.userStatusMap = loadFile();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        UserStatus previousUserStatus =
                userStatusMap.put(userStatus.getId(), userStatus);
        try {
            saveFile(userStatusMap);
        } catch (RuntimeException exception) {
            if (Objects.isNull(previousUserStatus)) {
                userStatusMap.remove(userStatus.getId());
            } else {
                userStatusMap.put(previousUserStatus.getId(), previousUserStatus);
            }
            throw exception;
        }
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID userStatusId) {
        return Optional.ofNullable(userStatusMap.get(userStatusId));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return userStatusMap.values().stream()
                .filter(userStatus -> Objects.equals(userStatus.getUserId(), userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusMap.values().stream().toList();
    }

    @Override
    public void delete(UUID userStatusId) {
        UserStatus deletedUserStatus = userStatusMap.remove(userStatusId);
        try {
            saveFile(userStatusMap);
        } catch (RuntimeException exception) {
            if (Objects.nonNull(deletedUserStatus)) {
                userStatusMap.put(deletedUserStatus.getId(), deletedUserStatus);
            }
            throw exception;
        }
    }

}
