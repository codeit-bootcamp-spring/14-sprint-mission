package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class User {
    private final int userId;
    private final String userName;
    private final String role;
    private final LocalDateTime createAt = LocalDateTime.now();
    private final LocalDateTime updateAt = LocalDateTime.now();

}
