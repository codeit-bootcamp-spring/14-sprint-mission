package com.sprint.mission.discodeit.web.controller.auth;

import com.sprint.mission.discodeit.domain.entity.User;
import com.sprint.mission.discodeit.domain.service.application.UserServiceApp;
import com.sprint.mission.discodeit.web.controller.dto.req.UserLoginRequestDTO;
import com.sprint.mission.discodeit.web.controller.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final UserServiceApp userServiceApp;

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody UserLoginRequestDTO userLoginRequestDTO, HttpServletRequest request){
        User user = userServiceApp.login(userLoginRequestDTO);

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_USER, user.getId());

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
