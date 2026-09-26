package com.sprint.mission.discodeit.adaptor.api;

import com.sprint.mission.discodeit.application.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.application.user.dto.UserDto;
import com.sprint.mission.discodeit.application.user.dto.UserStatusDto;
import com.sprint.mission.discodeit.application.user.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.application.user.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.application.user.provided.command.UserModifier;
import com.sprint.mission.discodeit.application.user.provided.command.UserRegister;
import com.sprint.mission.discodeit.application.user.provided.command.UserRemover;
import com.sprint.mission.discodeit.application.user.provided.command.UserStatusCommand;
import com.sprint.mission.discodeit.application.user.provided.query.UserAllFinder;
import com.sprint.mission.discodeit.application.user.provided.query.UserFinder;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

  @RequestMapping(method = RequestMethod.POST, value = "")
  public ResponseEntity<UserDto> register(@RequestBody UserCreateRequest request) {
    UserDto response = userRegister.register(request);
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

  @RequestMapping(method = RequestMethod.PATCH, value = "/{userId}")
  public ResponseEntity<UserDto> update(@PathVariable UUID userId,
      @RequestBody UserUpdateRequest request) {

    UserDto response = userModifier.modify(userId, request);
    return ResponseEntity.ok(response);
  }

}
