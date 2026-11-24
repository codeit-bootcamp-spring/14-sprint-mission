package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("로그인 API 통합 테스트 - 성공")
    void login_Success() throws Exception {
        // Given
        UserCreateRequest userRequest = new UserCreateRequest(
            "loginuser",
            "login@example.com",
            "Password1!"
        );
        userService.create(userRequest, Optional.empty());

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "loginuser")
                .param("password", "Password1!"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.username", is("loginuser")))
            .andExpect(jsonPath("$.email", is("login@example.com")));
    }

    @Test
    @DisplayName("로그아웃 API 통합 테스트 - 성공")
    void logout_Success() throws Exception {
        // Given: 회원가입 후 로그인해서 유효한 세션 발급받기
        UserCreateRequest userRequest = new UserCreateRequest(
            "logoutuser",
            "logout@example.com",
            "Password1!"
        );
        userService.create(userRequest, Optional.empty());

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "logoutuser")
                .param("password", "Password1!"))
            .andExpect(status().isOk())
            .andReturn();

        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);

        // When: 발급받은 세션 + CSRF 토큰으로 로그아웃 요청
        mockMvc.perform(post("/api/auth/logout")
                .with(csrf())
                .session(session))
            .andExpect(status().isNoContent()); // 204 확인

        // Then: 서버 세션이 무효화되었고, 이후 /api/auth/me 호출 시 401 반환되는지 확인
        assertThat(session.isInvalid()).isTrue();
        mockMvc.perform(get("/api/auth/me")
                .session(session))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("로그인 API 통합 테스트 - 실패 (존재하지 않는 사용자)")
    void login_Failure_UserNotFound() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "nonexistentuser")
                .param("password", "Password1!"))
            .andExpect(status().isUnauthorized()) // 401 확인
            .andExpect(jsonPath("$.status", is(401))); // ErrorResponse의 status 필드 확인
    }

    @Test
    @DisplayName("로그인 API 통합 테스트 - 실패 (잘못된 비밀번호)")
    void login_Failure_InvalidCredentials() throws Exception {
        // Given
        UserCreateRequest userRequest = new UserCreateRequest(
            "loginuser2",
            "login2@example.com",
            "Password1!"
        );
        userService.create(userRequest, Optional.empty());
        // When & Then: 틀린 비밀번호 -> BadCredentialsException -> LoginFailureHandler(401)
        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "loginuser2")
                .param("password", "WrongPassword1!"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status", is(401)));
    }

    @Test
    @DisplayName("로그인 API 통합 테스트 - 실패 (유효하지 않은 요청)")
    void login_Failure_InvalidRequest() throws Exception {
        // When & Then: 빈 값으로 로그인 시도 -> 인증 실패 -> LoginFailureHandler(401)
        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "")
                .param("password", ""))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status", is(401)));
    }

    @Test
    @DisplayName("현재 사용자 정보 조회 통합 테스트 - 성공 (세션 유지)")
    void me_Success() throws Exception {
        // Given: 회원가입 후 formLogin으로 로그인하여 세션 발급받기
        UserCreateRequest userRequest = new UserCreateRequest(
            "meuser",
            "me@example.com",
            "Password1!"
        );
        userService.create(userRequest, Optional.empty());

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "meuser")
                .param("password", "Password1!"))
            .andExpect(status().isOk())
            .andReturn();

        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);

        // When & Then: 그 세션을 그대로 들고 GET /api/auth/me 호출 시 200 + UserDto 응답 확인
        mockMvc.perform(get("/api/auth/me")
                .session(session))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.username", is("meuser")))
            .andExpect(jsonPath("$.email", is("me@example.com")));
    }
} 