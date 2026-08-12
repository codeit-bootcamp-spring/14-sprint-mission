package com.sprint.mission.discodeit.dto.binarycontentdto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter //폼으로 받을때 이거 없으면 null로 받아옴
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BinaryContentCreateRequestDto {
    String contentAddress;
    UUID userId;
    UUID messageId;

    public BinaryContent toEntity() {
        return new BinaryContent(
                this.contentAddress,
                this.userId,
                this.messageId);
    }
}
