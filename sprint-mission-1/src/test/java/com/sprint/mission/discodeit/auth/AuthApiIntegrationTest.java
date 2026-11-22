package com.sprint.mission.discodeit.auth;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthApiIntegrationTest {

    private static final String USERNAME = "kyj";
    private static final String PASSWORD = "pw1234";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(
            User.create(USERNAME, passwordEncoder.encode(PASSWORD), "kyj@test.com", null));
    }

    @Test
    @DisplayName("CSRF 토큰 발급 - 203과 XSRF-TOKEN 쿠키")
    // 다른 테스트의 csrf()가 공유 컨텍스트의 CsrfFilter 저장소를 TestCsrfTokenRepository로 바꿔
    // 쿠키가 안 써진다. 실제 CookieCsrfTokenRepository로 검증하려고 새 컨텍스트에서 실행한다
    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    void csrfToken() throws Exception {
        mockMvc.perform(get("/api/auth/csrf-token"))
            .andExpect(status().isNonAuthoritativeInformation())
            .andExpect(cookie().exists("XSRF-TOKEN"));
    }

    @Test
    @DisplayName("로그인 성공 - 200과 UserDto, 세션으로 /me 조회")
    void login_success() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .param("username", USERNAME)
                .param("password", PASSWORD))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value(USERNAME))
            .andExpect(jsonPath("$.role").value("USER"))
            // 세션 등록 후 조회하므로 로그인 응답부터 접속 중으로 나온다
            .andExpect(jsonPath("$.online").value(true))
            .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);

        mockMvc.perform(get("/api/auth/me").session(session))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(user.getId().toString()));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호가 틀리면 401")
    void login_fail_wrongPassword() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .param("username", USERNAME)
                .param("password", "wrong"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value("LOGIN_UNAUTHORIZED"));
    }

    @Test
    @DisplayName("로그인 실패 - CSRF 토큰이 없으면 403")
    void login_fail_noCsrf() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .param("username", USERNAME)
                .param("password", PASSWORD))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("미인증 요청은 401 JSON")
    void unauthenticated() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value("AUTH_REQUIRED"));
    }

    @Test
    @DisplayName("권한 변경 실패 - USER는 403")
    @WithMockUser(roles = "USER")
    void updateRole_fail_forbidden() throws Exception {
        mockMvc.perform(put("/api/auth/role")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(roleRequest("ADMIN")))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.code").value("ACCESS_DENIED"));
    }

    @Test
    @DisplayName("권한 변경 성공 - ADMIN은 200")
    @WithMockUser(roles = "ADMIN")
    void updateRole_success() throws Exception {
        mockMvc.perform(put("/api/auth/role")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(roleRequest("CHANNEL_MANAGER")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.role").value("CHANNEL_MANAGER"));
    }

    @Test
    @DisplayName("actuator - health는 공개, loggers는 미인증 401")
    void actuator() throws Exception {
        mockMvc.perform(get("/actuator/health"))
            .andExpect(status().isOk());
        mockMvc.perform(get("/actuator/loggers"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("actuator - USER는 loggers에 접근할 수 없다")
    @WithMockUser(roles = "USER")
    void actuator_forbiddenForUser() throws Exception {
        mockMvc.perform(get("/actuator/loggers"))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("권한 변경 - 대상 사용자의 기존 세션은 만료되어 401")
    void updateRole_expiresTargetSession() throws Exception {
        MockHttpSession session = login();

        mockMvc.perform(put("/api/auth/role")
                .with(user("admin").roles("ADMIN"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(roleRequest("CHANNEL_MANAGER")))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/auth/me").session(session))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value("SESSION_EXPIRED"));
    }

    @Test
    @DisplayName("동시 세션 1개 - 새로 로그인하면 이전 세션이 만료된다")
    void concurrentLogin_expiresPreviousSession() throws Exception {
        MockHttpSession first = login();
        MockHttpSession second = login();

        mockMvc.perform(get("/api/auth/me").session(first))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value("SESSION_EXPIRED"));
        mockMvc.perform(get("/api/auth/me").session(second))
            .andExpect(status().isOk());
    }

    private MockHttpSession login() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .param("username", USERNAME)
                .param("password", PASSWORD))
            .andExpect(status().isOk())
            .andReturn();
        return (MockHttpSession) result.getRequest().getSession(false);
    }

    private String roleRequest(String newRole) {
        return "{\"userId\":\"" + user.getId() + "\",\"newRole\":\"" + newRole + "\"}";
    }
}
