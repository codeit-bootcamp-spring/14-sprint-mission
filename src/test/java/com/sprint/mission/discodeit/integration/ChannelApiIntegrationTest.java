package com.sprint.mission.discodeit.integration;

import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.readStatus.repository.ReadStatusRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("채널 API 통합 테스트")
class ChannelApiIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private ReadStatusRepository readStatusRepository;

    @Nested
    @DisplayName("생성 POST /api/channels/public, /private")
    class Create {

        @Test
        @DisplayName("성공 - PUBLIC 채널이 생성되고 DB에 저장된다")
        void createPublic_success() throws Exception {
            MvcResult result = mockMvc.perform(post("/api/channels/public")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"name": "공지", "description": "공지 채널"}
                                    """))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.type").value("PUBLIC"))
                    .andExpect(jsonPath("$.name").value("공지"))
                    .andReturn();

            assertThat(channelRepository.findById(idOf(result))).isPresent();
        }

        @Test
        @DisplayName("성공 - PRIVATE 채널이 생성되고 참여자마다 읽음 상태가 만들어진다")
        void createPrivate_success() throws Exception {
            UUID kim = createUser("kim");
            UUID lee = createUser("lee");

            MvcResult result = mockMvc.perform(post("/api/channels/private")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"participantIds": ["%s", "%s"]}
                                    """.formatted(kim, lee)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.type").value("PRIVATE"))
                    .andExpect(jsonPath("$.participants.length()").value(2))
                    .andReturn();

            assertThat(readStatusRepository.findUserIdByChannelId(idOf(result)))
                    .containsExactlyInAnyOrder(kim, lee);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 참여자가 있으면 404, 트랜잭션이 롤백되어 채널이 남지 않는다")
        // 클래스의 @Transactional 안에서는 서비스 롤백이 테스트 종료까지 미뤄져 확인할 수 없으므로,
        // 이 테스트만 트랜잭션 없이 실행해 운영과 같은 조건에서 롤백을 검증한다
        @Transactional(propagation = Propagation.NOT_SUPPORTED)
        void createPrivate_unknownUser_fail() throws Exception {
            mockMvc.perform(post("/api/channels/private")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"participantIds": ["%s"]}
                                    """.formatted(UUID.randomUUID())))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("USER_NOT_FOUND"));

            assertThat(channelRepository.findAll()).isEmpty();
        }

        @Test
        @DisplayName("실패 - 이름 없이 PUBLIC 채널을 만들면 400")
        void createPublic_blankName_fail() throws Exception {
            mockMvc.perform(post("/api/channels/public")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"name": ""}
                                    """))
                    .andExpect(status().isBadRequest());

            assertThat(channelRepository.findAll()).isEmpty();
        }
    }

    @Nested
    @DisplayName("수정 PATCH /api/channels/{channelId}")
    class Update {

        @Test
        @DisplayName("성공 - PUBLIC 채널 이름이 바뀌고 DB에 반영된다")
        void update_success() throws Exception {
            UUID channelId = createPublicChannel("공지");

            mockMvc.perform(patch("/api/channels/{channelId}", channelId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"newName": "새공지"}
                                    """))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("새공지"));

            assertThat(channelRepository.findById(channelId).orElseThrow().getName()).isEqualTo("새공지");
        }

        @Test
        @DisplayName("실패 - PRIVATE 채널은 수정할 수 없다 (403)")
        void update_private_fail() throws Exception {
            UUID kim = createUser("kim");
            MvcResult created = mockMvc.perform(post("/api/channels/private")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"participantIds": ["%s"]}
                                    """.formatted(kim)))
                    .andReturn();

            mockMvc.perform(patch("/api/channels/{channelId}", idOf(created))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"newName": "바꿀수없음"}
                                    """))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.code").value("PRIVATE_CHANNEL_UPDATE_NOT"));
        }

        @Test
        @DisplayName("실패 - 없는 채널이면 404")
        void update_notFound_fail() throws Exception {
            mockMvc.perform(patch("/api/channels/{channelId}", UUID.randomUUID())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"newName": "새공지"}
                                    """))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("CHANNEL_NOT_FOUND"));
        }
    }

    @Nested
    @DisplayName("삭제 DELETE /api/channels/{channelId}")
    class Delete {

        @Test
        @DisplayName("성공 - 삭제 후 사용자 채널 목록에서 사라진다")
        void delete_success() throws Exception {
            UUID userId = createUser("kim");
            UUID channelId = createPublicChannel("공지");

            mockMvc.perform(delete("/api/channels/{channelId}", channelId))
                    .andExpect(status().isOk());

            assertThat(channelRepository.findById(channelId)).isEmpty();
            mockMvc.perform(get("/api/channels").param("userId", userId.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        @DisplayName("성공(경계) - 다른 채널은 삭제되지 않는다")
        void delete_onlyTarget() throws Exception {
            UUID userId = createUser("kim");
            UUID target = createPublicChannel("삭제할방");
            createPublicChannel("남을방");

            mockMvc.perform(delete("/api/channels/{channelId}", target))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/channels").param("userId", userId.toString()))
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].name").value("남을방"));
        }
    }
}
