package com.sprint.mission.discodeit.binarycontent;

import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentCreateRequest;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Objects;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class BinaryContentRequestMapper {

  public BinaryContentCreateRequest toCreateRequest(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      return null;
    }
    String originalFilename = file.getOriginalFilename();

    String fileName =
        originalFilename == null || originalFilename.isBlank() ? "unnamed" : originalFilename;

    String contentType = file.getContentType() == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE
        : file.getContentType();

    try {
      return new BinaryContentCreateRequest(
          fileName,
          contentType,
          file.getBytes()
      );
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }


  public List<BinaryContentCreateRequest> toCreateRequests(List<MultipartFile> files) {
    if (files == null || files.isEmpty()) {
      return List.of();
    }
    return files.stream()
        .filter(Objects::nonNull)
        .filter(file -> !file.isEmpty())
        .map(this::toCreateRequest).toList();
  }
}
