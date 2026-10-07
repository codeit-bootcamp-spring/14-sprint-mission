package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.exception.ErrorCodeStatusMapper;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@Import({ErrorCodeStatusMapper.class})
@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserService userService;
  @MockitoBean
  private UserStatusService userStatusService;

  private static MockMultipartFile jsonPart(String name, String json) {
    return new MockMultipartFile(name, "", MediaType.APPLICATION_JSON_VALUE,
        json.getBytes(StandardCharsets.UTF_8));
  }

  @Test
  @DisplayName("POST /api/users: 사용자 생성 성공 시 201과 사용자 JSON을 반환한다")
  void create_success() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    given(userService.create(
        eq(new UserCreateRequest("alice", "alice@example.com", "pass1234")),
        eq(Optional.empty())))
        .willReturn(new UserDto(userId, "alice", "alice@example.com", null, true));

    // when & then
    mockMvc.perform(multipart("/api/users")
            .file(jsonPart("userCreateRequest",
                """
                {"username":"alice","email":"alice@example.com","password":"pass1234"}
                """)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value("alice"))
        .andExpect(jsonPath("$.email").value("alice@example.com"));
  }

  @Test
  @DisplayName("GET /api/users: 사용자 목록을 반환한다")
  void findAll_success() throws Exception {
    // given
    given(userService.findAll()).willReturn(List.of(
        new UserDto(UUID.randomUUID(), "alice", "alice@example.com", null, true),
        new UserDto(UUID.randomUUID(), "bob", "bob@example.com", null, false)));

    // when & then
    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[1].username").value("bob"));
  }

  @Test
  @DisplayName("DELETE /api/users/{id}: 삭제 성공 시 204")
  void delete_success() throws Exception {
    mockMvc.perform(delete("/api/users/{userId}", UUID.randomUUID()))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("POST /api/users: 검증 실패 시 400과 ErrorResponse를 반환한다")
  void create_invalidRequest() throws Exception {
    mockMvc.perform(multipart("/api/users")
            .file(jsonPart("userCreateRequest",
                """
                {"username":"","email":"not-email","password":"1"}
                """)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("INVALID_INPUT_VALUE"))
        .andExpect(jsonPath("$.status").value(400));
  }

  @Test
  @DisplayName("DELETE /api/users/{id}: 없는 사용자면 404와 ErrorResponse를 반환한다")
  void delete_userNotFound() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    willThrow(UserNotFoundException.withId(userId)).given(userService).delete(userId);

    // when & then
    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("USER_NOT_FOUND"))
        .andExpect(jsonPath("$.details.userId").value(userId.toString()))
        .andExpect(jsonPath("$.exceptionType").value("UserNotFoundException"));
  }
}
