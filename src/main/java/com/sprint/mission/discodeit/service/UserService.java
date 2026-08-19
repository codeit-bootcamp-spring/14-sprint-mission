package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.dto.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface UserService {


   UserResponseDto create(UserCreateRequestDto userRequest, BinaryContentCreateRequestDto profileRequest);
   UserResponseDto read(UUID id);
   List<UserResponseDto> readAll();
   UserResponseDto update(UUID id, UserUpdateRequestDto updateRequest, BinaryContentCreateRequestDto profileRequest);
   void delete(UUID id);


}
