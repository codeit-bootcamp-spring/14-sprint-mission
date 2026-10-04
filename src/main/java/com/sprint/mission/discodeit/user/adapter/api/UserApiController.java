package com.sprint.mission.discodeit.user.adapter.api;

import com.sprint.mission.discodeit.binarycontent.BinaryContentRequestMapper;
import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.user.application.dto.UserCreateRequest;
import com.sprint.mission.discodeit.user.application.dto.UserDto;
import com.sprint.mission.discodeit.user.application.dto.UserStatusDto;
import com.sprint.mission.discodeit.user.application.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.user.application.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.user.application.provided.command.UserModifier;
import com.sprint.mission.discodeit.user.application.provided.command.UserRegister;
import com.sprint.mission.discodeit.user.application.provided.command.UserRemover;
import com.sprint.mission.discodeit.user.application.provided.command.UserStatusCommand;
import com.sprint.mission.discodeit.user.application.provided.query.UserAllFinder;
import com.sprint.mission.discodeit.user.application.provided.query.UserFinder;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserApiController {

  private final UserRegister userRegister;
  private final UserModifier userModifier;
  private final UserRemover userRemover;
  private final UserFinder userFinder;
  private final UserAllFinder userAllFinder;
  private final UserStatusCommand userStatusCommand;
  private final BinaryContentRequestMapper binaryContentRequestMapper;


  @RequestMapping(method = RequestMethod.GET, value = "")
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> userList = userAllFinder.getAll();
    return ResponseEntity.ok(userList);
  }

  @RequestMapping(method = RequestMethod.PATCH, value = "/{userId}/userStatus")
  public ResponseEntity<UserStatusDto> updateUserStatusByUserId(@PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest request) {

    UserStatusDto response = userStatusCommand.update(userId, request);
    return ResponseEntity.ok(response);
  }

  @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDto> register(
      @RequestPart("userCreateRequest") UserCreateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {
    BinaryContentCreateRequest binaryContentCreateRequest = binaryContentRequestMapper.toCreateRequest(
        profile);
    UserDto response = userRegister.register(request, binaryContentCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  public ResponseEntity<UserDto> findById(@PathVariable UUID id) {
    UserDto response = userFinder.getById(id);
    return ResponseEntity.ok(response);
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
  public ResponseEntity<Void> deleteById(@PathVariable UUID userId) {
    userRemover.delete(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @RequestMapping(method = RequestMethod.PATCH, value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDto> update(@PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {
    BinaryContentCreateRequest binaryContentCreateRequest = binaryContentRequestMapper.toCreateRequest(
        profile);
    UserDto response = userModifier.modify(userId, request, binaryContentCreateRequest);
    return ResponseEntity.ok(response);
  }

}
