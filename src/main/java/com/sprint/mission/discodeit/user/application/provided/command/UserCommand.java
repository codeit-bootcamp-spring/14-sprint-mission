package com.sprint.mission.discodeit.user.application.provided.command;

import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.user.domain.User;

public interface UserCommand {

  User create(String username, String email, String password, BinaryContent profile);

  void delete(User user);

  User update(User user, String username, String email, String password, BinaryContent profile);
}
