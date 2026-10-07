package com.sprint.mission.discodeit.user.application.provided.command;

import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.user.application.dto.UserDto;
import com.sprint.mission.discodeit.user.application.dto.UserUpdateRequest;
import java.util.UUID;

public interface UserModifier {

  UserDto modify(UUID userId, UserUpdateRequest request, BinaryContentCreateRequest profile);
}
