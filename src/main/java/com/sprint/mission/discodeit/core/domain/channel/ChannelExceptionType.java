package com.sprint.mission.discodeit.core.domain.channel;

import com.sprint.mission.discodeit.core.domain.base.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.event.Level;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ChannelExceptionType implements ErrorCode {
    CHANNEL_NOT_FOUND(Level.INFO, "Channel 미존재", "Database에서 해당 Channel 찾을 수 없음"),
    NO_ACCESS_TO_CHANNEL(Level.WARN, "Channel 권한 없음", "등록되지 않은 User는 PRIVATE채널에 접근할 수 없습니다"),
    PRIVATE_CHANNEL_CANNOT_BE_MODIFIED(Level.INFO, "채널 정보 수정 불가", "PRIVATE 채널의 정보는 수정할 수 없습니다"),
    READ_STATUS_NOT_ALLOWED(Level.INFO, "ReadStatus 생성 불가", "PUBLIC 채널은 ReadStauts를 생성할 수 없습니다.");

    Level logLevel;
    String title;
    String description;
}
