package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class User {
  private final int userId;
  private final long createAt;
  private final long updateAt;
  private final String userName;

}
