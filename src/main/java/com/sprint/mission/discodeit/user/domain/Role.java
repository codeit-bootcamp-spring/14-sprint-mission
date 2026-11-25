package com.sprint.mission.discodeit.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum Role {
  ADMIN("Admin"),
  CHANNEL_MANAGER("ChannelManager"),
  USER("user");

  String description;
}
