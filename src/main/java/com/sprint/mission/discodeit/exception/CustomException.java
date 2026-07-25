package com.sprint.mission.discodeit.exception;

public class CustomException extends Exception {
    private final ExceptionType type;

    public CustomException(ExceptionType type) {
        this.type = type;
    }
}
