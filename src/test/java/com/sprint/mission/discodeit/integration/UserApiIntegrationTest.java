package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    private MockMultipartFile jsonPart(String name, Object body) throws Exception {
        return new MockMultipartFile(name, "", MediaType.APPLICATION_JSON_VALUE,
            objectMapper.writeValueAsBytes(body));
    }

    private UserDto createUser(String username) throws Exception {
        String json = mockMvc.perform(multipart("/api/users").file(jsonPart("userCreateRequest",
                new UserCreateRequest(username, username + "@test.com", "1234"))))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(json, UserDto.class);
    }

    @Test
    @DisplayName("사용자 생성 성공 - 프로필 이미지와 함께 저장된다")
    void create_success() throws Exception {
        MockMultipartFile profile = new MockMultipartFile("profile", "me.png",
            MediaType.IMAGE_PNG_VALUE, new byte[]{1, 2, 3});

        mockMvc.perform(multipart("/api/users")
                .file(jsonPart("userCreateRequest",
                    new UserCreateRequest("tester", "tester@test.com", "1234")))
                .file(profile))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value("tester"))
            .andExpect(jsonPath("$.online").value(true))
            .andExpect(jsonPath("$.profile.fileName").value("me.png"))
            .andExpect(jsonPath("$.profile.size").value(3));

        assertThat(userRepository.existsByUsername("tester")).isTrue();
    }

    @Test
    @DisplayName("사용자 생성 실패 - 같은 username이 있으면 409")
    void create_fail_duplicate() throws Exception {
        createUser("tester");

        mockMvc.perform(multipart("/api/users").file(jsonPart("userCreateRequest",
                new UserCreateRequest("tester", "other@test.com", "1234"))))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.code").value("DUPLICATE_USER"))
            .andExpect(jsonPath("$.details.username").value("tester"));
    }

    @Test
    @DisplayName("사용자 수정 성공")
    void update_success() throws Exception {
        UserDto user = createUser("before");

        mockMvc.perform(multipart(HttpMethod.PATCH, "/api/users/{userId}", user.id())
                .file(jsonPart("userUpdateRequest", new UserUpdateRequest("after", null, null))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("after"));

        assertThat(userRepository.findById(user.id()).orElseThrow().getUsername())
            .isEqualTo("after");
    }

    @Test
    @DisplayName("사용자 삭제 성공")
    void delete_success() throws Exception {
        UserDto user = createUser("tester");

        mockMvc.perform(delete("/api/users/{userId}", user.id()))
            .andExpect(status().isNoContent());

        assertThat(userRepository.findById(user.id())).isEmpty();
    }

    @Test
    @DisplayName("사용자 목록 조회 - 만든 사용자들이 모두 나온다")
    void findAll_success() throws Exception {
        createUser("a");
        createUser("b");

        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    }
}
