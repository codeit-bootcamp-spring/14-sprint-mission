package com.sprint.mission.discodeit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ChannelIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChannelRepository channelRepository;

    @Test
    @DisplayName("채널생성 API를 호출하고 채널을 생성한다")
    void success_newChannel() throws Exception {
        PublicChannelCreateRequest request = new PublicChannelCreateRequest(
            "테스트 채널",
            "테스트 채널입니다"
        );

        mockMvc.perform(
                post("/api/channels/public")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("테스트 채널"));

        assertTrue(channelRepository.count() == 1);
    }

    @Test
    @DisplayName("채널삭제 API를 호출하고 채널을 삭제를한다")
    void success_deleteChannel() throws Exception {
        Channel channel = channelRepository.saveAndFlush(
            new Channel(
                ChannelType.PUBLIC,
                "테스트 채널",
                "테스트 채널입니다"
            )
        );

        mockMvc.perform(delete("/api/channels/{channelId}", channel.getId()))
            .andExpect(status().isNoContent());

        assertTrue(channelRepository.findById(channel.getId()).isEmpty());
    }

}
