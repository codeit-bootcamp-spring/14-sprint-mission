package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channeldto.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channeldto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.messagedto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.messagedto.MessageResponseDto;
import com.sprint.mission.discodeit.dto.userdto.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.userdto.UserResponseDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscodeitApplication.class, args);
	}
}
