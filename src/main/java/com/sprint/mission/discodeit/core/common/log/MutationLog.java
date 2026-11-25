package com.sprint.mission.discodeit.core.common.log;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MutationLog {
}
