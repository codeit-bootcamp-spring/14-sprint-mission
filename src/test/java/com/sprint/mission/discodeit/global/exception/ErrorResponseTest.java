package com.sprint.mission.discodeit.global.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;
import org.junit.jupiter.api.Test;

class ErrorResponseTest {

    @Test
    void createsValidationResponseWithProvidedDetails() {
        Map<String, Object> details = Map.of("username", "필수 항목입니다");

        ErrorResponse response = ErrorResponse.of(
            CustomErrorCode.VALIDATION_FAILED, new IllegalArgumentException(), details);

        assertEquals("VALIDATION_FAILED", response.code());
        assertEquals(CustomErrorCode.VALIDATION_FAILED.getMessage(), response.message());
        assertEquals(400, response.status());
        assertEquals("IllegalArgumentException", response.exceptionType());
        assertEquals(details, response.details());
        assertNotNull(response.timestamp());
    }
}
