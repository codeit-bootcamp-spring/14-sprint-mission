package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class DiscodeitApplication {
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

//		UserService userService = new BasicUserService(userRepository);
//		ChannelService channelService = new BasicChannelService(channelRepository);
//		MessageService messageService = new BasicMessageService(messageRepository, channelRepository, userRepository);
// 		이게 원래 하던거

		// 스프링 부트 실행 및
		ApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		// 서비스 초기화
		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);
		/*
		컨트롤러에서는 이렇게 초기화함
		UserService userService;
		ChannelService channelService;
		MessageService messageService;
		 */

		// 셋업
		User user = setupUser(userService);
		Channel channel = setupChannel(channelService, user);

		// 테스트
		System.out.println("=== Basic*Service & Repository 템플릿 테스트 시작 ===");
		messageCreateTest(messageService, channel, user);
		System.out.println("=== 템플릿 테스트 완료 ===");
	}
}

