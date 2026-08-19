package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserCreationDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequestMapping(value = "/api/users")
@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final BasicUserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public UserDto create(@Valid @RequestBody UserCreationDto request) {
        return userService.createAccount(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getProfileId()
        );
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public UserDto update(@PathVariable UUID id,
                                  @Valid @RequestBody UserUpdateDto request) {
        return userService.updateUser(
                id,
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getProfileId()
        );
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public UserDto delete(@PathVariable UUID id) {
        return userService.deleteAccount(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/findAll")
    public ResponseEntity<List<UserDto>> readAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getAllUsers());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public UserDto read(@PathVariable UUID id) {
        return userService.getUser(id);
    }
}
