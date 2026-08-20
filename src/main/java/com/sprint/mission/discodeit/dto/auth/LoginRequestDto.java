package com.sprint.mission.discodeit.dto.auth;


import jakarta.validation.Valid;

public record LoginRequestDto(@Valid String username,
                              @Valid String password) {

}
