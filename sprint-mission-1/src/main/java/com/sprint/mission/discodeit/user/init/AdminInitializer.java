package com.sprint.mission.discodeit.user.init;

import com.sprint.mission.discodeit.user.entity.Role;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${discodeit.admin.username}")
    private String username;
    @Value("${discodeit.admin.password}")
    private String password;
    @Value("${discodeit.admin.email}")
    private String email;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.existsByRole(Role.ADMIN)) {
            return;
        }
        User admin = User.create(username, passwordEncoder.encode(password), email);
        admin.updateRole(Role.ADMIN);
        userRepository.save(admin);
        log.info("어드민 계정 초기화: {}", username);
    }
}