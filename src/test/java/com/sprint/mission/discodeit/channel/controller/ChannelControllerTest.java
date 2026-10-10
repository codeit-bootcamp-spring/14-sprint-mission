package com.sprint.mission.discodeit.channel.controller;

import com.sprint.mission.discodeit.channel.application.ChannelService;
import com.sprint.mission.discodeit.channel.domain.ChannelType;
import com.sprint.mission.discodeit.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.channel.dto.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.channel.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.common.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exception.PrivateChannelUpdateNotAllowedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChannelController.class)
class ChannelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChannelService channelService;

    @Nested
    @DisplayName("POST /api/channels/public")
    class CreatePublic {

        @Test
        @DisplayName("성공 - 200과 생성된 채널 JSON을 반환한다")
        void createPublic_success() throws Exception {
            // given
            UUID channelId = UUID.randomUUID();
            given(channelService.publicCreate(any(PublicChannelCreateRequest.class)))
                    .willReturn(new ChannelDto(channelId, ChannelType.PUBLIC, "공지", "공지 채널", List.of(), null));

            // when & then
            mockMvc.perform(post("/api/channels/public")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"name": "공지", "description": "공지 채널"}
                                    """))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(channelId.toString()))
                    .andExpect(jsonPath("$.type").value("PUBLIC"))
                    .andExpect(jsonPath("$.name").value("공지"));
        }

        @Test
        @DisplayName("실패 - 이름이 비어 있으면 400과 필드별 오류 메시지를 반환한다")
        void createPublic_blankName_fail() throws Exception {
            mockMvc.perform(post("/api/channels/public")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"name": "", "description": "공지 채널"}
                                    """))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("INVALID_INPUT"))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.exceptionType").value("MethodArgumentNotValidException"))
                    .andExpect(jsonPath("$.details.name").value("채널 이름은 필수입니다."));

            then(channelService).should(never()).publicCreate(any());
        }
    }

    @Nested
    @DisplayName("POST /api/channels/private")
    class CreatePrivate {

        @Test
        @DisplayName("성공 - 참여자 목록으로 PRIVATE 채널을 생성한다")
        void createPrivate_success() throws Exception {
            // given
            UUID userId = UUID.randomUUID();
            given(channelService.privateCreate(any(PrivateChannelCreateRequest.class)))
                    .willReturn(new ChannelDto(UUID.randomUUID(), ChannelType.PRIVATE, null, null, List.of(), null));

            // when & then
            mockMvc.perform(post("/api/channels/private")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"participantIds\": [\"" + userId + "\"]}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.type").value("PRIVATE"));
        }

        @Test
        @DisplayName("실패 - 참여자가 비어 있으면 400")
        void createPrivate_emptyParticipants_fail() throws Exception {
            mockMvc.perform(post("/api/channels/private")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"participantIds": []}
                                    """))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.details.participantIds").exists());
        }
    }

    @Nested
    @DisplayName("PATCH /api/channels/{channelId}")
    class Update {

        @Test
        @DisplayName("성공 - 수정된 채널 JSON을 반환한다")
        void update_success() throws Exception {
            // given
            UUID channelId = UUID.randomUUID();
            given(channelService.update(eq(channelId), any(ChannelUpdateRequestDto.class)))
                    .willReturn(new ChannelDto(channelId, ChannelType.PUBLIC, "새이름", null, List.of(), null));

            // when & then
            mockMvc.perform(patch("/api/channels/{channelId}", channelId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"newName": "새이름"}
                                    """))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("새이름"));
        }

        @Test
        @DisplayName("실패 - PRIVATE 채널이면 403과 ErrorResponse를 반환한다")
        void update_privateChannel_fail() throws Exception {
            // given
            UUID channelId = UUID.randomUUID();
            given(channelService.update(eq(channelId), any(ChannelUpdateRequestDto.class)))
                    .willThrow(new PrivateChannelUpdateNotAllowedException(channelId));

            // when & then
            mockMvc.perform(patch("/api/channels/{channelId}", channelId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"newName": "새이름"}
                                    """))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.code").value("PRIVATE_CHANNEL_UPDATE_NOT"))
                    .andExpect(jsonPath("$.exceptionType").value("PrivateChannelUpdateNotAllowedException"))
                    .andExpect(jsonPath("$.details.channelId").value(channelId.toString()));
        }
    }

    @Nested
    @DisplayName("DELETE /api/channels/{channelId}")
    class Delete {

        @Test
        @DisplayName("성공 - 200")
        void delete_success() throws Exception {
            UUID channelId = UUID.randomUUID();

            mockMvc.perform(delete("/api/channels/{channelId}", channelId))
                    .andExpect(status().isOk());

            then(channelService).should().delete(channelId);
        }

        @Test
        @DisplayName("실패 - 없는 채널이면 404")
        void delete_notFound_fail() throws Exception {
            UUID channelId = UUID.randomUUID();
            willThrow(new ChannelNotFoundException(channelId)).given(channelService).delete(channelId);

            mockMvc.perform(delete("/api/channels/{channelId}", channelId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("CHANNEL_NOT_FOUND"));
        }
    }

    @Nested
    @DisplayName("GET /api/channels?userId=")
    class FindAllByUserId {

        @Test
        @DisplayName("성공 - 채널 목록 JSON 배열을 반환한다")
        void findAll_success() throws Exception {
            UUID userId = UUID.randomUUID();
            given(channelService.findAllByUserId(userId)).willReturn(List.of(
                    new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "공지", null, List.of(), null),
                    new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "잡담", null, List.of(), null)));

            mockMvc.perform(get("/api/channels").param("userId", userId.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].name").value("공지"));
        }
    }
}
