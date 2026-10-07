package com.sprint.mission.discodeit.user.application.provided.command;

import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.user.application.dto.UserCreateRequest;
import com.sprint.mission.discodeit.user.application.dto.UserDto;

public interface UserRegister {

  UserDto register(UserCreateRequest request, BinaryContentCreateRequest profile);
}
