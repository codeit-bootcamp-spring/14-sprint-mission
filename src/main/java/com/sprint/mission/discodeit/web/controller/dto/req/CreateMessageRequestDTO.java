package com.sprint.mission.discodeit.web.controller.dto.req;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMessageRequestDTO {
    UUID userId;
    String content;
    List<MultipartFile> imageList;  //안들어올수도있음
}
