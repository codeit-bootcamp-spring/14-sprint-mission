package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicServiceFactory;

public class JavaApplication {

    static User setupUser(UserService userService) {
        User user = userService.createUser("woody@codeit.com", "woody");
        return user;
    }

    static Channel setupChannel(ChannelService channelService, User user) {
        Channel channel = channelService.createChannel("공지", user);
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.createMessage("안녕하세요.", channel, author);
        System.out.println("메시지 생성 성공! Message ID: " + message.getId());
        System.out.println("메시지 내용: " + message.getValues());
        System.out.println("보낸 사람: " + message.getSender().getName());
        System.out.println("채널명: " + message.getChannel().getChannelName());
    }

    public static void main(String[] args) {
        // 서비스 초기화
        BasicServiceFactory factory = new BasicServiceFactory();

        UserService userService = factory.getUserService();
        ChannelService channelService = factory.getChannelService();
        MessageService messageService = factory.getMessageService();

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService, user);

        // 테스트 실행
        System.out.println("=== Basic*Service & Repository 템플릿 테스트 시작 ===");
        messageCreateTest(messageService, channel, user);
        System.out.println("=== 템플릿 테스트 완료 ===");
    }
}
