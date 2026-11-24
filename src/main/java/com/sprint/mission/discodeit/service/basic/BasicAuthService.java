package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    @Value("${discodeit.admin.username:admin}")
    private String username;
    @Value("${discodeit.admin.password:admin123}")
    private String password;
    @Value("${discodeit.admin.email:admin@codeit.com}")
    private String email;

    @Transactional
    @Override
    public UserDto initAdmin() {
        // 이미 ADMIN 계정이 있거나 같은 아이디/이메일이 있으면 중복 생성하지 않고 건너뜀
        if (userRepository.existsByRole(Role.ADMIN)
            || userRepository.existsByEmail(email)
            || userRepository.existsByUsername(username)) {
            log.debug("이미 어드민 계정이 존재합니다.");
            return null;
        }
        // 비밀번호는 반드시 BCrypt 해시로 암호화해서 저장!
        String encodedPassword = passwordEncoder.encode(password);
        User admin = new User(username, email, encodedPassword, null);
        admin.updateRole(Role.ADMIN);
        // UserMapper.toDto()에서 user.getStatus().isOnline()을 쓰므로 UserStatus도 꼭 함께 생성!
        new UserStatus(admin, Instant.now());
        userRepository.save(admin);
        UserDto adminDto = userMapper.toDto(admin);
        log.info("어드민이 초기화되었습니다. {}", adminDto);
        return adminDto;
    }

    @Transactional
    @Override
    public UserDto updateRole(RoleUpdateRequest request) {
        // 1. 대상 사용자 조회 (없으면 404 예외)
        User user = userRepository.findById(request.userId())
            .orElseThrow(() -> UserNotFoundException.withId(request.userId()));
        // 2. 권한 변경 (트랜잭션이 끝날 때 JPA 변경 감지로 DB에 자동 UPDATE)
        user.updateRole(request.newRole());
        log.info("사용자 권한 변경 완료: userId={}, newRole={}", user.getId(), user.getRole());
        return userMapper.toDto(user);
    }
}
