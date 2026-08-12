package com.sprint.mission.discodeit.global.exception;

import java.util.UUID;

// Entity가 존재하지 않을 때
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException user(UUID id) {
        return new NotFoundException("존재하지 않는 유저입니다 - id: " + id);
    }

    public static NotFoundException userStatus(UUID id) {
        return new NotFoundException("존재하지 않는 유저 상태입니다 - id: " + id);
    }

    public static NotFoundException userStatusByUser(UUID userId) {
        return new NotFoundException("해당 유저의 상태가 존재하지 않습니다 - userId: " + userId);
    }

    public static NotFoundException channel(UUID id) {
        return new NotFoundException("존재하지 않는 채널입니다 - id: " + id);
    }

    public static NotFoundException message(UUID id) {
        return new NotFoundException("존재하지 않는 메시지입니다 - id: " + id);
    }

    public static NotFoundException readStatus(UUID id) {
        return new NotFoundException("존재하지 않는 채널 읽음 상태입니다 - id: " + id);
    }

    public static NotFoundException binaryContent(UUID id) {
        return new NotFoundException("존재하지 않는 파일입니다 - id: " + id);
    }
}
