package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.auth.LoginRequestDto;
import com.sprint.mission.discodeit.dto.auth.LoginResponseDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserUpsertRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.service.application.auth.AuthApplicationService;
import com.sprint.mission.discodeit.service.application.binarycontent.BinaryContentApplicationService;
import com.sprint.mission.discodeit.service.application.channel.ChannelApplicationService;
import com.sprint.mission.discodeit.service.application.message.MessageApplicationService;
import com.sprint.mission.discodeit.service.application.user.UserApplicationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        try {
            // ===================== 서비스 초기화 =====================

            UserApplicationService userService = context.getBean(UserApplicationService.class);

            ChannelApplicationService channelService = context.getBean(ChannelApplicationService.class);

            MessageApplicationService messageService = context.getBean(MessageApplicationService.class);

            AuthApplicationService authService = context.getBean(AuthApplicationService.class);

            BinaryContentApplicationService binaryContentService =
                    context.getBean(BinaryContentApplicationService.class);


            // ===================== 셋업 =====================

            System.out.println("\n===== 셋업 =====");

            UserResponseDto woody = userService.create(
                    UserUpsertRequestDto.of(
                            "woody",
                            "woody@codeit.com",
                            "woody1234"
                    ),
                    BinaryContentCreateRequestDto.of(
                            "woody-profile.txt",
                            "woody profile image"
                                    .getBytes(StandardCharsets.UTF_8)
                    )
            );

            UserResponseDto alice = userService.create(
                    UserUpsertRequestDto.of(
                            "alice",
                            "alice@codeit.com",
                            "alice1234"
                    ),
                    null
            );

            ChannelResponseDto publicChannel = channelService.createPublic(
                    PublicChannelCreateRequestDto.of(
                            "공지",
                            "공지 채널입니다."
                    )
            );

            ChannelResponseDto privateChannel = channelService.createPrivate(
                    PrivateChannelCreateRequestDto.of(
                            List.of(
                                    woody.getId(),
                                    alice.getId()
                            )
                    )
            );


            // ===================== BinaryContent 테스트 =====================

            System.out.println("\n===== BinaryContent =====");

            BinaryContentResponseDto profile =
                    binaryContentService.findById(woody.getProfileId());

            System.out.println(
                    "프로필 조회: id=" + profile.getId()
                            + ", fileName=" + profile.getFileName()
                            + ", size=" + profile.getBytes().length
            );

            byte[] documentBytes =
                    "standalone binary content"
                            .getBytes(StandardCharsets.UTF_8);

            BinaryContentResponseDto document =
                    binaryContentService.create(
                            BinaryContentCreateRequestDto.of(
                                    "document.txt",
                                    documentBytes
                            )
                    );

            BinaryContentResponseDto foundDocument =
                    binaryContentService.findById(document.getId());

            System.out.println(
                    "독립 파일 생성/조회: id=" + foundDocument.getId()
                            + ", fileName=" + foundDocument.getFileName()
                            + ", bytesEqual=" + Arrays.equals(
                                    documentBytes,
                                    foundDocument.getBytes()
                            )
            );


            // ===================== User 테스트 =====================

            System.out.println("\n===== User =====");

            UserResponseDto user = userService.findById(woody.getId());

            List<UserResponseDto> users = userService.findAll();

            UserResponseDto updatedUser = userService.update(
                    woody.getId(),
                    UserUpsertRequestDto.of(
                            "woody-updated",
                            "woody-new@codeit.com",
                            "woody5678"
                    ),
                    null
            );


            // ===================== Auth 테스트 =====================

            System.out.println("\n===== Auth =====");

            LoginResponseDto login = authService.login(
                    LoginRequestDto.from(
                            "woody-updated",
                            "woody5678"
                    )
            );

            // ===================== Channel 테스트 =====================

            System.out.println("\n===== Channel =====");

            ChannelResponseDto channel = channelService.findById(publicChannel.getId());

            List<ChannelResponseDto> channels = channelService.findAllByUserId(woody.getId());

            ChannelResponseDto updatedChannel =
                    channelService.update(
                            publicChannel.getId(),
                            ChannelUpdateRequestDto.of(
                                    "공지-수정",
                                    "수정된 공지 채널입니다."
                            )
                    );


            // ===================== Message 테스트 =====================

            System.out.println("\n===== Message =====");

            MessageResponseDto message =
                    messageService.create(
                            MessageCreateRequestDto.of(
                                    "안녕하세요.",
                                    woody.getId(),
                                    publicChannel.getId()
                            ),
                            List.of(
                                    BinaryContentCreateRequestDto.of(
                                            "message-attachment-1.txt",
                                            "first attachment"
                                                    .getBytes(StandardCharsets.UTF_8)
                                    ),
                                    BinaryContentCreateRequestDto.of(
                                            "message-attachment-2.txt",
                                            "second attachment"
                                                    .getBytes(StandardCharsets.UTF_8)
                                    )
                            )
                    );

            List<BinaryContentResponseDto> messageAttachments =
                    binaryContentService.findAllByIdIn(
                            message.getAttachmentIds()
                    );

            System.out.println(
                    "메시지 첨부파일 조회: messageId=" + message.getId()
                            + ", attachmentCount=" + messageAttachments.size()
            );

            MessageResponseDto foundMessage = messageService.findById(message.getId());

            List<MessageResponseDto> messages =
                    messageService.findAllByChannelId(
                            publicChannel.getId()
                    );

            MessageResponseDto updatedMessage =
                    messageService.update(
                            message.getId(),
                            MessageUpdateRequestDto.of(
                                    "안녕하세요. (수정됨)"
                            )
                    );

            // ===================== 삭제 테스트 =====================

            System.out.println("\n===== 삭제 =====");

            messageService.delete(message.getId());

            binaryContentService.delete(document.getId());

            channelService.delete(privateChannel.getId());

            channelService.delete(publicChannel.getId());

            userService.delete(alice.getId());

            userService.delete(woody.getId());

        } catch (CustomException exception) {
            System.out.println(
                    "[ERROR] "
                            + exception.getType().getResponse()
            );
        }
    }
}
