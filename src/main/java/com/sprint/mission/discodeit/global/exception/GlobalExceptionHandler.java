package com.sprint.mission.discodeit.global.exception;

import jakarta.validation.ConstraintViolationException;
import java.net.BindException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
        비즈니스 로직 처리 중 발생한 예외 처리
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleException(CustomException exception){
        log.warn("서비스 로직 처리 중 예외 발생 ------- message : {}", exception.getMessage());

        CustomErrorCode errorCode = exception.getCustomErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
            .body(errorCode.getMessage());
    }

    /*
        컨버전  서비스의 타입변환 검증 실패 예외 처리
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Void> handleException(MethodArgumentTypeMismatchException exception){
        log.warn("타입 변환 중 예외 발생 ------- message : {}", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .build();
    }

    /*
        데이터 바인더 내부에서 처리된 바인딩 리절트 익셉션(바인딩 리절트를 명시하지 않아 발생한 예외)
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Void> handleException(BindException exception){
        log.warn("메서드 내부에서 바인딩 리절트로 처리할 것 ----- {}", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .build();
    }

    /*
        웹 데이터 바인더 내부에서 자바 빈 검증 실패 시 (스프링 밸리데이터 통한 필드 검증)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Void> handleException(MethodArgumentNotValidException exception){
        //todo : 바인딩 리절트 처리
        log.warn("Validator 검증 중 예외 발생 ---------- message : {}", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
            build();
    }

    /*
        컨트롤러 단일 변수 매핑 간 예외 발생 처리
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Void> handleException(HandlerMethodValidationException exception){
        log.warn("컨트롤러 단일 변수 검증 중 예외 발생 ----------- message : {}", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .build();
    }

    /*
        스프링 validated로 aop 등록한 컨트롤러 외의 계층에서 발생한 검증 예외 처리
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Void> handleException(ConstraintViolationException exception){
        log.warn("컨트롤러 계층 외의 검증 시 예외 발생 ------------ ");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .build();
    }


    @ExceptionHandler
    public ResponseEntity<Void> handleException(Exception exception){
        log.error("서버 내부에서 정의하지 않은 예외 발생 -------- ", exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .build();
    }
}
