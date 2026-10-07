package com.sprint.mission.discodeit.user.application.provided.query;

import com.sprint.mission.discodeit.user.domain.User;
import java.util.List;
import java.util.UUID;

public interface UserEntityFinder {

  User getEntityById(UUID userId);

  List<User> getEntitiesById(List<UUID> userIds);

  User getEntityByUsername(String username);
}
