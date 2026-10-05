package com.sprint.mission.discodeit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.saveAndFlush(
            new User(
                "kimoo",
                "kim@gmail.com",
                "1234",
                null
            )
        );
    }

    @Test
    @DisplayName("유저생성 API를 호출하고 유저를 생성한다")
    void success_newUser() throws Exception {
        // given
        UserCreateRequest request = new UserCreateRequest(
            "leemoo",
            "lee@gmail.com",
            "1234"
        );

        // when, then
        mockMvc.perform(
                multipart("/api/users")
                    .file(jsonPart("userCreateRequest", request))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value("leemoo"))
            .andExpect(jsonPath("$.email").value("lee@gmail.com"));

        assertTrue(userRepository.existsByEmail("lee@gmail.com"));
    }



    @Test
    @DisplayName("유저리스트조회 API를 호출하고 유저를 조회한다")
    void success_findAllUser() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].username").value("kimoo"));
    }









    private MockMultipartFile jsonPart(String partName, Object request)
        throws Exception {

        return new MockMultipartFile(
            partName,
            "",
            MediaType.APPLICATION_JSON_VALUE,
            objectMapper.writeValueAsBytes(request)
        );
    }
}
