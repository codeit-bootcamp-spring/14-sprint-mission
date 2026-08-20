package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.IService.ChannelService;
import com.sprint.mission.discodeit.service.IService.MessageService;
import com.sprint.mission.discodeit.service.IService.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {
	static User setupUser(UserService userService) {
		User user = new User("민준", "minjun@naver.com",null);
		userService.create(user, );
		return user;
	}

	static Channel setupChannel(ChannelService channelService) {
		Channel channel = new Channel("GongJi");
		channelService.create(channel);
		return channel;
	}

	static void messageCreateTest(MessageService messageService, Channel channel, User author) {
		Message message = new Message("안녕하세요.", author.getId(),channel.getId());
		messageService.create(message);
		System.out.println("메시지 생성: " + message);
	}
	static void runTest(UserRepository userRepository,
		ChannelRepository channelRepository,
		MessageRepository messageRepository) {
		UserService userService = new BasicUserService(userRepository, "");
		ChannelService channelService = new BasicChannelService(channelRepository);
		MessageService messageService = new BasicMessageService(messageRepository, userRepository, channelRepository);

		User user = setupUser(userService);
		Channel channel = setupChannel(channelService);
		messageCreateTest(messageService, channel, user);
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context =SpringApplication.run(DiscodeitApplication.class, args);
		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

		User user = setupUser(userService);
		Channel channel = setupChannel(channelService);
		messageCreateTest(messageService, channel, user);
	}




}
