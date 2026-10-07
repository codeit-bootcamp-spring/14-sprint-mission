package com.sprint.mission.discodeit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class MessageIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;

    private Channel channel;
    private User user;

    @BeforeEach
    void setUp() {
        channel = channelRepository.saveAndFlush(
            new Channel(ChannelType.PUBLIC, "테스트 채널", "테스트 채널입니다")
        );
        user = userRepository.saveAndFlush(
            new User("kimoo", "kim@gmail.com", "1234", null)
        );
    }

    @Test
    @DisplayName("메세지생성 API를 호출하고 메세지를 생성한다")
    void success_newMessage() throws Exception {
        MessageCreateRequest request = new MessageCreateRequest(
            "테스트 메세지입니다.",
            channel.getId(),
            user.getId()
        );

        mockMvc.perform(
                multipart("/api/messages")
                    .file(jsonPart("messageCreateRequest", request))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.content").value("테스트 메세지입니다."))
            .andExpect(jsonPath("$.channelId").value(channel.getId().toString()));
    }

    @Test
    @DisplayName("메세지수정 API를 호출하고 메세지를 수정한다")
    void success_updateMessage() throws Exception {
        Message message = messageRepository.saveAndFlush(
            new Message("수정 전 메세지입니다.", channel, user)
        );
        MessageUpdateRequest request = new MessageUpdateRequest("수정 후 메세지입니다.");

        mockMvc.perform(
                patch("/api/messages/{messageId}", message.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").value("수정 된 메세지입니다."));
    }

    private MockMultipartFile jsonPart(String partName, Object request)
        throws Exception {

        return new MockMultipartFile(
            partName,
            "",
            MediaType.APPLICATION_JSON_VALUE,
            objectMapper.writeValueAsBytes(request)
        );
    }
}
