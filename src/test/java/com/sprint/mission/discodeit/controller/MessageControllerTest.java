package com.sprint.mission.discodeit.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.exception.ErrorCodeStatusMapper;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.service.MessageService;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@Import({ErrorCodeStatusMapper.class})
@WebMvcTest(MessageController.class)
class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private MessageService messageService;

  @Test
  @DisplayName("POST /api/messages: 첨부 파일과 함께 메시지를 생성하면 201")
  void create_success() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    MessageDto created = new MessageDto(UUID.randomUUID(), Instant.now(), null, "hello",
        channelId, null, List.of());
    // 첫 인자는 record라 equals로 정확히 비교한다. 첨부 목록은 byte[]를 담고 있어
    // record equals가 배열을 참조로 비교하므로 any()로 둔다.
    given(messageService.create(eq(new MessageCreateRequest("hello", channelId, authorId)), any()))
        .willReturn(created);

    MockMultipartFile request = new MockMultipartFile("messageCreateRequest", "",
        MediaType.APPLICATION_JSON_VALUE,
        """
            {"content":"hello","channelId":"%s","authorId":"%s"}
            """.formatted(channelId, authorId).getBytes(StandardCharsets.UTF_8));
    MockMultipartFile attachment = new MockMultipartFile("attachments", "a.txt",
        MediaType.TEXT_PLAIN_VALUE, "abc".getBytes(StandardCharsets.UTF_8));

    // when & then
    mockMvc.perform(multipart("/api/messages").file(request).file(attachment))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.content").value("hello"))
        .andExpect(jsonPath("$.channelId").value(channelId.toString()));
  }

  @Test
  @DisplayName("PATCH /api/messages/{id}: 메시지 수정 성공")
  void update_success() throws Exception {
    // given
    UUID messageId = UUID.randomUUID();
    given(messageService.update(messageId, new MessageUpdateRequest("after"))).willReturn(
        new MessageDto(messageId, Instant.now(), Instant.now(), "after", UUID.randomUUID(), null,
            List.of()));

    // when & then
    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"newContent":"after"}
                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").value("after"));
  }

  @Test
  @DisplayName("GET /api/messages?channelId=: 기본 페이지는 size 50, createdAt 내림차순")
  void findAllByChannelId_defaultPageable() throws Exception {
    // given
    UUID channelId = UUID.randomUUID();
    given(messageService.findAllByChannelId(eq(channelId), isNull(), any(Pageable.class)))
        .willReturn(new PageResponse<>(List.of(), null, 50, false, null));

    // when & then
    mockMvc.perform(get("/api/messages").param("channelId", channelId.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isEmpty())
        .andExpect(jsonPath("$.hasNext").value(false));

    ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
    then(messageService).should().findAllByChannelId(eq(channelId), isNull(), captor.capture());
    assertThat(captor.getValue().getPageSize()).isEqualTo(50);
    assertThat(captor.getValue().getSort().getOrderFor("createdAt").getDirection())
        .isEqualTo(Sort.Direction.DESC);
  }

  @Test
  @DisplayName("PATCH /api/messages/{id}: 없는 메시지면 404")
  void update_messageNotFound() throws Exception {
    // given
    UUID messageId = UUID.randomUUID();
    given(messageService.update(eq(messageId), any()))
        .willThrow(new MessageNotFoundException(messageId));

    // when & then
    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"newContent":"after"}
                """))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("MESSAGE_NOT_FOUND"));
  }

  @Test
  @DisplayName("PATCH /api/messages/{id}: 빈 내용이면 400")
  void update_blankContent() throws Exception {
    mockMvc.perform(patch("/api/messages/{messageId}", UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"newContent":""}
                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("INVALID_INPUT_VALUE"));
  }
}
