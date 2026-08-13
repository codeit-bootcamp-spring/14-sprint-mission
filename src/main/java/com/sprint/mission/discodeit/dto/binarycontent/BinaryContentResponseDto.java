package com.sprint.mission.discodeit.dto.binarycontent;

import com.sprint.mission.discodeit.domain.BinaryContent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BinaryContentResponseDto {
    UUID id;
    String fileName;
    byte[] bytes;

    public static BinaryContentResponseDto from(BinaryContent binaryContent) {
        return new BinaryContentResponseDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getBytes().clone()
        );
    }
}
