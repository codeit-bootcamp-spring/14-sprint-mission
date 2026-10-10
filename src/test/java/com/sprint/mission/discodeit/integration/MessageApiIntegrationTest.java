package com.sprint.mission.discodeit.integration;

import com.sprint.mission.discodeit.message.repository.MessageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("메시지 API 통합 테스트")
class MessageApiIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private MessageRepository messageRepository;

    @Nested
    @DisplayName("생성 POST /api/messages")
    class Create {

        @Test
        @DisplayName("성공 - 첨부파일과 함께 생성되고 DB에 저장된다")
        void create_success() throws Exception {
            UUID authorId = createUser("kim");
            UUID channelId = createPublicChannel("일반");

            MvcResult result = mockMvc.perform(multipart("/api/messages")
                            .file(jsonPart("messageCreateRequest", """
                                    {"content": "안녕하세요", "channelId": "%s", "authorId": "%s"}
                                    """.formatted(channelId, authorId)))
                            .file(new MockMultipartFile("attachments", "a.txt", "text/plain", "hello".getBytes())))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").value("안녕하세요"))
                    .andExpect(jsonPath("$.channelId").value(channelId.toString()))
                    .andExpect(jsonPath("$.author.username").value("kim"))
                    .andExpect(jsonPath("$.attachments.length()").value(1))
                    .andExpect(jsonPath("$.attachments[0].fileName").value("a.txt"))
                    .andReturn();

            assertThat(messageRepository.findById(idOf(result))).isPresent();
        }

        @Test
        @DisplayName("실패 - 없는 채널이면 404, 메시지가 저장되지 않는다")
        void create_channelNotFound_fail() throws Exception {
            UUID authorId = createUser("kim");

            mockMvc.perform(multipart("/api/messages")
                            .file(jsonPart("messageCreateRequest", """
                                    {"content": "안녕하세요", "channelId": "%s", "authorId": "%s"}
                                    """.formatted(UUID.randomUUID(), authorId))))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("CHANNEL_NOT_FOUND"));

            assertThat(messageRepository.findAll()).isEmpty();
        }

        @Test
        @DisplayName("실패 - 작성자가 없으면 400")
        void create_missingAuthor_fail() throws Exception {
            UUID channelId = createPublicChannel("일반");

            mockMvc.perform(multipart("/api/messages")
                            .file(jsonPart("messageCreateRequest", """
                                    {"content": "안녕하세요", "channelId": "%s"}
                                    """.formatted(channelId))))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.details.authorId").exists());
        }
    }

    @Nested
    @DisplayName("수정 PATCH /api/messages/{messageId}")
    class Update {

        @Test
        @DisplayName("성공 - 내용이 바뀌고 DB에 반영된다")
        void update_success() throws Exception {
            UUID messageId = createMessage(createPublicChannel("일반"), createUser("kim"), "원래 내용");

            mockMvc.perform(patch("/api/messages/{messageId}", messageId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"newContent": "수정된 내용"}
                                    """))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").value("수정된 내용"));

            assertThat(messageRepository.findById(messageId).orElseThrow().getContent()).isEqualTo("수정된 내용");
        }

        @Test
        @DisplayName("실패 - 없는 메시지면 404")
        void update_notFound_fail() throws Exception {
            mockMvc.perform(patch("/api/messages/{messageId}", UUID.randomUUID())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"newContent": "수정된 내용"}
                                    """))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("MESSAGE_NOT_FOUND"));
        }
    }

    @Nested
    @DisplayName("삭제 DELETE /api/messages/{messageId}")
    class Delete {

        @Test
        @DisplayName("성공 - 삭제 후 DB에서 사라진다")
        void delete_success() throws Exception {
            UUID messageId = createMessage(createPublicChannel("일반"), createUser("kim"), "지울 메시지");

            mockMvc.perform(delete("/api/messages/{messageId}", messageId))
                    .andExpect(status().isOk());

            assertThat(messageRepository.findById(messageId)).isEmpty();
        }

        @Test
        @DisplayName("실패 - 없는 메시지면 404")
        void delete_notFound_fail() throws Exception {
            mockMvc.perform(delete("/api/messages/{messageId}", UUID.randomUUID()))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.exceptionType").value("MessageNotFoundException"));
        }
    }

    @Nested
    @DisplayName("목록 조회 GET /api/messages?channelId=")
    class FindAllByChannelId {

        @Test
        @DisplayName("성공 - 채널의 메시지만 페이지 크기만큼 조회되고 다음 페이지 여부를 알려준다")
        void findAll_success() throws Exception {
            UUID authorId = createUser("kim");
            UUID channelId = createPublicChannel("일반");
            UUID otherChannelId = createPublicChannel("다른방");
            createMessage(channelId, authorId, "1");
            createMessage(channelId, authorId, "2");
            createMessage(channelId, authorId, "3");
            createMessage(otherChannelId, authorId, "다른 채널");

            mockMvc.perform(get("/api/messages")
                            .param("channelId", channelId.toString())
                            .param("size", "2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(2))
                    .andExpect(jsonPath("$.hasNext").value(true))
                    .andExpect(jsonPath("$.nextCursor").exists())
                    .andExpect(jsonPath("$.content[*].channelId")
                            .value(everyItem(is(channelId.toString()))));
        }

        @Test
        @DisplayName("성공(경계) - 메시지가 없는 채널이면 빈 목록과 다음 페이지 없음")
        void findAll_empty() throws Exception {
            UUID channelId = createPublicChannel("빈방");

            mockMvc.perform(get("/api/messages").param("channelId", channelId.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(0))
                    .andExpect(jsonPath("$.hasNext").value(false));
        }
    }
}
