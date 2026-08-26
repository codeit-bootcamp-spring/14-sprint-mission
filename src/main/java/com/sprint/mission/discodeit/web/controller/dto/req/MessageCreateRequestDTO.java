package com.sprint.mission.discodeit.web.controller.dto.req;

import java.rmi.server.UID;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageCreateRequestDTO {
    UUID authorId;
    UUID channelId;
    String content;
}
