package com.sprint.mission.discodeit.global.exception;

import com.sprint.mission.discodeit.global.exception.dto.ErrorResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handle(DiscodeitException exception) {
        ExceptionType type = exception.getType();
        String detail = exception.getDetails().toString();

        logMessage(type, exception, detail);

        return ResponseEntity
            .status(type.getStatus())
            .body(ErrorResponse.of(type));
    }

    // Validation 시 추가, exception에서 getMessage를 그대로 사용시 에러의 전체 내용이 나와서 비효율
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
        MethodArgumentNotValidException exception) {
        ExceptionType type = ExceptionType.VALIDATION_FAILED;

        List<String> messages = new ArrayList<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            messages.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
        }
        String detail = String.join(", ", messages);

        logMessage(type, exception, detail);

        return ResponseEntity
            .status(type.getStatus())
            .body(ErrorResponse.of(type, detail));
    }

    // 파라미터 형식이 틀릴 경우
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
        MethodArgumentTypeMismatchException exception) {
        ExceptionType type = ExceptionType.INVALID_PARAMETER;

        String detail = String.format("%s 형식이 올바르지 않습니다. 입력: %s, 필요 형식: %s", exception.getName(),
            exception.getValue(), exception.getRequiredType());

        logMessage(type, exception, detail);

        return ResponseEntity
            .status(type.getStatus())
            .body(ErrorResponse.of(type, detail));

    }

    // Json이 깨졌을 때, 읽을 수 없을 때
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(
        HttpMessageNotReadableException exception) {
        ExceptionType type = ExceptionType.INVALID_PARAMETER;

        String detail = exception.getMessage();

        logMessage(type, exception, detail);
        return ResponseEntity
            .status(type.getStatus())
            .body(ErrorResponse.of(type, detail));
    }

    // 파일 처리 시 오류
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorResponse> handleMultipart(
        MultipartException exception) {
        ExceptionType type = ExceptionType.MULTIPART_FAILED;

        String detail = exception.getMessage();

        logMessage(type, exception, detail);

        return ResponseEntity
            .status(type.getStatus())
            .body(ErrorResponse.of(type, detail));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception exception) {
        ExceptionType type = ExceptionType.INTERNAL_ERROR;
        String detail = type.getMessage();

        logMessage(type, exception, detail);

        return ResponseEntity
            .status(type.getStatus())
            .body(ErrorResponse.of(type, detail));

    }

    // @PreAuthorize 거부는 컨트롤러 안에서 발생해 여기로 온다. 예상된 거부라 스택트레이스는 남기지 않는다
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException exception) {
        ExceptionType type = ExceptionType.ACCESS_DENIED;
        logWithoutStackTrace(type, exception.getMessage());
        return ResponseEntity.status(type.getStatus()).body(ErrorResponse.of(type));
    }

    // 아래 세 예외는 클라이언트 요청 오류다. catch-all로 가면 500이 되므로 원래 상태 코드로 응답한다
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResource(NoResourceFoundException exception) {
        ExceptionType type = ExceptionType.RESOURCE_NOT_FOUND;
        logWithoutStackTrace(type, exception.getResourcePath());
        return ResponseEntity.status(type.getStatus()).body(ErrorResponse.of(type));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(
        HttpRequestMethodNotSupportedException exception) {
        ExceptionType type = ExceptionType.METHOD_NOT_ALLOWED;
        logWithoutStackTrace(type, exception.getMessage());
        return ResponseEntity.status(type.getStatus())
            .headers(exception.getHeaders())   // Allow 헤더
            .body(ErrorResponse.of(type, exception.getMessage()));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMediaTypeNotSupported(
        HttpMediaTypeNotSupportedException exception) {
        ExceptionType type = ExceptionType.UNSUPPORTED_MEDIA_TYPE;
        logWithoutStackTrace(type, exception.getMessage());
        return ResponseEntity.status(type.getStatus())
            .headers(exception.getHeaders())   // Accept 헤더
            .body(ErrorResponse.of(type, exception.getMessage()));
    }

    private static void logMessage(ExceptionType type, Exception exception, String detail) {
        log.atLevel(type.getLevel())
            .setCause(exception)
            .log("{} | details = {}", type.getMessage(), detail);
    }

    private static void logWithoutStackTrace(ExceptionType type, String detail) {
        log.atLevel(type.getLevel())
            .log("{} | details = {}", type.getMessage(), detail);
    }

}