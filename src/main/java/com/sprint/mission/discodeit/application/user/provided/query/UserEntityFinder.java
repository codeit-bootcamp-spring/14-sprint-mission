package com.sprint.mission.discodeit.application.user.provided.query;

import com.sprint.mission.discodeit.domain.User;
import java.util.List;
import java.util.UUID;

public interface UserEntityFinder {

  User getEntityById(UUID userId);

  List<User> getEntitiesById(List<UUID> userIds);

  User getEntityByUsername(String username);
}
