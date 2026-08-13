package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageResponse;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {
    static UserResponse setupUser(UserService userService) {
        UserCreateRequest request = new UserCreateRequest(
                "woody",
                "woody@codeit.com",
                "woody1234",
                null, null, null
        );
        return userService.create(request);
    }

    static ChannelResponse setupChannel(ChannelService channelService) {
        ChannelCreatePublicRequest request = new ChannelCreatePublicRequest("공지", "공지 채널입니다.");
        return channelService.createPublic(request);
    }
    static void messageCreateTest(MessageService messageService, ChannelResponse channel, UserResponse author) {
        MessageCreateRequest request = new MessageCreateRequest(
                "안녕하세요.",
                channel.id(),
                author.userId(),
                null
        );
        MessageResponse message = messageService.create(request);
        System.out.println("메시지 생성: " + message.id());
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        // 3. 셋업 및 테스트 (기존 코드와 동일)
        UserResponse user = setupUser(userService);
        ChannelResponse channel = setupChannel(channelService);

        messageCreateTest(messageService, channel, user);
    }
}
