package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${discodeit.admin.username}")
    private String username;
    @Value("${discodeit.admin.email}")
    private String email;
    @Value("${discodeit.admin.password}")
    private String password;

    @Transactional
    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.existsByRole(Role.ADMIN)) {
            return;
        }
        if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
            log.warn("어드민 계정을 만들 수 없습니다. 이미 사용 중인 username 또는 email입니다.");
            return;
        }
        User admin = new User(username, email, passwordEncoder.encode(password), null);
        admin.updateRole(Role.ADMIN);
        new UserStatus(admin, Instant.now());
        userRepository.save(admin);
        log.info("어드민 계정 생성 완료");
    }
}
