package com.sprint.mission.discodeit.user.controller;

import com.sprint.mission.discodeit.common.exception.DuplicateUsernameException;
import com.sprint.mission.discodeit.common.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.application.UserService;
import com.sprint.mission.discodeit.user.application.UserStatusService;
import com.sprint.mission.discodeit.user.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private UserStatusService userStatusService;

    // multipart 요청의 JSON 파트
    private MockMultipartFile jsonPart(String name, String json) {
        return new MockMultipartFile(name, "", MediaType.APPLICATION_JSON_VALUE, json.getBytes(StandardCharsets.UTF_8));
    }

    @Nested
    @DisplayName("POST /api/users")
    class Create {

        @Test
        @DisplayName("성공 - 201과 생성된 사용자 JSON을 반환한다 (비밀번호는 응답에 없다)")
        void create_success() throws Exception {
            // given
            UUID userId = UUID.randomUUID();
            given(userService.create(any(UserCreateRequestDto.class), isNull()))
                    .willReturn(new UserDto(userId, "kim", "kim@test.com", null, true));

            // when & then
            mockMvc.perform(multipart("/api/users")
                            .file(jsonPart("userCreateRequest", """
                                    {"username": "kim", "email": "kim@test.com", "password": "password1234"}
                                    """)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(userId.toString()))
                    .andExpect(jsonPath("$.username").value("kim"))
                    .andExpect(jsonPath("$.email").value("kim@test.com"))
                    .andExpect(jsonPath("$.password").doesNotExist());
        }

        @Test
        @DisplayName("실패 - email 형식이 틀리면 400과 필드별 오류 메시지를 반환한다")
        void create_invalidEmail_fail() throws Exception {
            mockMvc.perform(multipart("/api/users")
                            .file(jsonPart("userCreateRequest", """
                                    {"username": "kim", "email": "not-an-email", "password": "password1234"}
                                    """)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("INVALID_INPUT"))
                    .andExpect(jsonPath("$.details.email").value("올바른 email 형식이 아닙니다."));

            then(userService).should(never()).create(any(), any());
        }

        @Test
        @DisplayName("실패 - 중복 username이면 409와 ErrorResponse를 반환한다")
        void create_duplicateUsername_fail() throws Exception {
            given(userService.create(any(UserCreateRequestDto.class), isNull()))
                    .willThrow(new DuplicateUsernameException("kim"));

            mockMvc.perform(multipart("/api/users")
                            .file(jsonPart("userCreateRequest", """
                                    {"username": "kim", "email": "kim@test.com", "password": "password1234"}
                                    """)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.code").value("DUPLICATE_USERNAME"))
                    .andExpect(jsonPath("$.status").value(409))
                    .andExpect(jsonPath("$.details.username").value("kim"));
        }
    }

    @Nested
    @DisplayName("PATCH /api/users/{userId}")
    class Update {

        @Test
        @DisplayName("성공 - 수정된 사용자 JSON을 반환한다")
        void update_success() throws Exception {
            UUID userId = UUID.randomUUID();
            given(userService.update(eq(userId), any(UserUpdateRequestDto.class), isNull()))
                    .willReturn(new UserDto(userId, "newKim", "kim@test.com", null, true));

            mockMvc.perform(multipart(HttpMethod.PATCH, "/api/users/{userId}", userId)
                            .file(jsonPart("userUpdateRequest", """
                                    {"newUsername": "newKim"}
                                    """)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("newKim"));
        }

        @Test
        @DisplayName("실패 - 없는 사용자면 404")
        void update_notFound_fail() throws Exception {
            UUID userId = UUID.randomUUID();
            given(userService.update(eq(userId), any(UserUpdateRequestDto.class), isNull()))
                    .willThrow(new UserNotFoundException(userId));

            mockMvc.perform(multipart(HttpMethod.PATCH, "/api/users/{userId}", userId)
                            .file(jsonPart("userUpdateRequest", """
                                    {"newUsername": "newKim"}
                                    """)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("USER_NOT_FOUND"))
                    .andExpect(jsonPath("$.details.userId").value(userId.toString()));
        }
    }

    @Nested
    @DisplayName("DELETE /api/users/{userId}")
    class Delete {

        @Test
        @DisplayName("성공 - 200")
        void delete_success() throws Exception {
            UUID userId = UUID.randomUUID();

            mockMvc.perform(delete("/api/users/{userId}", userId))
                    .andExpect(status().isOk());

            then(userService).should().delete(userId);
        }

        @Test
        @DisplayName("실패 - 없는 사용자면 404")
        void delete_notFound_fail() throws Exception {
            UUID userId = UUID.randomUUID();
            willThrow(new UserNotFoundException(userId)).given(userService).delete(userId);

            mockMvc.perform(delete("/api/users/{userId}", userId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.exceptionType").value("UserNotFoundException"));
        }
    }

    @Nested
    @DisplayName("GET /api/users")
    class FindAll {

        @Test
        @DisplayName("성공 - 사용자 목록 JSON 배열을 반환한다")
        void findAll_success() throws Exception {
            given(userService.findAll()).willReturn(List.of(
                    new UserDto(UUID.randomUUID(), "kim", "kim@test.com", null, true),
                    new UserDto(UUID.randomUUID(), "lee", "lee@test.com", null, false)));

            mockMvc.perform(get("/api/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[1].online").value(false));
        }
    }
}
