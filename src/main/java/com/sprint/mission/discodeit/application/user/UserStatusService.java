package com.sprint.mission.discodeit.application.user;

import com.sprint.mission.discodeit.adaptor.dto.userStatusDto.UserStatusRequest;
import com.sprint.mission.discodeit.adaptor.dto.userStatusDto.UserStatusResponse;
import com.sprint.mission.discodeit.adaptor.dto.userStatusDto.UserStatusUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  UserStatusResponse create(UserStatusRequest request);

  UserStatusResponse findUserStatusByUserId(UUID userId);

  List<UserStatusResponse> findAll();

  void delete(UserStatusRequest request);

  UserStatusResponse update(UUID userStatusId, UserStatusUpdateRequest request);

  UserStatusResponse updateByUserId(UUID userId, UserStatusUpdateRequest request);
}
