package com.sprint.mission.discodeit.channel;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.channel.dto.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.channel.dto.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.entity.ChannelType;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

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

    @Test
    @DisplayName("공개 채널 생성 성공")
    @WithMockUser(roles = "CHANNEL_MANAGER")
    void createPublicChannel_success() throws Exception {
        String request = objectMapper.writeValueAsString(
            new ChannelPublicCreateRequestDto("공지", "공지 채널입니다"));

        mockMvc.perform(post("/api/channels/public")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("공지"));
    }

    @Test
    @DisplayName("공개 채널 생성 성공 - ADMIN은 역할 계층으로 CHANNEL_MANAGER 권한을 가진다")
    @WithMockUser(roles = "ADMIN")
    void createPublicChannel_success_admin() throws Exception {
        String request = objectMapper.writeValueAsString(
            new ChannelPublicCreateRequestDto("공지", "공지 채널입니다"));

        mockMvc.perform(post("/api/channels/public")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("공개 채널 생성 실패 - USER는 403")
    @WithMockUser(roles = "USER")
    void createPublicChannel_fail_forbidden() throws Exception {
        String request = objectMapper.writeValueAsString(
            new ChannelPublicCreateRequestDto("공지", "공지 채널입니다"));

        mockMvc.perform(post("/api/channels/public")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.code").value("ACCESS_DENIED"));
    }

    @Test
    @DisplayName("채널 삭제 - USER는 퍼블릭 채널은 403, 프라이빗 채널은 204")
    @WithMockUser(roles = "USER")
    void deleteChannel_byUser() throws Exception {
        Channel publicChannel = channelRepository.save(
            new Channel("공개", ChannelType.PUBLIC, "설명"));
        Channel privateChannel = channelRepository.save(new Channel(ChannelType.PRIVATE));

        mockMvc.perform(delete("/api/channels/{channelId}", publicChannel.getId()).with(csrf()))
            .andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/channels/{channelId}", privateChannel.getId()).with(csrf()))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("공개 채널 수정 성공")
    @WithMockUser(roles = "CHANNEL_MANAGER")
    void updateChannel_success() throws Exception {
        Channel channel = channelRepository.save(
            new Channel("이전이름", ChannelType.PUBLIC, "이전설명"));

        String request = objectMapper.writeValueAsString(
            new ChannelUpdateRequestDto("새이름", "새설명"));

        mockMvc.perform(patch("/api/channels/{channelId}", channel.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("새이름"));
    }
}
