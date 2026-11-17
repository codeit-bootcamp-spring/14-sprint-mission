package com.sprint.mission.discodeit.integration;

import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 통합 테스트 공통 설정.
 * - 실제 애플리케이션 컨텍스트 전체를 H2(test 프로파일) 위에 띄운다.
 * - MockMvc 요청은 테스트와 같은 스레드에서 실행되므로 @Transactional로 테스트마다 롤백된다.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
abstract class IntegrationTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    protected MockMultipartFile jsonPart(String name, String json) {
        return new MockMultipartFile(name, "", MediaType.APPLICATION_JSON_VALUE, json.getBytes(StandardCharsets.UTF_8));
    }

    protected UUID idOf(MvcResult result) throws Exception {
        String body = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        return UUID.fromString(JsonPath.read(body, "$.id"));
    }

    // ===== 다른 API 테스트의 사전 데이터를 실제 API로 만든다 =====

    protected UUID createUser(String username) throws Exception {
        MvcResult result = mockMvc.perform(multipart("/api/users")
                        .file(jsonPart("userCreateRequest", """
                                {"username": "%s", "email": "%s@test.com", "password": "password1234"}
                                """.formatted(username, username))))
                .andExpect(status().isCreated())
                .andReturn();
        return idOf(result);
    }

    protected UUID createPublicChannel(String name) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/channels/public")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "%s", "description": "설명"}
                                """.formatted(name)))
                .andExpect(status().isOk())
                .andReturn();
        return idOf(result);
    }

    protected UUID createMessage(UUID channelId, UUID authorId, String content) throws Exception {
        MvcResult result = mockMvc.perform(multipart("/api/messages")
                        .file(jsonPart("messageCreateRequest", """
                                {"content": "%s", "channelId": "%s", "authorId": "%s"}
                                """.formatted(content, channelId, authorId))))
                .andExpect(status().isOk())
                .andReturn();
        return idOf(result);
    }
}
