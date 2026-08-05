package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.dto.channel.ChannelCreationDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateNameDto;
import com.sprint.mission.discodeit.dto.message.MessageCreationDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.user.UserCreationDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.NoSuchElementException;

@SpringBootApplication
public class DiscodeitApplication {
	public static void main(String[] args) {
//		SpringApplication.run(DiscodeitApplication.class, args);
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

		User u1 = userService.createAccount(new UserCreationDto("user1", "user1", "password", null));
		User u2 = userService.createAccount(new UserCreationDto("user2", "user2", "password", null));
		User u3 = userService.createAccount(new UserCreationDto("user3", "user3", "password", null));
		User u4 = userService.createAccount(new UserCreationDto("ToBeDeleted", "ToBeDeleted", "password", null));
		List<User> users = List.of(u1, u2, u3, u4);

		Channel c1 = channelService.createChannel(new ChannelCreationDto(ChannelType.PRIVATE, "channel1", List.of(u1.getId(), u2.getId())));
		Channel c2 = channelService.createChannel(new ChannelCreationDto(ChannelType.PRIVATE, "ToBeDeleted", List.of(u3.getId(), u4.getId())));
		List<Channel> channels = List.of(c1, c2);

		Message m1 = messageService.createMessage(new MessageCreationDto("user1 to channel1", u1.getId(), c1.getId(), null));

		BinaryContent bc = new BinaryContent(null);
		Message m2 = messageService.createMessage(new MessageCreationDto("ToBeUpdatedAndDeleted", u3.getId(), c2.getId(), List.of(bc.getId())));
		List<Message> messages = List.of(m1, m2);

		userServiceTest(userService, users);
		messageServiceTest(messageService, messages);
		channelServiceTest(channelService, channels);
		serviceIntegrationTest(userService, channelService, messageService);
	}


	static void userServiceTest(UserService userService, List<User> users) {
		User u1 = users.get(0);
		User u2 = users.get(1);
		User u3 = users.get(2);
		User u4 = users.get(3);

		System.out.println("\n\n\n============= UserService =============");
		System.out.println("1. User 생성");
		System.out.println("2. User 전체 조회");
		System.out.println(userService.getAllUsers());

		System.out.println("3. User 이름 갱신");
		System.out.println("4. 갱신된 User 단일 조회");
		userService.updateUser(u1.getId(), new UserUpdateDto("user1_updated", "email_updated", "password_updated", null));
		System.out.println("userService.getUser(u1.getId()) = " + userService.getUser(u1.getId()));

		System.out.println("5. User 삭제");
		System.out.println("6. 삭제된 User 단일 조회");
		try {
			userService.deleteAccount(u4.getId());
			System.out.println("userService.getUser(u4) = " + userService.getUser(u4.getId()));
		} catch (NoSuchElementException e) {
			System.out.println("6. 삭제된 User 조회되지 않음을 검증");
		}

	}

	static void channelServiceTest(ChannelService channelService, List<Channel> channels) {
		Channel c1 = channels.get(0);
		Channel c2 = channels.get(1);

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
		try {
			channelService.deleteChannel(c2.getId());
			System.out.println("channelService.getChannel(c2.getId()) = " + channelService.getChannel(c2.getId()));
		} catch (NoSuchElementException e) {
			System.out.println("6. 삭제된 Channel 조회되지 않음 검증 완료");
		}

	}

	static void messageServiceTest(MessageService messageService, List<Message> messages) {
		Message m1 = messages.get(0);
		Message m2 = messages.get(1);

		System.out.println("\n\n\n============= MessageService =============");
		System.out.println("1. Message 생성");
		System.out.println("2. Message 전체 조회");
		System.out.println(messageService.getAllMessages());

		System.out.println("3. Message 내용 갱신");
		System.out.println("4. 갱신된 Message 단일 조회");
		messageService.updateMessage(m1.getId(), new MessageUpdateDto("UPDATED"));
		System.out.println("messageService.getMessage(m1.getId()) = " + messageService.getMessage(m1.getId()));

		System.out.println("5. Message 삭제");
		System.out.println("6. 삭제된 Message 단일 조회");
		messageService.deleteMessage(m2.getId());
		System.out.println("messageService.getMessage(m2.getId()) = " + messageService.getMessage(m2.getId()));
	}

	static void serviceIntegrationTest(UserService userService,
									   ChannelService channelService,
									   MessageService messageService) {
		System.out.println("\n\n\n============= ServiceIntegrationTest =============");

		// User 삭제 시, 해당 User의 Message, 그리고 Channel의 user list 에서도 삭제됨을 검증 완료
		System.out.println("1. User 삭제 시, 해당 User의 Message, 그리고 Channel의 user list 에서도 삭제됨을 검증");
		User userToBeDeleted = userService.createAccount(new UserCreationDto("userToBeDeleted", "userToBeDeleted", "password", null));
		Channel channelToBeDeleted = channelService.createChannel(new ChannelCreationDto(ChannelType.PRIVATE, "channelToBeDeleted", List.of(userToBeDeleted.getId())));
		Message msgToBeDeleted = messageService.createMessage(new MessageCreationDto("msgToBeDeleted", userToBeDeleted.getId(), channelToBeDeleted.getId(), null));

		userService.deleteAccount(userToBeDeleted.getId());
		System.out.println("channelService.getChannel(channelToBeDeleted.getId()) = " + channelService.getChannel(channelToBeDeleted.getId()));
		System.out.println("messageService.getMessage(msgToBeDeleted.getId()) = " + messageService.getMessage(msgToBeDeleted.getId()));

		// Channel 삭제 시, 내부 Message가 삭제됨을 검증
		System.out.println("2. Channel 삭제 시, 내부 Message가 삭제됨을 검증");
		User u = userService.createAccount(new UserCreationDto("u", "u", "password", null));
		Channel channelToBeDeleted2 = channelService.createChannel(new ChannelCreationDto(ChannelType.PRIVATE, "channelToBeDeleted2", List.of(u.getId())));
		Message msgToBeDeleted2 = messageService.createMessage(new MessageCreationDto("msgToBeDeleted", u.getId(), channelToBeDeleted2.getId(), null));

		channelService.deleteChannel(channelToBeDeleted2.getId());
		System.out.println("messageService.getMessage(msgToBeDeleted2.getId()) = " + messageService.getMessage(msgToBeDeleted2.getId()));

		// Message 생성 시, User가 channel에 소속됨을 검증
		try {
			User notInChannel = new User("notInChannel", "email", "password", null);
			Channel channel = channelService.createChannel(new ChannelCreationDto(ChannelType.PRIVATE, "test channel", List.of()));
			messageService.createMessage(new MessageCreationDto("", notInChannel.getId(), channel.getId(), null));
		} catch (IllegalArgumentException e) {
			System.out.println("3. Message 생성 시, User가 channel에 소속됨을 검증 성공");
		}

		// Message 생성 시, Channel이 Repository에 소속됨을 검증
		try {
			User user = userService.createAccount(new UserCreationDto("user", "user", "password", null));
			Channel notRegistered = new Channel("notRegistered", List.of(user.getId()));
			messageService.createMessage(new MessageCreationDto("", user.getId(), notRegistered.getId(), null));
		} catch (IllegalArgumentException e) {
			System.out.println("4. Message 생성 시, Channel이 Repository에 소속됨을 검증");
		}

		// Channel 생성 시, User가 Repository에 등록된 유저임을 검증
		try {
			User notRegistered = new User("notRegistered", "email", "password", null);
			channelService.createChannel(new ChannelCreationDto(ChannelType.PRIVATE, "not valid channel", List.of(notRegistered.getId())));
		} catch (IllegalArgumentException e) {
			System.out.println("5. Channel 생성 시, User가 repository 등록된 유저임을 검증 성공");
		}
	}

}
