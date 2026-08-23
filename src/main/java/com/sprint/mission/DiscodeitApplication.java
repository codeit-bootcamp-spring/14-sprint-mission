package com.sprint.mission;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
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
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {
	static UserResponseDto setupUser(UserService userService) {
		UserCreateRequestDto request = new UserCreateRequestDto("민준", "minjun@naver.com", "password123");
		return userService.create(request, null);
	}

	static ChannelResponseDto setupChannel(ChannelService channelService) {
		PublicChannelCreateRequestDto request = new PublicChannelCreateRequestDto("GongJi", "설명");
		return channelService.createPublic(request);
	}

	static void messageCreateTest(MessageService messageService, ChannelResponseDto channel, UserResponseDto user) {
		MessageCreateRequestDto request = new MessageCreateRequestDto("안녕하세요", user.id(), channel.id());
		MessageResponseDto message = messageService.create(request, null);
		System.out.println("생성된 메시지: " + message);
	}


	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

		UserResponseDto user = setupUser(userService);
		ChannelResponseDto channel = setupChannel(channelService);
		messageCreateTest(messageService, channel, user);

		// 조회 테스트
		UserResponseDto foundUser = userService.read(user.id());
		System.out.println("조회된 유저: " + foundUser);

		List<UserResponseDto> allUsers = userService.readAll();
		System.out.println("전체 유저 수: " + allUsers.size());

		// 수정 테스트
		UserUpdateRequestDto updateRequest = new UserUpdateRequestDto("민준2", null);
		UserResponseDto updatedUser = userService.update(user.id(), updateRequest, null);
		System.out.println("수정된 유저: " + updatedUser);

		// 채널 메시지 목록 조회 테스트
		List<MessageResponseDto> messages = messageService.findAllByChannelId(channel.id());
		System.out.println("채널 메시지 목록: " + messages);

		// 삭제 테스트
		userService.delete(user.id());
		System.out.println("유저 삭제 완료");

		// 삭제 후 재조회 -> 예외 발생하는지 확인
		try {
			userService.read(user.id());
		} catch (Exception e) {
			System.out.println("삭제 확인됨, 예상된 예외: " + e.getMessage());
		}

	}




}
