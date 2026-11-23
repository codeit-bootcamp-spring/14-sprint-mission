package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.IService.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final UserService userService;

    @Value("${discodeit.admin.username}") private String username;
    @Value("${discodeit.admin.email}")    private String email;
    @Value("${discodeit.admin.password}") private String password;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.existsByRole(Role.ADMIN)) {
            log.info("어드민 계정이 이미 존재합니다. 초기화를 건너뜁니다.");
            return;
        }

        UserResponseDto created = userService.create(
            new UserCreateRequestDto(username, email, password), null);

        User admin = userRepository.findById(created.id()).orElseThrow();
        admin.updateRole(Role.ADMIN);

        log.info("어드민 계정 초기화 완료: username={}", username);
    }
}
