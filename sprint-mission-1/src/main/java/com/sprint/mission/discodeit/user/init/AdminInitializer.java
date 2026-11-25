package com.sprint.mission.discodeit.user.init;

import com.sprint.mission.discodeit.user.entity.Role;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
        // 같은 username/email의 일반 계정이 있으면 중복 생성하지 않는다 (user_name에 UNIQUE 제약 없음)
        if (userRepository.findByUserName(username).isPresent()
            || userRepository.findByEmail(email).isPresent()) {
            log.warn("어드민 계정 초기화 건너뜀: username 또는 email이 이미 사용 중입니다. username={}",
                username);
            return;
        }
        User admin = User.create(username, passwordEncoder.encode(password), email);
        admin.updateRole(Role.ADMIN);
        userRepository.save(admin);
        log.info("어드민 계정 초기화: {}", username);
    }
}
