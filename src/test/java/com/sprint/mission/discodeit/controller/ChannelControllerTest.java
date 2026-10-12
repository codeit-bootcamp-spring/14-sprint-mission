package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelController.class)
class ChannelControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ChannelService channelService;

    @Test
    @DisplayName("PUBLIC 채널 생성 성공 - 201")
    void createPublic_success() throws Exception {
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("공지", "공지 채널");
        given(channelService.createPublic(any(PublicChannelCreateRequest.class))).willReturn(
            new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "공지", "공지 채널", List.of(),
                null));

        mockMvc.perform(post("/api/channels/public")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.type").value("PUBLIC"))
            .andExpect(jsonPath("$.name").value("공지"));
    }

    @Test
    @DisplayName("PUBLIC 채널 생성 실패 - 이름이 비어 있으면 400")
    void createPublic_fail_blankName() throws Exception {
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("", null);

        mockMvc.perform(post("/api/channels/public")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("INVALID_INPUT"))
            .andExpect(jsonPath("$.details.name").exists());
    }
}
