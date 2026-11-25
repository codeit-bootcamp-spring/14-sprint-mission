package com.sprint.mission.discodeit.global.util.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class LocalFileUpload extends AbstractFileUpload{
    private final String fileDir;

    public LocalFileUpload(@Value("${file.upload-dir}") String fileDir) {
        this.fileDir = fileDir;
    }

    @Override
    protected String getFullPath(String fileName) {
        return this.fileDir + fileName;
    }

    @Override
    protected String storeFile(MultipartFile file, String storeFileName) throws IOException {
        Path uploadDirectory = Path.of(fileDir).toAbsolutePath();
        Files.createDirectories(uploadDirectory);

        Path destinationPath = uploadDirectory.resolve(storeFileName);
        File destinationFile = destinationPath.toFile();
        file.transferTo(destinationFile);
        return destinationFile.getAbsolutePath();
    }
}
