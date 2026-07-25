package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class Application {
    public static void main(String[] args) {
        // JCF 저장 방식
        JCFUserRepository userRepository = JCFUserRepository.getInstance();
        JCFChannelRepository channelRepository = JCFChannelRepository.getInstance();
        JCFMessageRepository messageRepository = JCFMessageRepository.getInstance();


//        // File I/O 저장 방식
//        FileUserRepository userRepository = FileUserRepository.getInstance();
//        FileChannelRepository channelRepository = FileChannelRepository.getInstance();
//        FileMessageRepository messageRepository = FileMessageRepository.getInstance();


        UserService userService = new BasicUserService(userRepository);
        ChannelService channelService = new BasicChannelService(channelRepository);
        MessageService messageService =
                new BasicMessageService(
                        messageRepository,
                        userRepository,
                        channelRepository
                );

        // 1. 등록
        System.out.println("\n=== 1. 등록 ===");
        UserResponseDto user1 = userService.create(
                UserCreateRequestDto.of(
                        "userk",
                        "user1@email.com",
                        "pw123"
                )
        );
        UserResponseDto user2 = userService.create(
                UserCreateRequestDto.of(
                        "user2",
                        "user2@email.com",
                        "pw1234"
                )
        );
        UserResponseDto user3 = userService.create(
                UserCreateRequestDto.of(
                        "user3",
                        "user3@gmail.com",
                        "pw12345"
                )
        );
        System.out.println(user1);
        System.out.println(user2);
        System.out.println(user3);

        ChannelResponseDto channel1 = channelService.create(
                ChannelCreateRequestDto.of(
                        "general",
                        ChannelType.PUBLIC
                )
        );
        System.out.println(channel1);

        MessageResponseDto message1 = messageService.create(
                MessageCreateRequestDto.of(
                        "User1의 첫 메세지입니다",
                        user1.getId(),
                        channel1.getId()
                )
        );
        MessageResponseDto message2 = messageService.create(
                MessageCreateRequestDto.of(
                        "User2의 첫 메세지입니다",
                        user2.getId(),
                        channel1.getId()
                )
        );
        MessageResponseDto message3 = messageService.create(
                MessageCreateRequestDto.of(
                        "User3의 첫 general 채널 메세지입니다",
                        user3.getId(),
                        channel1.getId()
                )
        );
        MessageResponseDto message4 = messageService.create(
                MessageCreateRequestDto.of(
                        "User3의 두번째 general 채널 메세지입니다",
                        user3.getId(),
                        channel1.getId()
                )
        );
        System.out.println(message1);
        System.out.println(message2);
        System.out.println(message3);
        System.out.println(message4);


        // 2-1. 조회 (단건)
        System.out.println("\n=== 2-1. 조회(단건) ===");
        System.out.println("\tUser");
        System.out.println("\t\t" + userService.read(user1.getId()));
        System.out.println("\t\t" + userService.read(user2.getId()));
        System.out.println("\t\t" + userService.read(user3.getId()));
        System.out.println("\tChannel");
        System.out.println("\t\t" + channelService.read(channel1.getId()));
        System.out.println("\tMessage");
        System.out.println("\t\t" + messageService.read(message1.getId()));
        System.out.println("\t\t" + messageService.read(message2.getId()));
        System.out.println("\t\t" + messageService.read(message3.getId()));
        System.out.println("\t\t" + messageService.read(message4.getId()));

        // 2-2. 조회 (다건)
        System.out.println("\n=== 2-2. 조회(다건) ===");
        System.out.println("\tUser");
        System.out.println("\t\t" + userService.readAll());
        System.out.println("\tChannel");
        System.out.println("\t\t" + channelService.readAll());
        System.out.println("\tMessage");
        System.out.println("\t\t" + messageService.readAll());


        // 3. 수정
        System.out.println("\n=== 3. 수정 ===");
        userService.update(
                UserUpdateRequestDto.of(
                        user1.getId(),
                        "user1-1",
                        "first@email.com",
                        "pw1232"
                )
        );
        channelService.update(
                ChannelUpdateRequestDto.of(
                        channel1.getId(),
                        "new-general",
                        ChannelType.PRIVATE
                )
        );
        messageService.update(
                MessageUpdateRequestDto.of(
                        message3.getId(),
                        "User3의 첫 general 채널 메세지입니다 (수정됨)"
                )
        );

        // 4. 수정된 데이터 조회
        System.out.println("\n=== 4. 수정된 데이터 조회 ===");
        System.out.println(userService.read(user1.getId()));
        System.out.println(channelService.read(channel1.getId()));
        System.out.println(messageService.read(message3.getId()));

        // 5. 삭제
        System.out.println("\n=== 5. 삭제 ===");
        userService.delete(user1.getId());
        channelService.delete(channel1.getId());
        messageService.delete(message2.getId());
        messageService.delete(message1.getId());


        // 6. 조회를 통해 삭제되었는지 확인
        System.out.println("\n=== 6. 조회를 통해 삭제되었는지 확인 ===");
        try {
            userService.read(user1.getId());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            channelService.read(channel1.getId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            messageService.read(message2.getId());
            messageService.read(message1.getId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
