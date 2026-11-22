package com.sprint.mission.discodeit.adapter.out.storage.config.aws;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "discodeit.storage.s3")
public record AwsProperties(@NotBlank String region,
                            @NotBlank String accessKey,
                            @NotBlank String secretKey,
                            @NotBlank String bucket) {}
