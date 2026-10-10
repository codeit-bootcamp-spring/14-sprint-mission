package com.sprint.mission.discodeit.message.controller;

import com.sprint.mission.discodeit.common.dto.PageResponse;
import com.sprint.mission.discodeit.common.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.message.application.MessageService;
import com.sprint.mission.discodeit.message.dto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.message.dto.MessageDto;
import com.sprint.mission.discodeit.message.dto.MessageUpdateRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MessageService messageService;

    private MockMultipartFile jsonPart(String json) {
        return new MockMultipartFile("messageCreateRequest", "", MediaType.APPLICATION_JSON_VALUE,
                json.getBytes(StandardCharsets.UTF_8));
    }

    private MessageDto messageDto(UUID id, String content, UUID channelId) {
        return new MessageDto(id, Instant.parse("2026-10-06T10:00:00Z"), null, content, channelId, null, List.of());
    }

    @Nested
    @DisplayName("POST /api/messages")
    class Create {

        @Test
        @DisplayName("성공 - 첨부파일과 함께 메시지를 생성하고 JSON을 반환한다")
        void create_success() throws Exception {
            // given
            UUID channelId = UUID.randomUUID();
            UUID authorId = UUID.randomUUID();
            UUID messageId = UUID.randomUUID();
            given(messageService.create(any(MessageCreateRequestDto.class), any()))
                    .willReturn(messageDto(messageId, "안녕하세요", channelId));

            // when & then
            mockMvc.perform(multipart("/api/messages")
                            .file(jsonPart("{\"content\": \"안녕하세요\", \"channelId\": \"" + channelId
                                    + "\", \"authorId\": \"" + authorId + "\"}"))
                            .file(new MockMultipartFile("attachments", "a.txt", "text/plain", "a".getBytes())))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(messageId.toString()))
                    .andExpect(jsonPath("$.content").value("안녕하세요"))
                    .andExpect(jsonPath("$.channelId").value(channelId.toString()));
        }

        @Test
        @DisplayName("실패 - channelId가 없으면 400")
        void create_missingChannelId_fail() throws Exception {
            mockMvc.perform(multipart("/api/messages")
                            .file(jsonPart("{\"content\": \"안녕하세요\", \"authorId\": \"" + UUID.randomUUID() + "\"}")))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.details.channelId").value("channelId는 필수입니다."));

            then(messageService).should(never()).create(any(), any());
        }

        @Test
        @DisplayName("실패 - 없는 채널이면 404")
        void create_channelNotFound_fail() throws Exception {
            UUID channelId = UUID.randomUUID();
            given(messageService.create(any(MessageCreateRequestDto.class), isNull()))
                    .willThrow(new ChannelNotFoundException(channelId));

            mockMvc.perform(multipart("/api/messages")
                            .file(jsonPart("{\"content\": \"안녕하세요\", \"channelId\": \"" + channelId
                                    + "\", \"authorId\": \"" + UUID.randomUUID() + "\"}")))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("CHANNEL_NOT_FOUND"));
        }
    }

    @Nested
    @DisplayName("PATCH /api/messages/{messageId}")
    class Update {

        @Test
        @DisplayName("성공 - 수정된 메시지 JSON을 반환한다")
        void update_success() throws Exception {
            UUID messageId = UUID.randomUUID();
            given(messageService.update(eq(messageId), any(MessageUpdateRequestDto.class)))
                    .willReturn(messageDto(messageId, "수정됨", UUID.randomUUID()));

            mockMvc.perform(patch("/api/messages/{messageId}", messageId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"newContent": "수정됨"}
                                    """))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").value("수정됨"));
        }

        @Test
        @DisplayName("실패 - 수정 내용이 비어 있으면 400")
        void update_blankContent_fail() throws Exception {
            mockMvc.perform(patch("/api/messages/{messageId}", UUID.randomUUID())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"newContent": " "}
                                    """))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.details.newContent").exists());
        }

        @Test
        @DisplayName("실패 - 없는 메시지면 404")
        void update_notFound_fail() throws Exception {
            UUID messageId = UUID.randomUUID();
            given(messageService.update(eq(messageId), any(MessageUpdateRequestDto.class)))
                    .willThrow(new MessageNotFoundException(messageId));

            mockMvc.perform(patch("/api/messages/{messageId}", messageId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"newContent": "수정됨"}
                                    """))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("MESSAGE_NOT_FOUND"))
                    .andExpect(jsonPath("$.details.messageId").value(messageId.toString()));
        }
    }

    @Nested
    @DisplayName("DELETE /api/messages/{messageId}")
    class Delete {

        @Test
        @DisplayName("성공 - 200")
        void delete_success() throws Exception {
            UUID messageId = UUID.randomUUID();

            mockMvc.perform(delete("/api/messages/{messageId}", messageId))
                    .andExpect(status().isOk());

            then(messageService).should().delete(messageId);
        }

        @Test
        @DisplayName("실패 - 없는 메시지면 404")
        void delete_notFound_fail() throws Exception {
            UUID messageId = UUID.randomUUID();
            willThrow(new MessageNotFoundException(messageId)).given(messageService).delete(messageId);

            mockMvc.perform(delete("/api/messages/{messageId}", messageId))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/messages?channelId=")
    class FindAllByChannelId {

        @Test
        @DisplayName("성공 - 페이지 응답 JSON(content, nextCursor, hasNext)을 반환한다")
        void findAll_success() throws Exception {
            // given
            UUID channelId = UUID.randomUUID();
            Instant nextCursor = Instant.parse("2026-10-06T10:00:00Z");
            given(messageService.findAllByChannelId(eq(channelId), isNull(), any(Pageable.class)))
                    .willReturn(new PageResponse<>(
                            List.of(messageDto(UUID.randomUUID(), "최근 메시지", channelId)),
                            nextCursor, 50, true, null));

            // when & then
            mockMvc.perform(get("/api/messages").param("channelId", channelId.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(1))
                    .andExpect(jsonPath("$.content[0].content").value("최근 메시지"))
                    .andExpect(jsonPath("$.hasNext").value(true))
                    .andExpect(jsonPath("$.nextCursor").exists());
        }

        @Test
        @DisplayName("성공 - 커서를 넘기면 서비스에 그대로 전달한다")
        void findAll_withCursor() throws Exception {
            UUID channelId = UUID.randomUUID();
            Instant cursor = Instant.parse("2026-10-06T09:00:00Z");
            given(messageService.findAllByChannelId(eq(channelId), eq(cursor), any(Pageable.class)))
                    .willReturn(new PageResponse<>(List.of(), null, 50, false, null));

            mockMvc.perform(get("/api/messages")
                            .param("channelId", channelId.toString())
                            .param("cursor", cursor.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty())
                    .andExpect(jsonPath("$.hasNext").value(false));
        }
    }
}
