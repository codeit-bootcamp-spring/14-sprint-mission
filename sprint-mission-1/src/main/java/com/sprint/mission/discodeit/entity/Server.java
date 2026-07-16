package com.sprint.mission.discodeit.entity;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
public class Server {
    private final UUID serverId = UUID.randomUUID();
    @Setter
    private String serverName;
    private final LocalDateTime createAt = LocalDateTime.now();
    private final LocalDateTime updateAt = LocalDateTime.now();

}
