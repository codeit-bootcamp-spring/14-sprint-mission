package com.sprint.mission.discodeit.application.user.provided.command;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.User;

public interface UserCommand {

  User create(String username, String email, String password, BinaryContent profile);

  void delete(User user);

  User update(User user, String username, String email, String password, BinaryContent profile);
}
