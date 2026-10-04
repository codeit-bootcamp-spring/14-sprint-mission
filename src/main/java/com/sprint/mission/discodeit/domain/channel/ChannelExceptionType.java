package com.sprint.mission.discodeit.domain.channel;

import com.sprint.mission.discodeit.common.exception.IExceptionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ChannelExceptionType implements IExceptionType {
    CHANNEL_NOT_FOUND_IN_DATABASE(HttpStatus.NOT_FOUND, Level.INFO, "Channel 미존재", "Database에서 해당 Channel 찾을 수 없음"),
    NO_ACCESS_TO_CHANNEL(HttpStatus.FORBIDDEN, Level.WARN, "Channel 권한 없음", "등록되지 않은 User는 PRIVATE채널에 접근할 수 없습니다"),
    PRIVATE_CHANNEL_CANNOT_BE_MODIFIED(HttpStatus.BAD_REQUEST, Level.INFO, "채널 정보 수정 불가", "PRIVATE 채널의 정보는 수정할 수 없습니다"),
    ;

    HttpStatus httpStatus;
    Level logLevel;
    String title;
    String description;
}
