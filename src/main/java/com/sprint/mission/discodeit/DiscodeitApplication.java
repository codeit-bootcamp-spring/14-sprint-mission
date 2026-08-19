package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.dto.channel.ChannelCreationDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateNameDto;
import com.sprint.mission.discodeit.dto.message.MessageCreationDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.user.UserCreationDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.exception.CustomException;
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
		SpringApplication.run(DiscodeitApplication.class, args);
	}

}
