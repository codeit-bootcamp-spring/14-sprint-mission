package com.sprint.mission.discodeit.config.bootstrap;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "bootstrap.admin")
public record BootstrapAdminProperties(@NotBlank String email,
                                       @NotBlank String username,
                                       @NotBlank String password) {
}
