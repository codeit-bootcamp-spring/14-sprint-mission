package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Message {
    private final UUID messageId = UUID.randomUUID();
    private final LocalDateTime createAt = LocalDateTime.now();
    private final LocalDateTime updateAt = LocalDateTime.now();
    @Setter
    private String message;

}
