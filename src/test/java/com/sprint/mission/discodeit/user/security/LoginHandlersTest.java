package com.sprint.mission.discodeit.user.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.user.application.UserApplicationService;
import com.sprint.mission.discodeit.user.domain.entity.Role;
import com.sprint.mission.discodeit.user.web.dto.res.UserResponseDTO;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

class LoginHandlersTest {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void successReturnsUserInfo() throws Exception {
        UUID id = UUID.randomUUID();
        UserApplicationService service = mock(UserApplicationService.class);
        UserResponseDTO user = new UserResponseDTO(id, "june", "june@example.com", Role.USER, null, true);
        when(service.completeLogin(id)).thenReturn(user);
        DiscodeitUserDetails principal = DiscodeitUserDetails.init(SecurityUserInfo.init(id, "june"), "hash");
        var authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        var response = new MockHttpServletResponse();

        new LoginSuccessHandler(service, objectMapper)
            .onAuthenticationSuccess(new MockHttpServletRequest(), response, authentication);

        assertEquals(200, response.getStatus());
        assertEquals(id.toString(), objectMapper.readTree(response.getContentAsByteArray()).get("id").asText());
    }

    @Test
    void failureReturnsUnauthorizedErrorResponse() throws Exception {
        var response = new MockHttpServletResponse();

        new LoginFailureHandler(objectMapper).onAuthenticationFailure(
            new MockHttpServletRequest(), response, new BadCredentialsException("bad credentials"));

        assertEquals(401, response.getStatus());
        var body = objectMapper.readTree(response.getContentAsByteArray());
        assertEquals("AUTHENTICATION_FAILED", body.get("code").asText());
        assertEquals(401, body.get("status").asInt());
    }
}
