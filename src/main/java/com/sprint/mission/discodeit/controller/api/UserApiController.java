package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.userDto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDto.UserRequest;
import com.sprint.mission.discodeit.dto.userDto.UserResponse;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/api/user/findAll")
    public ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> userList = userService.readAll();
        return ResponseEntity.ok(userList);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/user")
    public UserResponse regeister(@RequestBody UserCreateRequest request) {
        System.out.println("request = " + request);
        return userService.create(request);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/user/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        UserResponse response = userService.findById(id);
        return ResponseEntity.ok(response);
    }
    @RequestMapping(method = RequestMethod.DELETE, value = "/api/user/{id}")
    public void deleteById(@PathVariable UUID id) {
        userService.delete(id);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/api/user/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable UUID id,@RequestBody UserRequest request) {

        UserResponse response = userService.update(id, request);
        return ResponseEntity.ok(response);
    }

}
