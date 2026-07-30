package com.sprint.mission.discodeit;

import static com.sprint.mission.discodeit.service.basic.BasicUserService.ERROR_USER_NOT_FOUND;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import java.util.List;
import java.util.UUID;

public class JavaApplication {
    static User setupUser(UserService userService) {
        return userService.create("woody", "woody@codeit.com", "woody1234");
    }

    static Channel setupChannel(ChannelService channelService) {
        return channelService.create(UUID.randomUUID(), "채널1");
    }

    static void messageCreateTest(User user, Channel channel, MessageService messageService) {
        Message message = messageService.create(user.getId(), channel.getId(), "채널1 공지");
        System.out.println("메시지 생성: " + message);
    }

    public static void main(String[] args) {
        // 레포지토리 객체 생성(싱글톤 패턴을 사용하기 위해 주석 처리)
//        UserRepository userRepository = new FileUserRepository();
//        ChannelRepository channelRepository = new FileChannelRepository();
//        MessageRepository messageRepository = new FileMessageRepository();

        // 싱글톤 패턴 사용.
        UserRepository userRepository = FileUserRepository.getInstance();
        ChannelRepository channelRepository = FileChannelRepository.getInstance();
        MessageRepository messageRepository = FileMessageRepository.getInstance();

        // 실행할 때마다 데이터가 추가되는 문제를 해결하기 위해 만듦.
        userRepository.deleteAll();
        channelRepository.deleteAll();
        messageRepository.deleteAll();
        // 서비스 주입
        UserService userService = new BasicUserService(userRepository);
        ChannelService channelService = new BasicChannelService(channelRepository);
        MessageService messageService = new BasicMessageService(messageRepository, userService, channelService);

        // JCF 방법
//        UserService userService = new JCFUserService();
//        ChannelService channelService = new JCFChannelService();
//        MessageService messageService = new JCFMessageService(userService, channelService);

//         sprint 2-2 테스트
        System.out.println("\n2-2 스프린트 미션 결과");
        System.out.println("=== 과제 템플릿 기본 테스트 시작 ===");
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        messageCreateTest(user, channel, messageService);
        System.out.println("=== 과제 템플릿 기본 테스트 완료 ===\n");


//        // sprint 2-1 테스트
        System.out.println("\n2-1 스프린트 미션 결과");
        System.out.println("=== 생성(create) 테스트 ===");
        User user1 = userService.create("가나다", "ga@codeit.com", "나다");
        User user2 = userService.create("라마바", "la@codeit.com", "마바");

        System.out.println("user1 = " + user1);
        System.out.println("user2 = " + user2);

        Channel channel1 = channelService.create(user1.getId(), "한글 초급");
        messageService.create(user1.getId(), channel1.getId(), "세종대왕 굿");
        System.out.println("channel1 = " + channel1);

        System.out.println("=== 수정(update) 테스트 ===");
        userService.update(user2.getId(), "랑망방", "rang@codeit.com", "망방");

        User update1 = userService.read(user2.getId())
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND));
        System.out.println("update1 = " + update1);

        System.out.println("=== 조회(read) 테스트 ===");
        User read1 = userService.read(user1.getId())
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND));
        System.out.println("read1 = " + read1);

        System.out.println("=== 삭제(delete) 테스트 ===");
        userService.delete(user2.getId());
        List<User> users = userService.readAll();
        System.out.println("users = " + users);

        System.out.println("=== 예외 처리(검증) 테스트 ===");
        try {
            messageService.create(UUID.randomUUID(), channel1.getId(), "유령 유저의 메시지");
        } catch (IllegalArgumentException e) {
            System.out.println("예외 발생 성공: " + e.getMessage());
        }

        try {
            messageService.create(user1.getId(), UUID.randomUUID(), "유령 유저의 메시지");
        } catch (IllegalArgumentException e) {
            System.out.println("예외 발생 성공: " + e.getMessage());
        }
    }
}
