package com.sprint.mission.discodeit.advice;

import com.sprint.mission.discodeit.exception.CustomRuntimeException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<String> handleCustomRuntimeException(CustomRuntimeException exception) {
        ExceptionType exceptionType = exception.getType();
        //백엔드 로그찍기
        log.makeLoggingEventBuilder(exceptionType.getLevel())
                .setCause(exception)
                .log(exception.getMessage());
        //프론트에 무슨 에러난지 보내주기
        return ResponseEntity
                .status(exceptionType.getStatus())
                /* 프론트에도 에러메세지 구체적으로 솨주기 위해
                exceptionType을 exception으로 변경*/
                .body(exception.getMessage());
    }

//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOthersException(Exception exception) {
        //백엔드 로그찍기
        log.error("정의되지 않은 예외 발생", exception);
        //프론트에 에러 화면쏴주기
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서부 내부 오류 입니다");
    }
}
