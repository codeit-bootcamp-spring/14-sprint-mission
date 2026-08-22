package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.userDto.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscodeitApplication.class, args);
	}
	UserCreateRequest userCreateRequest = new UserCreateRequest("codeit", "codeit", "codeit.com");
	UserCreateRequest userCreateRequest2 = new UserCreateRequest("codeit2", "codeit2", "codeit.com");
	UserCreateRequest userCreateRequest3 = new UserCreateRequest("codeit3", "codeit3", "codeit.com");
	
}
