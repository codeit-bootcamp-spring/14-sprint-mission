package com.sprint.mission.discodeit.common;

import com.sprint.mission.discodeit.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Void> handle(CustomException exception) {
        // 내가 알고있거나 / 명시적으로 처리하고싶어하는 예외 상황에 대해 이렇게 구체적인 예외 클래스를 명시해서 처리
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(null);
    }
}
