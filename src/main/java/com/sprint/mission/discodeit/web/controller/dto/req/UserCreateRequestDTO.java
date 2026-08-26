package com.sprint.mission.discodeit.web.controller.dto.req;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequestDTO {
    String email;
    String password;
    //String checkPassword;2af6e1d3-2df8-4cb7-860f-7055558e5022 // 04654c65-c179-4db8-bf99-8458daa42f72
    String username;
}
