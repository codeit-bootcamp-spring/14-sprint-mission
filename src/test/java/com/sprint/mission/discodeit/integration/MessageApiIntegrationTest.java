package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class MessageApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private MessageRepository messageRepository;

    private User author;
    private Channel channel;

    @BeforeEach
    void setUp() {
        author = userRepository.save(new User("tester", "tester@test.com", "1234", null));
        channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "채널", null));
    }

    private MockMultipartFile requestPart(MessageCreateRequest request) throws Exception {
        return new MockMultipartFile("messageCreateRequest", "", MediaType.APPLICATION_JSON_VALUE,
            objectMapper.writeValueAsBytes(request));
    }

    private MessageDto createMessage(String content) throws Exception {
        String json = mockMvc.perform(multipart("/api/messages").file(requestPart(
                new MessageCreateRequest(content, channel.getId(), author.getId()))))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(json, MessageDto.class);
    }

    @Test
    @DisplayName("메시지 생성 성공 - 첨부파일 포함")
    void create_success() throws Exception {
        MockMultipartFile attachment = new MockMultipartFile("attachments", "a.txt",
            MediaType.TEXT_PLAIN_VALUE, "hello".getBytes());

        mockMvc.perform(multipart("/api/messages")
                .file(requestPart(new MessageCreateRequest("안녕", channel.getId(), author.getId())))
                .file(attachment))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.content").value("안녕"))
            .andExpect(jsonPath("$.author.username").value("tester"))
            .andExpect(jsonPath("$.attachments[0].fileName").value("a.txt"))
            .andExpect(jsonPath("$.attachments[0].size").value(5));
    }

    @Test
    @DisplayName("메시지 생성 실패 - 없는 채널이면 404")
    void create_fail_channelNotFound() throws Exception {
        mockMvc.perform(multipart("/api/messages").file(requestPart(
                new MessageCreateRequest("안녕", UUID.randomUUID(), author.getId()))))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("CHANNEL_NOT_FOUND"));
    }

    @Test
    @DisplayName("메시지 수정 성공")
    void update_success() throws Exception {
        MessageDto message = createMessage("before");

        mockMvc.perform(patch("/api/messages/{messageId}", message.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new MessageUpdateRequest("after"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").value("after"));
    }

    @Test
    @DisplayName("메시지 삭제 성공")
    void delete_success() throws Exception {
        MessageDto message = createMessage("삭제할 메시지");

        mockMvc.perform(delete("/api/messages/{messageId}", message.id()))
            .andExpect(status().isNoContent());

        assertThat(messageRepository.findById(message.id())).isEmpty();
    }

    @Test
    @DisplayName("메시지 목록 조회 - 페이지 크기보다 많으면 hasNext가 true")
    void findAllByChannelId_success() throws Exception {
        createMessage("1");
        createMessage("2");
        createMessage("3");

        mockMvc.perform(get("/api/messages")
                .param("channelId", channel.getId().toString())
                .param("size", "2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(2))
            .andExpect(jsonPath("$.size").value(2))
            .andExpect(jsonPath("$.hasNext").value(true));
    }
}
