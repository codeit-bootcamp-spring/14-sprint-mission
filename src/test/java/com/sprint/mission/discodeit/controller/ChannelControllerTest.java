package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.ErrorCodeStatusMapper;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@Import({ErrorCodeStatusMapper.class})
@WebMvcTest(ChannelController.class)
class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ChannelService channelService;

  @Test
  @DisplayName("POST /api/channels/public: 공개 채널 생성 성공 시 201")
  void createPublic_success() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    given(channelService.create(new PublicChannelCreateRequest("general", "desc"))).willReturn(
        new ChannelDto(channelId, ChannelType.PUBLIC, "general", "desc", List.of(), null));

    // when & then
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"name":"general","description":"desc"}
                """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(channelId.toString()))
        .andExpect(jsonPath("$.type").value("PUBLIC"))
        .andExpect(jsonPath("$.name").value("general"));
  }

  @Test
  @DisplayName("GET /api/channels?userId=: 사용자가 볼 수 있는 채널 목록")
  void findAll_success() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    given(channelService.findAllByUserId(userId)).willReturn(List.of(
        new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "general", null, List.of(), null)));

    // when & then
    mockMvc.perform(get("/api/channels").param("userId", userId.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].name").value("general"));
  }

  @Test
  @DisplayName("POST /api/channels/public: 빈 이름이면 400과 필드 오류를 반환한다")
  void createPublic_blankName() throws Exception {
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"name":" "}
                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("INVALID_INPUT_VALUE"))
        .andExpect(jsonPath("$.details.name").exists());
  }

  @Test
  @DisplayName("PATCH /api/channels/{id}: 비공개 채널 수정이면 ErrorResponse를 반환한다")
  void update_privateChannel() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    given(channelService.update(eq(channelId), any()))
        .willThrow(new PrivateChannelUpdateException(channelId));

    // when & then: 상태 코드는 ErrorCodeStatusMapper에서 정한 값으로 맞춘다
    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"newName":"renamed"}
                """))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value("PRIVATE_CHANNEL_UPDATE"))
        .andExpect(jsonPath("$.details.channelId").value(channelId.toString()));
  }
}
