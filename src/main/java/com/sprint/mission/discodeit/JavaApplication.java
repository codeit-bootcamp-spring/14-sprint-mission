package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.channel.ChannelCreationDto;
import com.sprint.mission.discodeit.entity.dto.channel.ChannelUpdateNameDto;
import com.sprint.mission.discodeit.entity.dto.message.MessageCreationDto;
import com.sprint.mission.discodeit.entity.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.dto.user.UserCreationDto;
import com.sprint.mission.discodeit.entity.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;

public class JavaApplication {

    static UserService userService = AppConfig.userService();
    static MessageService messageService = AppConfig.messageService();
    static ChannelService channelService = AppConfig.channelService();

    static User u1 = userService.createAccount(new UserCreationDto("user1"));
    static User u2 = userService.createAccount(new UserCreationDto("user2"));
    static User u3 = userService.createAccount(new UserCreationDto("user3"));
    static User u4 = userService.createAccount(new UserCreationDto("ToBeDeleted"));

    static Channel c1 = channelService.createChannel(new ChannelCreationDto("channel1", List.of(u1.getId(), u2.getId())));
    static Channel c2 = channelService.createChannel(new ChannelCreationDto("ToBeDeleted", List.of(u3.getId(), u4.getId())));

    static Message m1 = messageService.createMessage(new MessageCreationDto("user1 to channel1", u1.getId(), c1.getId()));
    static Message m2 = messageService.createMessage(new MessageCreationDto("ToBeUpdatedAndDeleted", u3.getId(), c2.getId()));

    public static void main(String[] args) {
        userServiceTest(userService);
        channelServiceTest(channelService);
        messageServiceTest(messageService);
        serviceIntegrationTest(userService, channelService, messageService);
    }

    static void beforeEach() {

    }


    static void userServiceTest(UserService userService) {
        System.out.println("\n\n\n============= UserService =============");
        System.out.println("1. User 생성");
        System.out.println("2. User 전체 조회");
        System.out.println(userService.getAllUsers());

        System.out.println("3. User 이름 갱신");
        System.out.println("4. 갱신된 User 단일 조회");
        userService.updateUser(u1.getId(), new UserUpdateDto("user1_updated"));
        System.out.println("userService.getUser(u1.getId()) = " + userService.getUser(u1.getId()));

        System.out.println("5. User 삭제");
        System.out.println("6. 삭제된 User 단일 조회");
        userService.deleteAccount(u4.getId());
        System.out.println("userService.getUser(u4) = " + userService.getUser(u4.getId()));
    }

    static void channelServiceTest(ChannelService channelService) {
        System.out.println("\n\n\n============= ChannelService =============");
        System.out.println("1. Channel 생성");
        System.out.println("2. Channel 전체 조회");
        System.out.println(channelService.getAllChannels());

        System.out.println("3. Channel 이름 갱신");
        System.out.println("4. 갱신된 Channel 단일 조회");
        channelService.updateChannelName(c1.getId(), new ChannelUpdateNameDto("c1_updated"));
        System.out.println("channelService.getChannel(c1.getId()) = " + channelService.getChannel(c1.getId()));

        System.out.println("5. Channel 삭제");
        System.out.println("6. 삭제된 Channel 단일 조회");
        channelService.deleteChannel(c2.getId());
        System.out.println("channelService.getChannel(c2.getId()) = " + channelService.getChannel(c2.getId()));

        // TODO 채널 소속 유저 추가/삭제 기능 추가 후 테스트
    }

    static void messageServiceTest(MessageService messageService) {
        System.out.println("\n\n\n============= MessageService =============");
        System.out.println("1. Message 생성");
        System.out.println("2. Message 전체 조회");
        System.out.println(messageService.getAllMessages());

        System.out.println("3. Message 내용 갱신");
        System.out.println("4. 갱신된 Message 단일 조회");
        messageService.updateMessage(m2.getId(), new MessageUpdateDto("UPDATED"));
        System.out.println("messageService.getMessage(m2.) = " + messageService.getMessage(m2.getId()));

        System.out.println("5. Message 삭제");
        System.out.println("6. 삭제된 Message 단일 조회");
        messageService.deleteMessage(m2.getId());
        System.out.println("messageService.getMessage(m2.getId()) = " + messageService.getMessage(m2.getId()));
    }

    static void serviceIntegrationTest(UserService userService,
                                       ChannelService channelService,
                                       MessageService messageService) {
        System.out.println("\n\n\n============= ServiceIntegrationTest =============");
        // TODO 1. User 삭제 시, 해당 User의 Message, 그리고 Channel의 user list 에서도 삭제됨을 검증
        // TODO 2. Channel에 속하지 않은 User가 Message 보낼 수 없음을 검증
        User notInChannel = new User("notInChannel");
        Channel notUserChannel = channelService.createChannel(new ChannelCreationDto("test channel", List.of()));
        messageService.createMessage(new MessageCreationDto("", notInChannel.getId(), notUserChannel.getId()));

        // TODO 3. Message 생성 시, {User가 repository에 등록됨, User가 Channel에 소속됨}을 검증


        // TODO 4. Channel 생성 시, User가 repository에 등록된 유저임을 검증 (O)
        try {
            User notRegistered = new User("notRegistered");
            channelService.createChannel(new ChannelCreationDto("not valid channel", List.of(notRegistered.getId())));
        } catch (IllegalArgumentException e) {
            System.out.println("Channel 생성 시, User가 reposiotry에 등록된 유저임을 검증");
        }

    }
}
