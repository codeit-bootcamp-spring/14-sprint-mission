package com.sprint.mission.discodeit.adapter.in.runner;

import com.sprint.mission.discodeit.application.user.in.UserApplication;
import com.sprint.mission.discodeit.config.bootstrap.BootstrapAdminProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminBootstrapRunner implements ApplicationRunner {

    private final UserApplication application;
    private final BootstrapAdminProperties properties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        application.createBootstrapAdmin(properties.username(), properties.email(), properties.password());
    }
}
