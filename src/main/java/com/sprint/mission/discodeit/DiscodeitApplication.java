package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {

//	static User setupUser(UserService userService) {
//		User user = userService.create("woody", "woody@codeit.com", "woody1234");
//		return user;
//	}
//
//	static Channel setupChannel(ChannelService channelService) {
//		Channel channel = channelService.create(ChannelType.PUBLIC);
//		return channel;
//	}
//
//	static void messageCreateTest(MessageService messageService, Channel channel, User author) {
//		Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());
//		System.out.println("메시지 생성: " + message.getId());
//	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
//
//		// 서비스 초기화
//		// TODO context에서 Bean을 조회하여 각 서비스 구현체 할당 코드 작성하세요.
//		UserService userService = context.getBean(BasicUserService.class);
//		ChannelService channelService = context.getBean(BasicChannelService.class);
//		MessageService messageService = context.getBean(BasicMessageService.class);
//
//		// 셋업
//		User user = setupUser(userService);
//		Channel channel = setupChannel(channelService);
//		// 테스트
//		messageCreateTest(messageService, channel, user);

	}

}
