package com.sprint.mission.multipart;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

@Component
public class LocalMultipartFileConverter extends AbstractMultipartFileConverter {

    @Override
    protected String generate(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String sanitizedFileName = sanitize(originalFileName);

        return UUID.randomUUID() + "_" + sanitizedFileName;
    }

    private String sanitize(String originalFileName) {
        if (Objects.isNull(originalFileName) || originalFileName.isBlank()) {
            originalFileName = "unknown";
        }

        int lastWinSep = originalFileName.lastIndexOf("\\");
        int lastUnixSep = originalFileName.lastIndexOf("/");
        int lastIndex = Math.max(lastWinSep, lastUnixSep);

        // 파일명이 빈 값이면 unknown 파일명으로 변환하여 반환
        String sanitized = (lastIndex != -1)
                ? originalFileName.substring(lastIndex + 1)
                : originalFileName;
        return sanitized.isBlank() ? "unknown" : sanitized;
    }
}

