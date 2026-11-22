package com.sprint.mission.discodeit.user.application;

import com.sprint.mission.discodeit.global.exception.CustomException;
import com.sprint.mission.discodeit.global.security.JwtTokenProvider;
import com.sprint.mission.discodeit.global.security.exception.InvalidTokenException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.user.domain.entity.User;
import com.sprint.mission.discodeit.user.domain.repository.UserRepository;
import com.sprint.mission.discodeit.user.web.dto.res.JwtResponse;
import com.sprint.mission.discodeit.user.web.dto.res.RefreshTokenResult;
import com.sprint.mission.discodeit.user.web.dto.res.UserResponseDTO;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthApplicationService {
  private final JwtTokenProvider jwtTokenProvider;
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Transactional(readOnly = true)
  public RefreshTokenResult refreshAccessToken(String prevRefreshToken){
    UUID userId = jwtTokenProvider.getUserIdFromRefreshToken(prevRefreshToken);
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new InvalidTokenException("토큰값의 사용자를 찾을 수 없습니다."));

    UserResponseDTO userResponse = userMapper.toResponse(user);
    String accessToken = jwtTokenProvider.generateAccessToken(userId, user.getRole());
    String postRefreshToken = jwtTokenProvider.generateRefreshToken(userId);

    return RefreshTokenResult.of(JwtResponse.of(userResponse,accessToken), postRefreshToken);
  }
}
