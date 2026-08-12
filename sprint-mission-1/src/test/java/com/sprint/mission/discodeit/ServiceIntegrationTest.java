package com.sprint.mission.discodeit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sprint.mission.discodeit.auth.dto.LoginRequestDto;
import com.sprint.mission.discodeit.auth.service.AuthService;
import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.channel.dto.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.channel.dto.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.channel.dto.ChannelResponseDto;
import com.sprint.mission.discodeit.channel.dto.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.channel.entity.ChannelType;
import com.sprint.mission.discodeit.channel.service.ChannelService;
import com.sprint.mission.discodeit.message.dto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.message.dto.MessageResponseDto;
import com.sprint.mission.discodeit.message.dto.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.message.service.MessageService;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusResponseDto;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.readstatus.service.ReadStatusService;
import com.sprint.mission.discodeit.user.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.UserResponseDto;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequestDto;
import com.sprint.mission.discodeit.user.service.UserService;
import com.sprint.mission.discodeit.userstatus.dto.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.userstatus.dto.UserStatusResponseDto;
import com.sprint.mission.discodeit.userstatus.dto.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.userstatus.service.UserStatusService;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

/**
 * 서비스 계층 통합 테스트.
 *
 * <p>모든 도메인을 한 클래스에서 검증한다. JCF 저장소가 인스턴스 필드이므로
 * {@code @DirtiesContext(BEFORE_EACH_TEST_METHOD)} 로 매 테스트마다 컨텍스트를 새로 띄우면 저장소가 비워진다.
 *
 * <p>테스트가 늘어나 느려지면 {@code @DirtiesContext} 를 떼고 테스트마다 다른 이름을 쓰는
 * 방식으로 바꿀 것.
 */
@SpringBootTest
@TestPropertySource(properties = "discodeit.repository.type=jcf")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Discodeit 서비스 통합 테스트")
class ServiceIntegrationTest {

    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;
    @Autowired
    ChannelService channelService;
    @Autowired
    MessageService messageService;
    @Autowired
    ReadStatusService readStatusService;
    @Autowired
    UserStatusService userStatusService;
    @Autowired
    BinaryContentService binaryContentService;

    // ==================== 픽스처 / 헬퍼 ====================

    private static MockMultipartFile imageFile() {
        return new MockMultipartFile(
            "profileImage", "profile.png", "image/png", "image-bytes".getBytes());
    }

    private static MockMultipartFile textFile(String fileName) {
        return new MockMultipartFile(
            "attachment", fileName, "text/plain", ("content-of-" + fileName).getBytes());
    }

    /**
     * 프로필 이미지 없는 유저
     */
    private UserResponseDto createUser(String name) {
        return userService.userCreate(
            new UserCreateRequestDto(name, "password123", name + "@example.com", null));
    }

    /**
     * 프로필 이미지 있는 유저
     */
    private UserResponseDto createUserWithProfile(String name) {
        return userService.userCreate(
            new UserCreateRequestDto(name, "password123", name + "@example.com", imageFile()));
    }

    private ChannelResponseDto createPublicChannel(String name) {
        return channelService.channelCreate(new ChannelPublicCreateRequestDto(name, name + " 설명"));
    }

    private ChannelResponseDto createPrivateChannel(UUID... userIds) {
        return channelService.privateChannelCreate(
            new ChannelPrivateCreateRequestDto(List.of(userIds)));
    }

    private MessageResponseDto createMessage(UUID userId, UUID channelId, String content) {
        return messageService.messageCreate(
            new MessageCreateRequestDto(userId, channelId, content, null));
    }

    private MessageResponseDto createMessageWithAttachment(UUID userId, UUID channelId,
        String content, String fileName) {
        return messageService.messageCreate(
            new MessageCreateRequestDto(userId, channelId, content, List.of(textFile(fileName))));
    }

    /**
     * BinaryContent 존재 여부. findBinaryContent()는 대상이 없으면 null 역참조로 NPE가 나므로 목록으로 확인한다.
     * (findAllByIdIn 은 현재 파라미터를 무시하고 전체를 반환 — 다음 미션에서 수정 예정)
     */
    private boolean binaryContentExists(UUID binaryContentId) {
        return binaryContentService.findAll(binaryContentId).stream()
            .anyMatch(b -> b.binaryContentId().equals(binaryContentId));
    }

    /**
     * userId로 UserStatus 찾기 (findAllByUserId가 전체를 반환하므로 직접 필터)
     */
    private UserStatusResponseDto findStatusOf(UUID userId) {
        return userStatusService.findAllByUserId(userId).stream()
            .filter(s -> s.userId().equals(userId))
            .findFirst()
            .orElseThrow(() -> new AssertionError("UserStatus를 찾을 수 없습니다: " + userId));
    }

    // ==================== UserService ====================

    @Nested
    @DisplayName("UserService")
    class UserServiceTest {

        @Test
        @DisplayName("프로필 이미지 없이 생성할 수 있다 (선택 항목)")
        void create_withoutProfileImage() {
            UserResponseDto user = createUser("유저A");

            assertThat(user.id()).isNotNull();
            assertThat(user.userName()).isEqualTo("유저A");
            assertThat(user.email()).isEqualTo("유저A@example.com");
            assertThat(user.binaryId()).isNull();
        }

        @Test
        @DisplayName("프로필 이미지를 등록하면 BinaryContent가 실제로 저장된다")
        void create_withProfileImage_savesBinaryContent() {
            UserResponseDto user = createUserWithProfile("유저B");

            assertThat(user.binaryId()).isNotNull();

            BinaryContentResponseDto profile =
                binaryContentService.findBinaryContent(user.binaryId());
            assertThat(profile.fileName()).isEqualTo("profile.png");
            assertThat(profile.contentType()).isEqualTo("image/png");
        }

        @Test
        @DisplayName("생성과 동시에 UserStatus가 만들어진다")
        void create_alsoCreatesUserStatus() {
            UserResponseDto user = createUser("유저C");

            assertThat(findStatusOf(user.id()).userId()).isEqualTo(user.id());
            assertThat(user.online()).isFalse();   // 아직 로그인 전
        }

        @Test
        @DisplayName("username이 중복되면 예외")
        void create_duplicateUserName_throws() {
            userService.userCreate(
                new UserCreateRequestDto("중복", "password123", "first@example.com", null));

            assertThatThrownBy(() -> userService.userCreate(
                new UserCreateRequestDto("중복", "password123", "second@example.com", null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 존재하는 유저 이름");
        }

        @Test
        @DisplayName("email이 중복되면 예외")
        void create_duplicateEmail_throws() {
            userService.userCreate(
                new UserCreateRequestDto("이름1", "password123", "dup@example.com", null));

            assertThatThrownBy(() -> userService.userCreate(
                new UserCreateRequestDto("이름2", "password123", "dup@example.com", null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 존재하는 이메일");
        }

        @Test
        @DisplayName("응답에 패스워드가 포함되지 않는다")
        void response_hasNoPassword() {
            UserResponseDto user = userService.userCreate(
                new UserCreateRequestDto("비밀유저", "supersecret", "pw@example.com", null));

            assertThat(user.toString()).doesNotContain("supersecret");
        }

        @Test
        @DisplayName("findAll은 생성한 모든 유저를 반환한다")
        void findAll_returnsAll() {
            createUser("전체1");
            createUser("전체2");
            createUser("전체3");

            assertThat(userService.findAll())
                .extracting(UserResponseDto::userName)
                .containsExactlyInAnyOrder("전체1", "전체2", "전체3");
        }

        @Test
        @DisplayName("이름·이메일이 실제로 수정된다")
        void update_appliesChanges() {
            UserResponseDto user = createUser("수정전");

            userService.userUpdate(user.id(),
                new UserUpdateRequestDto("수정후", null, "after@example.com", null));

            UserResponseDto found = userService.findById(user.id());
            assertThat(found.userName()).isEqualTo("수정후");
            assertThat(found.email()).isEqualTo("after@example.com");
        }

        @Test
        @DisplayName("null인 필드는 기존 값을 유지한다 (부분 수정)")
        void update_partial_keepsExisting() {
            UserResponseDto user = createUser("부분수정");
            String originalEmail = user.email();

            userService.userUpdate(user.id(),
                new UserUpdateRequestDto("이름만변경", null, null, null));

            UserResponseDto found = userService.findById(user.id());
            assertThat(found.userName()).isEqualTo("이름만변경");
            assertThat(found.email()).isEqualTo(originalEmail);
        }

        @Test
        @DisplayName("비밀번호를 바꾸면 새 비밀번호로 로그인된다")
        void update_password_affectsLogin() {
            UserResponseDto user = createUser("비번변경");

            userService.userUpdate(user.id(),
                new UserUpdateRequestDto(null, "changed999", null, null));

            assertThat(authService.login(new LoginRequestDto("비번변경", "changed999"))).isNotNull();
        }

        @Test
        @DisplayName("프로필 이미지를 교체하면 기존 것은 삭제되고 새 것이 저장된다")
        void update_replacesProfileImage() {
            UserResponseDto user = createUserWithProfile("이미지교체");
            UUID oldProfileId = user.binaryId();

            userService.userUpdate(user.id(),
                new UserUpdateRequestDto(null, null, null, textFile("new-profile.txt")));

            UUID newProfileId = userService.findById(user.id()).binaryId();
            assertThat(newProfileId).isNotNull().isNotEqualTo(oldProfileId);
            assertThat(binaryContentExists(oldProfileId)).isFalse();
            assertThat(binaryContentService.findBinaryContent(newProfileId).fileName())
                .isEqualTo("new-profile.txt");
        }

        @Test
        @DisplayName("삭제하면 조회되지 않는다")
        void delete_removesUser() {
            UserResponseDto user = createUser("삭제대상");

            userService.userDelete(user.id());

            assertThatThrownBy(() -> userService.findById(user.id()))
                .isInstanceOf(IllegalArgumentException.class);
            assertThat(userService.findAll()).isEmpty();
        }

        @Test
        @DisplayName("삭제하면 프로필 이미지와 UserStatus도 함께 삭제된다")
        void delete_cascadesRelated() {
            UserResponseDto user = createUserWithProfile("연쇄삭제");
            UUID profileId = user.binaryId();

            userService.userDelete(user.id());

            assertThat(binaryContentExists(profileId)).isFalse();
            assertThat(userStatusService.findAllByUserId(user.id()))
                .noneMatch(s -> s.userId().equals(user.id()));
        }

        @Test
        @DisplayName("프로필 이미지가 없는 유저도 정상 삭제된다 (NPE 없이)")
        void delete_withoutProfileImage() {
            UserResponseDto user = createUser("이미지없음");

            userService.userDelete(user.id());

            assertThat(userService.findAll()).isEmpty();
        }

        @Test
        @DisplayName("없는 유저를 조회/수정/삭제하면 예외")
        void notFound_throws() {
            UUID unknown = UUID.randomUUID();

            assertThatThrownBy(() -> userService.findById(unknown))
                .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> userService.userUpdate(unknown,
                new UserUpdateRequestDto("이름", null, null, null)))
                .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> userService.userDelete(unknown))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    // ==================== AuthService ====================

    @Nested
    @DisplayName("AuthService")
    class AuthServiceTest {

        @Test
        @DisplayName("username과 password가 일치하면 유저 정보를 반환한다")
        void login_success() {
            createUser("로그인유저");

            UserResponseDto result = authService.login(
                new LoginRequestDto("로그인유저", "password123"));

            assertThat(result.userName()).isEqualTo("로그인유저");
        }

        @Test
        @DisplayName("로그인 응답에 패스워드가 포함되지 않는다")
        void login_responseHasNoPassword() {
            createUser("비밀로그인");

            UserResponseDto result = authService.login(
                new LoginRequestDto("비밀로그인", "password123"));

            assertThat(result.toString()).doesNotContain("password123");
        }

        @Test
        @DisplayName("로그인하면 온라인 상태가 된다")
        void login_marksUserOnline() {
            UserResponseDto user = createUser("온라인유저");
            assertThat(user.online()).isFalse();

            authService.login(new LoginRequestDto("온라인유저", "password123"));

            assertThat(userService.findById(user.id()).online()).isTrue();
            assertThat(findStatusOf(user.id()).lastActiveAt()).isNotNull();
        }

        @Test
        @DisplayName("존재하지 않는 username이면 예외")
        void login_unknownUser_throws() {
            assertThatThrownBy(() -> authService.login(
                new LoginRequestDto("없는유저", "password123")))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("password가 틀리면 예외이고, 온라인 상태가 되지 않는다")
        void login_wrongPassword_throws() {
            UserResponseDto user = createUser("비번틀림");

            assertThatThrownBy(() -> authService.login(
                new LoginRequestDto("비번틀림", "wrong-password")))
                .isInstanceOf(IllegalArgumentException.class);

            assertThat(userService.findById(user.id()).online()).isFalse();
        }
    }

    // ==================== ChannelService ====================

    @Nested
    @DisplayName("ChannelService")
    class ChannelServiceTest {

        @Test
        @DisplayName("PUBLIC 채널은 이름과 설명을 가진다")
        void createPublic() {
            ChannelResponseDto channel = createPublicChannel("공개채널");

            assertThat(channel.id()).isNotNull();
            assertThat(channel.channelName()).isEqualTo("공개채널");
            assertThat(channel.channelType()).isEqualTo(ChannelType.PUBLIC);
            assertThat(channel.lastMessageAt()).isNull();   // 아직 메시지 없음
            assertThat(channel.userIds()).isNull();         // PUBLIC은 참여자 개념 없음
        }

        @Test
        @DisplayName("PRIVATE 채널은 이름과 설명이 없고, 참여자 id를 가진다")
        void createPrivate() {
            UserResponseDto u1 = createUser("참여자1");
            UserResponseDto u2 = createUser("참여자2");

            ChannelResponseDto channel = createPrivateChannel(u1.id(), u2.id());

            assertThat(channel.channelType()).isEqualTo(ChannelType.PRIVATE);
            assertThat(channel.channelName()).isNull();
            assertThat(channel.description()).isNull();
            assertThat(channel.userIds()).containsExactlyInAnyOrder(u1.id(), u2.id());
        }

        @Test
        @DisplayName("PRIVATE 채널 생성 시 참여자별 ReadStatus가 만들어진다")
        void createPrivate_createsReadStatusPerUser() {
            UserResponseDto u1 = createUser("읽음1");
            UserResponseDto u2 = createUser("읽음2");

            ChannelResponseDto channel = createPrivateChannel(u1.id(), u2.id());

            // channelId와 userId가 뒤바뀌지 않았는지까지 확인
            assertThat(readStatusService.findAllByUserId(u1.id()))
                .singleElement()
                .satisfies(rs -> {
                    assertThat(rs.channelId()).isEqualTo(channel.id());
                    assertThat(rs.userId()).isEqualTo(u1.id());
                });
            assertThat(readStatusService.findAllByUserId(u2.id())).hasSize(1);
        }

        @Test
        @DisplayName("존재하지 않는 유저를 참여자로 넣으면 예외")
        void createPrivate_unknownUser_throws() {
            assertThatThrownBy(() -> createPrivateChannel(UUID.randomUUID()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 유저");
        }

        @Test
        @DisplayName("조회 시 가장 최근 메시지의 시각이 포함된다")
        void findById_includesLastMessageAt() {
            UserResponseDto user = createUser("작성자");
            ChannelResponseDto channel = createPublicChannel("메시지채널");

            assertThat(channelService.findById(channel.id()).lastMessageAt()).isNull();

            createMessage(user.id(), channel.id(), "첫 번째");
            MessageResponseDto second = createMessage(user.id(), channel.id(), "두 번째");

            assertThat(channelService.findById(channel.id()).lastMessageAt())
                .isEqualTo(second.createdAt());
        }

        @Test
        @DisplayName("findAllByUserId - PUBLIC은 전부, PRIVATE은 참여한 것만 보인다")
        void findAllByUserId_filtersPrivate() {
            UserResponseDto me = createUser("나");
            UserResponseDto other = createUser("남");

            ChannelResponseDto pub = createPublicChannel("모두의채널");
            ChannelResponseDto mine = createPrivateChannel(me.id());
            ChannelResponseDto theirs = createPrivateChannel(other.id());

            List<UUID> visible = channelService.findAllByUserId(me.id()).stream()
                .map(ChannelResponseDto::id)
                .toList();

            assertThat(visible).contains(pub.id(), mine.id());
            assertThat(visible).doesNotContain(theirs.id());
        }

        @Test
        @DisplayName("PUBLIC 채널의 이름과 설명이 실제로 수정된다")
        void update_appliesChanges() {
            ChannelResponseDto channel = createPublicChannel("원래이름");

            channelService.channelUpdate(channel.id(),
                new ChannelUpdateRequestDto("바뀐이름", "바뀐설명"));

            ChannelResponseDto found = channelService.findById(channel.id());
            assertThat(found.channelName()).isEqualTo("바뀐이름");
            assertThat(found.description()).isEqualTo("바뀐설명");
        }

        @Test
        @DisplayName("null인 필드는 기존 값을 유지한다")
        void update_partial_keepsExisting() {
            ChannelResponseDto channel = createPublicChannel("부분수정");
            String originalDescription = channel.description();

            channelService.channelUpdate(channel.id(),
                new ChannelUpdateRequestDto("이름만", null));

            ChannelResponseDto found = channelService.findById(channel.id());
            assertThat(found.channelName()).isEqualTo("이름만");
            assertThat(found.description()).isEqualTo(originalDescription);
        }

        @Test
        @DisplayName("PRIVATE 채널은 수정할 수 없다")
        void update_privateChannel_throws() {
            UserResponseDto user = createUser("비공개유저");
            ChannelResponseDto channel = createPrivateChannel(user.id());

            assertThatThrownBy(() -> channelService.channelUpdate(channel.id(),
                new ChannelUpdateRequestDto("이름변경", "설명변경")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비공개 채널은 수정할 수 없습니다");
        }

        @Test
        @DisplayName("삭제하면 메시지·첨부파일·ReadStatus가 모두 함께 삭제된다")
        void delete_cascadesAll() {
            UserResponseDto user = createUser("삭제유저");
            ChannelResponseDto channel = createPrivateChannel(user.id());
            MessageResponseDto message =
                createMessageWithAttachment(user.id(), channel.id(), "첨부있음", "doc.txt");
            UUID attachmentId = message.binaryContentsId().get(0);

            assertThat(binaryContentExists(attachmentId)).isTrue();

            channelService.channelDelete(channel.id());

            assertThatThrownBy(() -> channelService.findById(channel.id()))
                .isInstanceOf(IllegalArgumentException.class);
            assertThat(messageService.findAllByChannelId(channel.id())).isEmpty();
            assertThat(readStatusService.findAllByUserId(user.id())).isEmpty();
            assertThat(binaryContentExists(attachmentId)).isFalse();
        }

        @Test
        @DisplayName("없는 채널을 조회/수정/삭제하면 예외")
        void notFound_throws() {
            UUID unknown = UUID.randomUUID();

            assertThatThrownBy(() -> channelService.findById(unknown))
                .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> channelService.channelUpdate(unknown,
                new ChannelUpdateRequestDto("이름", "설명")))
                .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> channelService.channelDelete(unknown))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    // ==================== MessageService ====================

    @Nested
    @DisplayName("MessageService")
    class MessageServiceTest {

        @Test
        @DisplayName("첨부파일 없이 생성할 수 있고, userId/channelId가 뒤바뀌지 않는다")
        void create_withoutAttachments() {
            UserResponseDto user = createUser("작성자");
            ChannelResponseDto channel = createPublicChannel("채널");

            MessageResponseDto message = createMessage(user.id(), channel.id(), "안녕하세요");

            assertThat(message.messageId()).isNotNull();
            assertThat(message.message()).isEqualTo("안녕하세요");
            assertThat(message.userId()).isEqualTo(user.id());
            assertThat(message.channelId()).isEqualTo(channel.id());
            assertThat(message.binaryContentsId()).isEmpty();
        }

        @Test
        @DisplayName("여러 개의 첨부파일을 같이 등록할 수 있다")
        void create_withMultipleAttachments() {
            UserResponseDto user = createUser("첨부작성자");
            ChannelResponseDto channel = createPublicChannel("첨부채널");

            MessageResponseDto message = messageService.messageCreate(
                new MessageCreateRequestDto(user.id(), channel.id(), "파일 3개",
                    List.of(textFile("a.txt"), textFile("b.txt"), textFile("c.txt"))));

            assertThat(message.binaryContentsId()).hasSize(3);
            assertThat(message.binaryContentsId()).allMatch(
                ServiceIntegrationTest.this::binaryContentExists);
        }

        @Test
        @DisplayName("존재하지 않는 채널/유저면 예외")
        void create_unknownReference_throws() {
            UserResponseDto user = createUser("유저");
            ChannelResponseDto channel = createPublicChannel("채널");

            assertThatThrownBy(() -> messageService.messageCreate(
                new MessageCreateRequestDto(user.id(), UUID.randomUUID(), "내용", null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 채널");

            assertThatThrownBy(() -> messageService.messageCreate(
                new MessageCreateRequestDto(UUID.randomUUID(), channel.id(), "내용", null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 유저");
        }

        @Test
        @DisplayName("findAllByChannelId는 해당 채널의 메시지만 반환한다")
        void findAllByChannelId_filtersOtherChannels() {
            UserResponseDto user = createUser("유저");
            ChannelResponseDto channelA = createPublicChannel("채널A");
            ChannelResponseDto channelB = createPublicChannel("채널B");

            createMessage(user.id(), channelA.id(), "A-1");
            createMessage(user.id(), channelA.id(), "A-2");
            createMessage(user.id(), channelB.id(), "B-1");

            assertThat(messageService.findAllByChannelId(channelA.id()))
                .extracting(MessageResponseDto::message)
                .containsExactlyInAnyOrder("A-1", "A-2");
            assertThat(messageService.findAllByChannelId(channelB.id())).hasSize(1);
        }

        @Test
        @DisplayName("메시지 내용이 실제로 수정된다")
        void update_appliesChanges() {
            UserResponseDto user = createUser("수정자");
            ChannelResponseDto channel = createPublicChannel("수정채널");
            MessageResponseDto message = createMessage(user.id(), channel.id(), "원래 내용");

            messageService.messageUpdate(message.messageId(),
                new MessageUpdateRequestDto("바뀐 내용"));

            assertThat(messageService.findById(message.messageId()).message())
                .isEqualTo("바뀐 내용");
        }

        @Test
        @DisplayName("삭제하면 첨부파일도 함께 삭제된다")
        void delete_cascadesAttachments() {
            UserResponseDto user = createUser("삭제자");
            ChannelResponseDto channel = createPublicChannel("삭제채널");
            MessageResponseDto message =
                createMessageWithAttachment(user.id(), channel.id(), "첨부있음", "file.txt");
            UUID attachmentId = message.binaryContentsId().get(0);

            messageService.messageDelete(message.messageId());

            assertThatThrownBy(() -> messageService.findById(message.messageId()))
                .isInstanceOf(IllegalArgumentException.class);
            assertThat(binaryContentExists(attachmentId)).isFalse();
        }

        @Test
        @DisplayName("첨부파일 없는 메시지도 정상 삭제된다 (NPE 없이)")
        void delete_withoutAttachments() {
            UserResponseDto user = createUser("무첨부");
            ChannelResponseDto channel = createPublicChannel("무첨부채널");
            MessageResponseDto message = createMessage(user.id(), channel.id(), "첨부없음");

            messageService.messageDelete(message.messageId());

            assertThat(messageService.findAllByChannelId(channel.id())).isEmpty();
        }

        @Test
        @DisplayName("없는 메시지를 조회/수정/삭제하면 예외")
        void notFound_throws() {
            UUID unknown = UUID.randomUUID();

            assertThatThrownBy(() -> messageService.findById(unknown))
                .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> messageService.messageUpdate(unknown,
                new MessageUpdateRequestDto("내용")))
                .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> messageService.messageDelete(unknown))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    // ==================== ReadStatusService ====================

    @Nested
    @DisplayName("ReadStatusService")
    class ReadStatusServiceTest {

        @Test
        @DisplayName("생성하면 channelId와 userId가 뒤바뀌지 않는다")
        void create_preservesIdOrder() {
            UserResponseDto user = createUser("읽음유저");
            ChannelResponseDto channel = createPublicChannel("읽음채널");

            ReadStatusResponseDto status = readStatusService.readStatusCreate(
                new ReadStatusCreateRequestDto(channel.id(), user.id()));

            assertThat(status.channelId()).isEqualTo(channel.id());
            assertThat(status.userId()).isEqualTo(user.id());
            assertThat(status.lastReadAt()).isNotNull();
        }

        @Test
        @DisplayName("존재하지 않는 채널/유저면 예외")
        void create_unknownReference_throws() {
            UserResponseDto user = createUser("유저");
            ChannelResponseDto channel = createPublicChannel("채널");

            assertThatThrownBy(() -> readStatusService.readStatusCreate(
                new ReadStatusCreateRequestDto(UUID.randomUUID(), user.id())))
                .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> readStatusService.readStatusCreate(
                new ReadStatusCreateRequestDto(channel.id(), UUID.randomUUID())))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("마지막 읽은 시각이 실제로 갱신된다")
        void update_appliesLastReadAt() {
            UserResponseDto user = createUser("갱신유저");
            ChannelResponseDto channel = createPublicChannel("갱신채널");
            ReadStatusResponseDto created = readStatusService.readStatusCreate(
                new ReadStatusCreateRequestDto(channel.id(), user.id()));

            Instant newTime = Instant.now().plus(Duration.ofMinutes(10));
            readStatusService.readStatusUpdate(created.id(),
                new ReadStatusUpdateRequestDto(newTime));

            assertThat(readStatusService.findReadStatus(created.id()).lastReadAt())
                .isEqualTo(newTime);
        }

        @Test
        @DisplayName("id로 삭제하면 그 하나만 사라진다")
        void delete_removesOnlyOne() {
            UserResponseDto u1 = createUser("삭제유저1");
            UserResponseDto u2 = createUser("삭제유저2");
            ChannelResponseDto channel = createPublicChannel("공유채널");

            ReadStatusResponseDto s1 = readStatusService.readStatusCreate(
                new ReadStatusCreateRequestDto(channel.id(), u1.id()));
            readStatusService.readStatusCreate(
                new ReadStatusCreateRequestDto(channel.id(), u2.id()));

            readStatusService.readStatusDelete(s1.id());

            assertThat(readStatusService.findAllByUserId(u1.id())).isEmpty();
            assertThat(readStatusService.findAllByUserId(u2.id())).hasSize(1);
        }

        @Test
        @DisplayName("findAllByUserId는 해당 유저의 것만 반환한다")
        void findAllByUserId_filtersOthers() {
            UserResponseDto me = createUser("나");
            UserResponseDto other = createUser("남");
            ChannelResponseDto c1 = createPublicChannel("채널1");
            ChannelResponseDto c2 = createPublicChannel("채널2");

            readStatusService.readStatusCreate(new ReadStatusCreateRequestDto(c1.id(), me.id()));
            readStatusService.readStatusCreate(new ReadStatusCreateRequestDto(c2.id(), me.id()));
            readStatusService.readStatusCreate(new ReadStatusCreateRequestDto(c1.id(), other.id()));

            assertThat(readStatusService.findAllByUserId(me.id()))
                .hasSize(2)
                .allMatch(r -> r.userId().equals(me.id()));
        }

        @Test
        @Disabled("요구사항 미구현 — 같은 channel+user 조합의 중복 검사가 없음. "
            + "readStatusCreate에 중복 체크를 추가하면 통과")
        @DisplayName("같은 채널·유저 조합은 중복 생성될 수 없다")
        void create_duplicate_throws() {
            UserResponseDto user = createUser("중복유저");
            ChannelResponseDto channel = createPublicChannel("중복채널");
            readStatusService.readStatusCreate(
                new ReadStatusCreateRequestDto(channel.id(), user.id()));

            assertThatThrownBy(() -> readStatusService.readStatusCreate(
                new ReadStatusCreateRequestDto(channel.id(), user.id())))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    // ==================== UserStatusService ====================

    @Nested
    @DisplayName("UserStatusService")
    class UserStatusServiceTest {

        @Test
        @DisplayName("id로 조회할 수 있다")
        void findById() {
            UserResponseDto user = createUser("상태유저");
            UserStatusResponseDto status = findStatusOf(user.id());

            UserStatusResponseDto found = userStatusService.findUserStatus(status.id());

            assertThat(found.id()).isEqualTo(status.id());
            assertThat(found.userId()).isEqualTo(user.id());
        }

        @Test
        @DisplayName("존재하지 않는 유저의 상태를 만들면 예외")
        void create_unknownUser_throws() {
            assertThatThrownBy(() -> userStatusService.userStatusCreate(
                new UserStatusCreateRequestDto(UUID.randomUUID())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 유저");
        }

        @Test
        @DisplayName("같은 유저의 상태를 중복 생성하면 예외")
        void create_duplicate_throws() {
            UserResponseDto user = createUser("중복상태");
            // userCreate가 이미 UserStatus를 만들었으므로 두 번째 생성은 실패해야 한다

            assertThatThrownBy(() -> userStatusService.userStatusCreate(
                new UserStatusCreateRequestDto(user.id())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 상태를 정의한 유저");
        }

        @Test
        @DisplayName("마지막 활동 시각을 갱신하면 온라인이 된다")
        void update_appliesLastActiveAt() {
            UserResponseDto user = createUser("활동유저");
            UserStatusResponseDto status = findStatusOf(user.id());
            assertThat(status.online()).isFalse();

            Instant now = Instant.now();
            userStatusService.userStatusUpdate(status.id(),
                new UserStatusUpdateRequestDto(null, now));

            UserStatusResponseDto updated = userStatusService.findUserStatus(status.id());
            assertThat(updated.lastActiveAt()).isEqualTo(now);
            assertThat(updated.online()).isTrue();
        }

        @Test
        @DisplayName("5분이 지난 활동 시각이면 오프라인이다")
        void update_staleActivity_isOffline() {
            UserResponseDto user = createUser("오래된유저");
            UserStatusResponseDto status = findStatusOf(user.id());

            userStatusService.userStatusUpdate(status.id(),
                new UserStatusUpdateRequestDto(null, Instant.now().minus(Duration.ofMinutes(6))));

            assertThat(userStatusService.findUserStatus(status.id()).online()).isFalse();
            assertThat(userService.findById(user.id()).online()).isFalse();
        }

        @Test
        @DisplayName("삭제하면 목록에서 사라진다")
        void delete_removesStatus() {
            UserResponseDto user = createUser("상태삭제");
            UserStatusResponseDto status = findStatusOf(user.id());

            userStatusService.userStatusDelete(status.id());

            assertThat(userStatusService.findAllByUserId(user.id()))
                .noneMatch(s -> s.id().equals(status.id()));
        }

        @Test
        @Disabled("findAllByUserId가 파라미터를 무시하고 전체를 반환함. "
            + "인터페이스를 findAll()로 바꾸거나 userId 필터를 추가하면 통과")
        @DisplayName("findAllByUserId는 해당 유저의 상태만 반환한다")
        void findAllByUserId_filtersOthers() {
            UserResponseDto me = createUser("나");
            createUser("남");

            assertThat(userStatusService.findAllByUserId(me.id()))
                .hasSize(1)
                .allMatch(s -> s.userId().equals(me.id()));
        }
    }

    // ==================== BinaryContentService ====================

    @Nested
    @DisplayName("BinaryContentService")
    class BinaryContentServiceTest {

        @Test
        @DisplayName("생성하면 id가 부여되고 내용이 보존된다")
        void create() {
            BinaryContentResponseDto created = binaryContentService.binaryContentCreate(
                new BinaryContentCreateRequestDto("doc.pdf", "application/pdf", "PDF".getBytes()));

            assertThat(created.binaryContentId()).isNotNull();
            assertThat(created.fileName()).isEqualTo("doc.pdf");
            assertThat(created.contentType()).isEqualTo("application/pdf");
            assertThat(created.bytes()).isEqualTo("PDF".getBytes());
        }

        @Test
        @DisplayName("id로 조회할 수 있다")
        void findById() {
            BinaryContentResponseDto created = binaryContentService.binaryContentCreate(
                new BinaryContentCreateRequestDto("a.txt", "text/plain", "A".getBytes()));

            assertThat(binaryContentService.findBinaryContent(created.binaryContentId()).fileName())
                .isEqualTo("a.txt");
        }

        @Test
        @DisplayName("삭제하면 목록에서 사라진다")
        void delete() {
            BinaryContentResponseDto created = binaryContentService.binaryContentCreate(
                new BinaryContentCreateRequestDto("b.txt", "text/plain", "B".getBytes()));

            binaryContentService.binaryContentDelete(created.binaryContentId());

            assertThat(binaryContentExists(created.binaryContentId())).isFalse();
        }
    }

    // ==================== 시나리오 ====================

    @Nested
    @DisplayName("전체 시나리오")
    class Scenario {

        @Test
        @DisplayName("가입 → 로그인 → 채널 생성 → 대화 → 수정 → 정리")
        void fullFlow() {
            // 1. 가입
            UserResponseDto alice = createUserWithProfile("앨리스");
            UserResponseDto bob = createUser("밥");
            assertThat(userService.findAll()).hasSize(2);

            // 2. 로그인 → 온라인
            authService.login(new LoginRequestDto("앨리스", "password123"));
            assertThat(userService.findById(alice.id()).online()).isTrue();
            assertThat(userService.findById(bob.id()).online()).isFalse();

            // 3. 공개 채널 + 비공개 채널
            ChannelResponseDto lobby = createPublicChannel("로비");
            ChannelResponseDto dm = createPrivateChannel(alice.id(), bob.id());
            assertThat(dm.userIds()).containsExactlyInAnyOrder(alice.id(), bob.id());

            // 4. 밥은 로비와 DM을 볼 수 있다
            assertThat(channelService.findAllByUserId(bob.id()))
                .extracting(ChannelResponseDto::id)
                .containsExactlyInAnyOrder(lobby.id(), dm.id());

            // 5. 대화 (첨부 포함)
            createMessage(alice.id(), lobby.id(), "안녕하세요!");
            MessageResponseDto withFile =
                createMessageWithAttachment(bob.id(), lobby.id(), "자료 공유합니다", "spec.pdf");
            UUID attachmentId = withFile.binaryContentsId().get(0);

            assertThat(messageService.findAllByChannelId(lobby.id())).hasSize(2);
            assertThat(channelService.findById(lobby.id()).lastMessageAt())
                .isEqualTo(withFile.createdAt());

            // 6. 메시지 수정
            messageService.messageUpdate(withFile.messageId(),
                new MessageUpdateRequestDto("자료 다시 올립니다"));
            assertThat(messageService.findById(withFile.messageId()).message())
                .isEqualTo("자료 다시 올립니다");

            // 7. 채널 이름 수정 (PUBLIC만 가능)
            channelService.channelUpdate(lobby.id(), new ChannelUpdateRequestDto("메인로비", null));
            assertThat(channelService.findById(lobby.id()).channelName()).isEqualTo("메인로비");
            assertThatThrownBy(() -> channelService.channelUpdate(dm.id(),
                new ChannelUpdateRequestDto("바꿔보기", null)))
                .isInstanceOf(IllegalArgumentException.class);

            // 8. 채널 삭제 → 메시지·첨부 정리
            channelService.channelDelete(lobby.id());
            assertThat(messageService.findAllByChannelId(lobby.id())).isEmpty();
            assertThat(binaryContentExists(attachmentId)).isFalse();

            // 9. 유저 삭제 → 프로필 정리
            UUID aliceProfile = alice.binaryId();
            userService.userDelete(alice.id());
            assertThat(userService.findAll()).hasSize(1);
            assertThat(binaryContentExists(aliceProfile)).isFalse();

            // 10. 밥에게는 DM만 남는다
            assertThat(channelService.findAllByUserId(bob.id()))
                .extracting(ChannelResponseDto::id)
                .containsExactly(dm.id());
        }
    }
}