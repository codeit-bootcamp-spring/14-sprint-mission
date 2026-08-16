package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

//@SpringBootApplication
public class Application {
    static User setupUser(UserService userService) {
        User user = userService.create("woody", "woody@codeit.com", "1234");

        return user;
    }

    static Channel setupChannel(ChannelService channelService) {
        Channel channel = channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.");

        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());

        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {
        // 서비스 초기화
        // TODO Basic*Service 구현체를 초기화하세요.
        // JCF 저장 방식
//        UserService userService = new BasicUserService(new JCFUserRepository());
//        ChannelService channelService = new BasicChannelService(new JCFChannelRepository());
//        MessageService messageService = new BasicMessageService(new JCFMessageRepository());

        // File 저장 방식
        UserService userService = new BasicUserService(new FileUserRepository());
        ChannelService channelService = new BasicChannelService(new FileChannelRepository());
        MessageService messageService = new BasicMessageService(new FileMessageRepository());

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);

        // 테스트
        messageCreateTest(messageService, channel, user);
    }
}
