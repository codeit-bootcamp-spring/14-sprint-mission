package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ChannelApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private UserRepository userRepository;

    private ChannelDto createPublicChannel(String name) throws Exception {
        String json = mockMvc.perform(post("/api/channels/public")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    new PublicChannelCreateRequest(name, "설명"))))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(json, ChannelDto.class);
    }

    @Test
    @DisplayName("PUBLIC 채널 생성 성공")
    void createPublic_success() throws Exception {
        ChannelDto channel = createPublicChannel("공지");

        assertThat(channel.type().name()).isEqualTo("PUBLIC");
        assertThat(channelRepository.findById(channel.id())).isPresent();
    }

    @Test
    @DisplayName("채널 수정 성공")
    void update_success() throws Exception {
        ChannelDto channel = createPublicChannel("before");

        mockMvc.perform(patch("/api/channels/{channelId}", channel.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    new PublicChannelUpdateRequest("after", "새 설명"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("after"))
            .andExpect(jsonPath("$.description").value("새 설명"));
    }

    @Test
    @DisplayName("채널 삭제 성공")
    void delete_success() throws Exception {
        ChannelDto channel = createPublicChannel("삭제할 채널");

        mockMvc.perform(delete("/api/channels/{channelId}", channel.id()))
            .andExpect(status().isNoContent());

        assertThat(channelRepository.findById(channel.id())).isEmpty();
    }
}
