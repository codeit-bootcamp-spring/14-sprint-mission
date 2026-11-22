package com.sprint.mission.discodeit.adapter.in.web.common;

import com.sprint.mission.discodeit.adapter.out.storage.StorageException;
import com.sprint.mission.discodeit.adapter.out.storage.StorageExceptionType;
import com.sprint.mission.discodeit.core.domain.base.DomainException;
import com.sprint.mission.discodeit.core.domain.user.UserException;
import com.sprint.mission.discodeit.core.domain.user.UserExceptionType;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    @Test
    void returnsMappedStatusAndPublicMessageForDomainException() throws Exception {
        var exception = new UserException(UserExceptionType.USER_MISSING_UPSERT_ARGUMENT,
                Map.of("password", "private-value"));

        mvcFor(exception).perform(get("/test-error"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("USER_MISSING_UPSERT_ARGUMENT"))
                .andExpect(jsonPath("$.message").value("필수 사용자 정보를 입력해 주세요."))
                .andExpect(jsonPath("$.details").isEmpty());
    }

    @Test
    void returnsGenericMessageForUnmappedStorageError() throws Exception {
        var exception = new StorageException(StorageExceptionType.LOCAL_STORAGE_FILE_NOT_FOUND, Map.of());

        mvcFor(exception).perform(get("/test-error"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("서버에서 오류가 발생했습니다."));
    }

    private MockMvc mvcFor(DomainException exception) {
        return MockMvcBuilders.standaloneSetup(new FailingController(exception))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @RestController
    static class FailingController {
        private final DomainException exception;

        FailingController(DomainException exception) {
            this.exception = exception;
        }

        @GetMapping("/test-error")
        void fail() {
            throw exception;
        }
    }
}
