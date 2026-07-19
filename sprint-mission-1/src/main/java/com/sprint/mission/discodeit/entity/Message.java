package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Message {
    private final User user;
    private String message;
    private final UUID messageId = UUID.randomUUID();
    private final String createAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

}
