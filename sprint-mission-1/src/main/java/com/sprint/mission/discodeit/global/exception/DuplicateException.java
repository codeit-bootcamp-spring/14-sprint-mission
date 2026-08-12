package com.sprint.mission.discodeit.global.exception;

import java.util.UUID;

public class DuplicateException extends RuntimeException {

    public DuplicateException(String message) {
        super(message);
    }

    public static DuplicateException channel(String name) {
        return new DuplicateException("이미 존재하는 채널입니다 - 이름: " + name);
    }

    public static DuplicateException userName(String name) {
        return new DuplicateException("이미 존재하는 유저입니다 - 이름: " + name);
    }

    public static DuplicateException userEmail(String email) {
        return new DuplicateException("이미 존재하는 이메일입니다 - 이메일: " + email);
    }

    public static DuplicateException userStatus(UUID id) {
        return new DuplicateException("이미 존재하는 유저 상태입니다 - id: " + id);
    }
}
