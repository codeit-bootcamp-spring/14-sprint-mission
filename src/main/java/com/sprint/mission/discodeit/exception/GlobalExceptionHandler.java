package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 컨트롤러 안에서 try-catch 하지 않는다. ArgumentResolver 단계(파라미터를 객체로 바꾸는 단계)에서
 * 터지는 예외는 컨트롤러 진입 전이라 try-catch로 못 잡음. — 그래서 컨트롤러 바깥, 여기서 잡는다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** 이 프로젝트가 의도적으로 던지는 예외. 상태 코드·메시지는 ExceptionType이 이미 들고 있다. */
    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
        log.warn("{}: {}", e.getType(), e.getMessage());
        return toResponse(e.getType().getHttpStatus(), e.getType().name(), e.getMessage());
    }

    /** id를 전부 @RequestParam으로 받으므로, 필수 파라미터를 빠뜨리면 여기로 온다. */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(MissingServletRequestParameterException e) {
        log.warn(e.getMessage());
        return toResponse(HttpStatus.BAD_REQUEST, "MISSING_PARAMETER", e.getMessage());
    }

    /** UUID 형식이 아닌 값을 파라미터로 보냈을 때. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.warn(e.getMessage());
        String message = String.format("%s 파라미터의 값 '%s'이(가) 올바르지 않습니다.", e.getName(), e.getValue());
        return toResponse(HttpStatus.BAD_REQUEST, "TYPE_MISMATCH", message);
    }

    /** JSON 본문이 깨졌거나 필수 필드가 아예 빠졌을 때. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException e) {
        log.warn(e.getMessage());
        return toResponse(HttpStatus.BAD_REQUEST, "MALFORMED_REQUEST_BODY", "요청 본문을 읽을 수 없습니다.");
    }

    /** switch의 default와 같다. 우리가 모르는 예외까지 여기서 받아 500으로 응답한다. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception e) {
        // 예외 객체를 두 번째 인자로 넘겨야 스택트레이스가 남는다. 메시지만 찍으면 원인 위치가 사라진다.
        log.error(e.getMessage(), e);
        return toResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.");
    }

    private ResponseEntity<ErrorResponse> toResponse(HttpStatus status, String code, String message) {
        ErrorResponse body = new ErrorResponse(Instant.now(), status.value(), code, message);
        return ResponseEntity.status(status).body(body);
    }
}
