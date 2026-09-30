package com.sprint.mission.discodeit.user.web;

import com.sprint.mission.discodeit.user.application.UserApplicationService;
import com.sprint.mission.discodeit.user.web.dto.req.UserLoginRequestDTO;
import com.sprint.mission.discodeit.user.web.dto.res.UserResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {
    private final UserApplicationService userApplicationService;

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(
        @RequestBody UserLoginRequestDTO request
    ){
        UserResponseDTO response = userApplicationService.login(request);

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }
}
