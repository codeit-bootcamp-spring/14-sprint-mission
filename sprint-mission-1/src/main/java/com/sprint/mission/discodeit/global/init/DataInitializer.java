package com.sprint.mission.discodeit.global.init;

import com.sprint.mission.discodeit.channel.dto.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.channel.dto.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.channel.dto.ChannelResponseDto;
import com.sprint.mission.discodeit.channel.service.ChannelService;
import com.sprint.mission.discodeit.message.dto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.message.dto.MessageResponseDto;
import com.sprint.mission.discodeit.message.service.MessageService;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusResponseDto;
import com.sprint.mission.discodeit.readstatus.service.ReadStatusService;
import com.sprint.mission.discodeit.user.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.UserResponseDto;
import com.sprint.mission.discodeit.user.service.UserService;
import com.sprint.mission.discodeit.userstatus.dto.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.userstatus.service.UserStatusService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Postman / 화면 테스트용 초기 데이터 생성.
 * application.yaml 의 discodeit.seed 가 true 일 때만 동작한다.
 *
 * <p>프로필 이미지는 classpath 의 {@code seed/profiles/{유저명}.{확장자}} 를 찾아 등록한다.
 * 파일이 없으면 프로필 없이 생성되고, 화면에서는 기본 아바타가 표시된다.
 * 실제 이미지를 쓰고 싶다면 같은 경로에 png/jpg 를 넣으면 된다.
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.seed", havingValue = "true")
public class DataInitializer implements CommandLineRunner {

    private static final String PASSWORD = "password123";
    private static final String LINE = "=".repeat(78);

    private static final String PROFILE_DIR = "seed/profiles/";
    private static final Map<String, String> CONTENT_TYPES = Map.of(
        "png", "image/png",
        "jpg", "image/jpeg",
        "jpeg", "image/jpeg",
        "svg", "image/svg+xml",
        "gif", "image/gif",
        "webp", "image/webp"
    );
    private static final List<String> EXTENSIONS =
        List.of("png", "jpg", "jpeg", "webp", "gif", "svg");

    private final UserService userService;
    private final UserStatusService userStatusService;
    private final ChannelService channelService;
    private final MessageService messageService;
    private final ReadStatusService readStatusService;

    @Override
    public void run(String... args) {
        if (!userService.findAll().isEmpty()) {
            System.out.println("\n[DataInitializer] 기존 데이터가 있어 초기화를 건너뜁니다.\n");
            return;
        }

        // ---------- 유저 ----------
        UserResponseDto woody = createUser("woody", "woody@codeit.com");
        UserResponseDto buzz = createUser("buzz", "buzz@codeit.com");
        UserResponseDto jessie = createUser("jessie", "jessie@codeit.com");
        UserResponseDto rex = createUser("rex", "rex@codeit.com");
        UserResponseDto trash = createUser("deleteme", "deleteme@codeit.com");

        // ---------- 온라인 상태로 전환 ----------
        // isOnline() 은 마지막 활동 시각이 5분 이내인지로 판단한다.
        markOnline(woody, buzz, jessie, rex);

        // ---------- 채널 ----------
        ChannelResponseDto general = createPublicChannel("일반", "자유롭게 대화하는 채널");
        ChannelResponseDto notice = createPublicChannel("공지", "공지사항 전용 채널");
        ChannelResponseDto trashCh = createPublicChannel("삭제용채널", "삭제 테스트용");

        ChannelResponseDto dm = channelService.privateChannelCreate(
            new ChannelPrivateCreateRequestDto(List.of(woody.id(), buzz.id())));

        // ---------- 메시지 ----------
        MessageResponseDto m1 = createMessage(woody.id(), general.id(), "안녕하세요! 첫 메시지입니다.");
        MessageResponseDto m2 = createMessage(buzz.id(), general.id(), "반갑습니다 :)");
        MessageResponseDto m3 = createMessage(jessie.id(), general.id(), "저도 왔어요");
        MessageResponseDto m4 = createMessage(woody.id(), notice.id(), "이번 주 공지입니다.");
        MessageResponseDto m5 = createMessage(woody.id(), dm.id(), "버즈, 잠깐 얘기 좀 해요");

        // ---------- 읽음 상태 (공개 채널) ----------
        ReadStatusResponseDto rs = readStatusService.readStatusCreate(
            new ReadStatusCreateRequestDto(general.id(), jessie.id()));

        // ---------- 출력 ----------
        StringBuilder sb = new StringBuilder("\n");
        sb.append(LINE).append("\n");
        sb.append("  테스트용 초기 데이터가 생성되었습니다\n");
        sb.append(LINE).append("\n\n");

        sb.append("[USER]\n");
        appendUser(sb, "woody", woody);
        appendUser(sb, "buzz", buzz);
        appendUser(sb, "jessie", jessie);
        appendUser(sb, "rex", rex);
        appendUser(sb, "deleteme", trash);

        sb.append("\n[CHANNEL]\n");
        appendChannel(sb, "일반(PUBLIC)", general);
        appendChannel(sb, "공지(PUBLIC)", notice);
        appendChannel(sb, "삭제용(PUBLIC)", trashCh);
        appendChannel(sb, "DM(PRIVATE)", dm);

        sb.append("\n[MESSAGE]\n");
        appendMessage(sb, "일반-1", m1);
        appendMessage(sb, "일반-2", m2);
        appendMessage(sb, "일반-3", m3);
        appendMessage(sb, "공지-1", m4);
        appendMessage(sb, "DM-1", m5);

        sb.append("\n[READ STATUS]\n");
        sb.append(String.format("  %-14s id=%s  (channel=%s, user=%s)%n",
            "jessie/일반", rs.id(), rs.channelId(), rs.userId()));

        sb.append("\n").append(LINE).append("\n");
        sb.append("  Postman Collection Variables 에 붙여넣기\n");
        sb.append(LINE).append("\n");
        sb.append(String.format("  userId        = %s%n", woody.id()));
        sb.append(String.format("  userId2       = %s%n", buzz.id()));
        sb.append(String.format("  userId3       = %s%n", jessie.id()));
        sb.append(String.format("  deleteUserId  = %s%n", trash.id()));
        sb.append(String.format("  userStatusId  = %s%n", woody.userStatusId()));
        sb.append(String.format("  channelId     = %s%n", general.id()));
        sb.append(String.format("  channelId2    = %s%n", notice.id()));
        sb.append(String.format("  deleteChannelId = %s%n", trashCh.id()));
        sb.append(String.format("  privateChannelId = %s%n", dm.id()));
        sb.append(String.format("  messageId     = %s%n", m1.messageId()));
        sb.append(String.format("  deleteMessageId = %s%n", m3.messageId()));
        sb.append(String.format("  readStatusId  = %s%n", rs.id()));
        sb.append(String.format("  binaryContentId = %s%n", woody.binaryId()));
        sb.append(LINE).append("\n");
        sb.append("  로그인 계정: woody / ").append(PASSWORD).append("\n");
        sb.append("  화면 확인:   http://localhost:8080\n");
        sb.append(LINE).append("\n");

        System.out.println(sb);
    }

    /* ---------- 생성 helpers ---------- */

    private UserResponseDto createUser(String name, String email) {
        return userService.userCreate(
            new UserCreateRequestDto(name, PASSWORD, email, loadProfileImage(name)));
    }

    private void markOnline(UserResponseDto... users) {
        for (UserResponseDto user : users) {
            userStatusService.userStatusUpdate(
                user.userStatusId(), new UserStatusUpdateRequestDto(null, Instant.now()));
        }
    }

    private ChannelResponseDto createPublicChannel(String name, String description) {
        return channelService.channelCreate(new ChannelPublicCreateRequestDto(name, description));
    }

    private MessageResponseDto createMessage(UUID userId, UUID channelId, String content) {
        return messageService.messageCreate(
            new MessageCreateRequestDto(userId, channelId, content, null));
    }

    /* ---------- 프로필 이미지 로딩 ---------- */

    /**
     * classpath 의 seed/profiles/{name}.{확장자} 를 찾아 MultipartFile 로 감싼다.
     * 없으면 null 을 반환하고, 유저는 프로필 없이 생성된다.
     */
    private MultipartFile loadProfileImage(String name) {
        for (String extension : EXTENSIONS) {
            String path = PROFILE_DIR + name + "." + extension;
            ClassPathResource resource = new ClassPathResource(path);
            if (!resource.exists()) {
                continue;
            }
            try (InputStream in = resource.getInputStream()) {
                return new SimpleMultipartFile(
                    name + "." + extension,
                    CONTENT_TYPES.getOrDefault(extension, "application/octet-stream"),
                    in.readAllBytes());
            } catch (IOException e) {
                System.out.println("[DataInitializer] 프로필 이미지 로딩 실패: " + path);
                return null;
            }
        }
        return null;
    }

    /**
     * 시드 데이터 주입 전용 MultipartFile 구현.
     * MockMultipartFile 은 spring-test(test scope)에 있어 main 에서 사용할 수 없다.
     */
    private record SimpleMultipartFile(String originalFilename, String contentType, byte[] content)
        implements MultipartFile {

        @Override
        public String getName() {
            return "profileImage";
        }

        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() {
            return content;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(File dest) throws IOException {
            try (FileOutputStream out = new FileOutputStream(dest)) {
                out.write(content);
            }
        }
    }

    /* ---------- 출력 helpers ---------- */

    private void appendUser(StringBuilder sb, String label, UserResponseDto u) {
        sb.append(String.format("  %-10s id=%s%n", label, u.id()));
        sb.append(String.format("  %-10s userStatusId=%s  binaryId=%s%n",
            "", u.userStatusId(), u.binaryId()));
    }

    private void appendChannel(StringBuilder sb, String label, ChannelResponseDto c) {
        sb.append(String.format("  %-16s id=%s%n", label, c.id()));
    }

    private void appendMessage(StringBuilder sb, String label, MessageResponseDto m) {
        sb.append(String.format("  %-8s id=%s  channel=%s%n", label, m.messageId(), m.channelId()));
    }
}
