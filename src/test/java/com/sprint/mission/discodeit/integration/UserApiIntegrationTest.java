package com.sprint.mission.discodeit.integration;

import com.sprint.mission.discodeit.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("사용자 API 통합 테스트")
class UserApiIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Nested
    @DisplayName("생성 POST /api/users")
    class Create {

        @Test
        @DisplayName("성공 - 프로필 이미지와 함께 생성되고 DB에 저장된다")
        void create_success() throws Exception {
            mockMvc.perform(multipart("/api/users")
                            .file(jsonPart("userCreateRequest", """
                                    {"username": "kim", "email": "kim@test.com", "password": "password1234"}
                                    """))
                            .file(new MockMultipartFile("profile", "profile.png", "image/png", new byte[]{1, 2, 3})))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.username").value("kim"))
                    .andExpect(jsonPath("$.profile.fileName").value("profile.png"))
                    .andExpect(jsonPath("$.online").value(true))
                    .andExpect(jsonPath("$.password").doesNotExist());

            assertThat(userRepository.findByUserName("kim")).isPresent();
        }

        @Test
        @DisplayName("실패 - 이미 존재하는 username이면 409")
        void create_duplicateUsername_fail() throws Exception {
            createUser("kim");

            mockMvc.perform(multipart("/api/users")
                            .file(jsonPart("userCreateRequest", """
                                    {"username": "kim", "email": "other@test.com", "password": "password1234"}
                                    """)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.code").value("DUPLICATE_USERNAME"));
        }

        @Test
        @DisplayName("실패 - 필수값이 없으면 400")
        void create_invalidInput_fail() throws Exception {
            mockMvc.perform(multipart("/api/users")
                            .file(jsonPart("userCreateRequest", """
                                    {"username": "", "email": "kim@test.com", "password": "password1234"}
                                    """)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.details.username").exists());

            assertThat(userRepository.findAll()).isEmpty();
        }
    }

    @Nested
    @DisplayName("수정 PATCH /api/users/{userId}")
    class Update {

        @Test
        @DisplayName("성공 - 보낸 값만 바뀌고 DB에 반영된다")
        void update_success() throws Exception {
            UUID userId = createUser("kim");

            mockMvc.perform(multipart(HttpMethod.PATCH, "/api/users/{userId}", userId)
                            .file(jsonPart("userUpdateRequest", """
                                    {"newUsername": "newKim"}
                                    """)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("newKim"))
                    .andExpect(jsonPath("$.email").value("kim@test.com"));

            assertThat(userRepository.findById(userId).orElseThrow().getUserName()).isEqualTo("newKim");
        }

        @Test
        @DisplayName("실패 - 없는 사용자면 404")
        void update_notFound_fail() throws Exception {
            mockMvc.perform(multipart(HttpMethod.PATCH, "/api/users/{userId}", UUID.randomUUID())
                            .file(jsonPart("userUpdateRequest", """
                                    {"newUsername": "newKim"}
                                    """)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("USER_NOT_FOUND"));
        }
    }

    @Nested
    @DisplayName("삭제 DELETE /api/users/{userId}")
    class Delete {

        @Test
        @DisplayName("성공 - 삭제 후 목록에서 사라진다")
        void delete_success() throws Exception {
            UUID userId = createUser("kim");

            mockMvc.perform(delete("/api/users/{userId}", userId))
                    .andExpect(status().isOk());

            assertThat(userRepository.findById(userId)).isEmpty();
        }

        @Test
        @DisplayName("실패 - 없는 사용자면 404")
        void delete_notFound_fail() throws Exception {
            mockMvc.perform(delete("/api/users/{userId}", UUID.randomUUID()))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.exceptionType").value("UserNotFoundException"));
        }
    }

    @Nested
    @DisplayName("목록 조회 GET /api/users")
    class FindAll {

        @Test
        @DisplayName("성공 - 생성한 사용자들이 모두 조회된다")
        void findAll_success() throws Exception {
            createUser("kim");
            createUser("lee");

            mockMvc.perform(get("/api/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[*].username").value(containsInAnyOrder("kim", "lee")));
        }

        @Test
        @DisplayName("성공(경계) - 사용자가 없으면 빈 배열")
        void findAll_empty() throws Exception {
            mockMvc.perform(get("/api/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }
    }
}
