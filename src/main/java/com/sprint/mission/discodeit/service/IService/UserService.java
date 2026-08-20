package com.sprint.mission.discodeit.service.IService;


import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
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
