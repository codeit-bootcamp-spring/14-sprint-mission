package com.sprint.mission.discodeit.core.common.annotation;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Service
@Validated
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationService {
}
