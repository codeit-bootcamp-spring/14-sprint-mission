package com.sprint.mission.discodeit.dto.auth;


import jakarta.validation.Valid;

public record LoginRequestDto(@Valid String name,
                              @Valid String password) {

}
