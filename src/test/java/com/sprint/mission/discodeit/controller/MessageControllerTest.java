package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MessageService messageService;

    private final UUID channelId = UUID.randomUUID();
    private final UUID authorId = UUID.randomUUID();

    private MessageDto messageDto(UUID id, String content) {
        return new MessageDto(id, Instant.now(), Instant.now(), content, channelId, null,
            List.of());
    }

    @Test
    @DisplayName("메시지 생성 성공 - 첨부파일과 함께 201")
    void create_success() throws Exception {
        MessageCreateRequest request = new MessageCreateRequest("안녕", channelId, authorId);
        MockMultipartFile json = new MockMultipartFile("messageCreateRequest", "",
            MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request));
        MockMultipartFile file = new MockMultipartFile("attachments", "a.txt",
            MediaType.TEXT_PLAIN_VALUE, "hello".getBytes());
        given(messageService.create(any(MessageCreateRequest.class), anyList()))
            .willReturn(messageDto(UUID.randomUUID(), "안녕"));

        mockMvc.perform(multipart("/api/messages").file(json).file(file))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.content").value("안녕"))
            .andExpect(jsonPath("$.channelId").value(channelId.toString()));
    }

    @Test
    @DisplayName("메시지 생성 실패 - channelId가 없으면 400")
    void create_fail_noChannelId() throws Exception {
        MessageCreateRequest request = new MessageCreateRequest("안녕", null, authorId);
        MockMultipartFile json = new MockMultipartFile("messageCreateRequest", "",
            MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request));

        mockMvc.perform(multipart("/api/messages").file(json))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("INVALID_INPUT"))
            .andExpect(jsonPath("$.details.channelId").exists());
    }
}
