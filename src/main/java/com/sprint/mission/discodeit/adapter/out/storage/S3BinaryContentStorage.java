package com.sprint.mission.discodeit.adapter.out.storage;

import com.sprint.mission.discodeit.adapter.in.controller.binaryContent.dto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.application.binarycontent.out.BinaryContentStorage;
import com.sprint.mission.discodeit.config.aws.AwsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@ConditionalOnProperty(
        name = "discodeit.storage.type",
        havingValue = "s3"
)
@RequiredArgsConstructor
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final S3Client s3Client;
    private final AwsProperties properties;
    private final S3Presigner s3Presigner;

    private static final String BINARY_CONTENT_ROOT_PATH = "images";

    @Override
    public UUID put(UUID id, byte[] bytes) {
        try {
            String key = makeS3ObjectKey(id);
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(properties.bucket())
                    .key(key)
                    .contentType("image/png")
                    .build();

            InputStream inputStream = new ByteArrayInputStream(bytes);

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(inputStream, bytes.length)
            );

            return id;
        } catch (Exception e) {
            throw new StorageException(
                    StorageExceptionType.S3_STORAGE_UPLOAD_FAILED,
                    Map.of("id", id),
                    e
            );
        }
    }

    private String makeS3ObjectKey(UUID id) {
        String binaryContentId = id.toString();
        return "%s/%s".formatted(BINARY_CONTENT_ROOT_PATH, binaryContentId);
    }

    @Override
    public InputStream get(UUID id) {
        try {
            String key = makeS3ObjectKey(id);
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(properties.bucket())
                    .key(key)
                    .build();

            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(request);
            return new ByteArrayInputStream(response.readAllBytes());
        } catch (Exception e) {
            throw new StorageException(
                    StorageExceptionType.S3_STORAGE_READ_FAILED,
                    Map.of("id", id),
                    e
            );
        }
    }

    @Override
    public ResponseEntity<?> download(BinaryContentResponseDto binaryContentResponse) {
        String key = makeS3ObjectKey(binaryContentResponse.id());
        String url = generatePresignedUrl(key);
        String contentType = binaryContentResponse.contentType();
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(url))
                .contentType(MediaType.valueOf(contentType))
                .build();
    }

    private String generatePresignedUrl(String key) {
        String bucket = properties.bucket();
//        String region = properties.getRegion();
//        String encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8).replace("+", "%20");

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(1L))
                .getObjectRequest(getObjectRequest)
                .build();

//        return "https://%s.s3.%s.amazonaws.com/%s".formatted(bucket, region, encodedKey);
        return s3Presigner.presignGetObject(getObjectPresignRequest)
                .url()
                .toString();
    }
}
