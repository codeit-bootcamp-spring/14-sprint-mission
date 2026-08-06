package com.sprint.mission.discodeit.exception;

public class NameExistsException extends RuntimeException{
    public NameExistsException(String message) {
        super(message);
    }

    public static NameExistsException ofChannel(String channelName) {
        return new NameExistsException("이미 존재하는 채널입니다: " + channelName);
    }

    public static NameExistsException ofUser(String updateUserName) {
        return new NameExistsException("이미 존재하는 유저입니다: " + updateUserName);
    }
}
