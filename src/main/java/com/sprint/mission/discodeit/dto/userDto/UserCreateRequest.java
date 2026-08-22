package com.sprint.mission.discodeit.dto.userDto;

public record UserCreateRequest (
    String name,
    String password,
    String email
){
}
