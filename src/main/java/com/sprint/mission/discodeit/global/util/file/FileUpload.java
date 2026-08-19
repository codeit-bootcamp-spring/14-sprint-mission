package com.sprint.mission.discodeit.global.util.file;

import com.sprint.mission.discodeit.domain.entity.BinaryContent;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface FileUpload {
    BinaryContent uploadFile(MultipartFile file);
    List<BinaryContent> uploadFiles(List<MultipartFile> files);
}
