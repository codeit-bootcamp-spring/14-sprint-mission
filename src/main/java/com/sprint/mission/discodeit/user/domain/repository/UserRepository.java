package com.sprint.mission.discodeit.user.domain.repository;

import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import com.sprint.mission.discodeit.message.domain.entity.Message;
import com.sprint.mission.discodeit.user.domain.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("""
    select u
        from User u
        join fetch u.userStatus
        left join fetch u.profileImage
    """)
    List<User> findAllWithProfileImageAndUserStatus();

    boolean existsByName(String name);
    boolean existsByEmail(String email);
    Optional<User> findByName(String name);

    default User getByNameOrThrow(String name) {
        return findByName(name).orElseThrow(() -> new CustomException(CustomErrorCode.USER_AUTH_MISMATCH));
    }

    default User getByIdOrThrow(UUID userId) {
        return findById(userId).orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
    }

    default void validateExistsById(UUID userId) {
        if (!existsById(userId)) {
            throw new CustomException(CustomErrorCode.USER_NOT_FOUND);
        }
    }
}
