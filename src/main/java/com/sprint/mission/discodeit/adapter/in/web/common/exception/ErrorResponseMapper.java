package com.sprint.mission.discodeit.adapter.in.web.common.exception;

import com.sprint.mission.discodeit.core.domain.base.DomainException;
import com.sprint.mission.discodeit.core.domain.base.ErrorCode;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;

import static com.sprint.mission.discodeit.adapter.in.web.controller.multipart.MultiPartFileExceptionType.FILE_IO_FAILED;
import static com.sprint.mission.discodeit.adapter.in.web.controller.multipart.MultiPartFileExceptionType.FILE_NOT_FOUND;
import static com.sprint.mission.discodeit.core.domain.binaryContent.BinaryContentExceptionType.BINARY_CONTENT_NOT_FOUND;
import static com.sprint.mission.discodeit.core.domain.channel.ChannelExceptionType.CHANNEL_NOT_FOUND;
import static com.sprint.mission.discodeit.core.domain.channel.ChannelExceptionType.NO_ACCESS_TO_CHANNEL;
import static com.sprint.mission.discodeit.core.domain.channel.ChannelExceptionType.PRIVATE_CHANNEL_CANNOT_BE_MODIFIED;
import static com.sprint.mission.discodeit.core.domain.channel.ChannelExceptionType.READ_STATUS_NOT_ALLOWED;
import static com.sprint.mission.discodeit.core.domain.message.MessageExceptionType.MESSAGE_NOT_FOUND;
import static com.sprint.mission.discodeit.core.domain.readstatus.ReadStatusExceptionType.READ_STATUS_ALREADY_EXISTS;
import static com.sprint.mission.discodeit.core.domain.readstatus.ReadStatusExceptionType.READ_STATUS_NOT_FOUND;
import static com.sprint.mission.discodeit.core.domain.user.UserExceptionType.LOGIN_FAILED;
import static com.sprint.mission.discodeit.core.domain.user.UserExceptionType.USER_MISSING_UPSERT_ARGUMENT;
import static com.sprint.mission.discodeit.core.domain.user.UserExceptionType.USER_NOT_FOUND;
import static com.sprint.mission.discodeit.core.domain.user.UserExceptionType.USER_UNIQUE_FIELD_CONFLICT;
import static java.util.Map.entry;

public final class ErrorResponseMapper {

    private static final WebErrorSpec INTERNAL_ERROR =
            new WebErrorSpec(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 오류가 발생했습니다.");

    private static final Map<ErrorCode, WebErrorSpec> MAPPINGS = Map.ofEntries(
            entry(LOGIN_FAILED, new WebErrorSpec(HttpStatus.UNAUTHORIZED, "사용자 이름 또는 비밀번호가 올바르지 않습니다.")),
            entry(USER_NOT_FOUND, new WebErrorSpec(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.")),
            entry(USER_UNIQUE_FIELD_CONFLICT, new WebErrorSpec(HttpStatus.BAD_REQUEST, "이미 사용 중인 사용자 이름 또는 이메일입니다.")),
            entry(USER_MISSING_UPSERT_ARGUMENT, new WebErrorSpec(HttpStatus.BAD_REQUEST, "필수 사용자 정보를 입력해 주세요.")),

            entry(CHANNEL_NOT_FOUND, new WebErrorSpec(HttpStatus.NOT_FOUND, "채널을 찾을 수 없습니다.")),
            entry(NO_ACCESS_TO_CHANNEL, new WebErrorSpec(HttpStatus.FORBIDDEN, "채널에 접근할 권한이 없습니다.")),
            entry(PRIVATE_CHANNEL_CANNOT_BE_MODIFIED, new WebErrorSpec(HttpStatus.BAD_REQUEST, "비공개 채널의 정보는 수정할 수 없습니다.")),

            entry(READ_STATUS_NOT_ALLOWED, new WebErrorSpec(HttpStatus.BAD_REQUEST, "공개 채널에는 읽음 상태를 생성할 수 없습니다.")),
            entry(READ_STATUS_NOT_FOUND, new WebErrorSpec(HttpStatus.NOT_FOUND, "읽음 상태를 찾을 수 없습니다.")),
            entry(READ_STATUS_ALREADY_EXISTS, new WebErrorSpec(HttpStatus.BAD_REQUEST, "이미 읽음 상태가 있습니다.")),

            entry(MESSAGE_NOT_FOUND, new WebErrorSpec(HttpStatus.NOT_FOUND, "메시지를 찾을 수 없습니다.")),

            entry(BINARY_CONTENT_NOT_FOUND, new WebErrorSpec(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다.")),

            entry(FILE_NOT_FOUND, new WebErrorSpec(HttpStatus.UNPROCESSABLE_CONTENT, "업로드할 파일을 선택해 주세요.")),
            entry(FILE_IO_FAILED, new WebErrorSpec(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."))
    );

    private ErrorResponseMapper() {
    }

    public static ErrorResponse toResponse(DomainException exception) {
        return toResponse(exception.getType(), exception.getTimestamp(), exception.getExceptionClassName());
    }

    public static ErrorResponse toResponse(ErrorCode errorCode, Instant timestamp, String exceptionType) {
        WebErrorSpec spec = MAPPINGS.getOrDefault(errorCode, INTERNAL_ERROR);
        return ErrorResponse.of(timestamp, errorCode.toString(), spec.message(), Map.of(), exceptionType, spec.status().value());
    }

    private record WebErrorSpec(HttpStatus status, String message) {
    }
}
