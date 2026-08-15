package com.sprint.mission.discodeit.controller.page;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.service.application.user.UserApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserPageController {

    private final UserApplicationService userApplicationService;

    @GetMapping("/users")
    public String retrieveAll() {
        return "forward:user-list.html";
    }
}
