package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private UserStatusService userStatusService;

    private MockMultipartFile jsonPart(String name, Object body) throws Exception {
        return new MockMultipartFile(name, "", MediaType.APPLICATION_JSON_VALUE,
            objectMapper.writeValueAsBytes(body));
    }

    @Test
    @DisplayName("사용자 생성 성공 - 201과 생성된 사용자 JSON")
    void create_success() throws Exception {
        UserCreateRequest request = new UserCreateRequest("tester", "tester@test.com", "1234");
        UserDto response = new UserDto(UUID.randomUUID(), "tester", "tester@test.com", null, true);
        given(userService.create(any(UserCreateRequest.class), isNull())).willReturn(response);

        mockMvc.perform(multipart("/api/users").file(jsonPart("userCreateRequest", request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value("tester"))
            .andExpect(jsonPath("$.email").value("tester@test.com"))
            .andExpect(jsonPath("$.online").value(true));
    }

    @Test
    @DisplayName("사용자 생성 실패 - email 형식이 틀리면 400과 상세 오류")
    void create_fail_invalidEmail() throws Exception {
        UserCreateRequest request = new UserCreateRequest("tester", "not-email", "1234");

        mockMvc.perform(multipart("/api/users").file(jsonPart("userCreateRequest", request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("INVALID_INPUT"))
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.exceptionType").value("MethodArgumentNotValidException"))
            .andExpect(jsonPath("$.details.email").exists());
    }
}
