package com.sprint.mission.discodeit.user.web.dto.req;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDTO(
    @NotBlank(message = "제대로 입력해주세요")
    String username,
    @NotBlank(message = "제대로 입력해주세요")
    String password
) {

}
