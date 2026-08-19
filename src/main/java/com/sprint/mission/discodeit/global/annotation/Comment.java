package com.sprint.mission.discodeit.global.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/*
       혹시나 주석용
 */
//런타임말고 이렇게 컴파일타임에만 올라오게 해야 메모리 줄일 수 있다는데 맞는지(굳이 안해도되나?)
@Retention(RetentionPolicy.SOURCE)
public @interface Comment {
    String value();
}