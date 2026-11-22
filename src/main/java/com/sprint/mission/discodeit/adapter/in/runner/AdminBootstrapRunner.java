package com.sprint.mission.discodeit.adapter.in.runner;

import com.sprint.mission.discodeit.core.application.user.port.in.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminBootstrapRunner implements ApplicationRunner {

    private final UserUseCase useCase;
    private final BootstrapAdminProperties properties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        useCase.createBootstrapAdmin(properties.username(), properties.email(), properties.password());
    }
}
