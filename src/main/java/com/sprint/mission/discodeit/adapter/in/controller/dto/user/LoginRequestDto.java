package com.sprint.mission.discodeit.adapter.in.controller.dto.user;


import jakarta.validation.Valid;

public record LoginRequestDto(@Valid String username,
                              @Valid String password) {

}
