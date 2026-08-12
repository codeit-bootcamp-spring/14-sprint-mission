package com.sprint.mission.discodeit.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateRequest {
    private String username;
    private String email;
    private String password;
    private UUID profileImageId;
}
