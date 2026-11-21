package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.binarycontent.web.dto.res.BinaryContentResponseDTO;
import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
@Component
@ConditionalOnProperty(
    prefix = "discodeit.storage",
    name = "type",
    havingValue = "s3"
)
public class S3BinaryContentStorage implements BinaryContentStorage{
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucket;
    private final long presignedUrlExpiration;

    public S3BinaryContentStorage(
        S3Client s3Client,
        S3Presigner s3Presigner,
        @Value("${discodeit.storage.s3.bucket}") String bucket,
        @Value("${discodeit.storage.s3.presigned-url-expiration}") long presignedUrlExpiration
    ) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.bucket = bucket;
        this.presignedUrlExpiration = presignedUrlExpiration;
    }

    @Override
    public UUID put(UUID id, byte[] bytes) {
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(id.toString())
                .build();

            s3Client.putObject(request, RequestBody.fromBytes(bytes));

            return id;
        } catch (SdkException e) {
            log.error("S3 파일 저장 실패 : {}", id, e);
            throw new CustomException(CustomErrorCode.FILE_STORE_FAILED,
                Map.of(
                    "binaryContentId", id,
                    "storageType", "s3"
                )
            );
        }
    }

    @Override
    public InputStream get(UUID id) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(id.toString())
                .build();

            return s3Client.getObject(request);
        } catch (SdkException e) {
            log.error("S3 파일 조회 실패: {}", id, e);

            throw new CustomException(
                CustomErrorCode.FILE_READ_FAILED,
                Map.of(
                    "binaryContentId", id,
                    "storageType", "s3"
                )
            );
        }
    }

    @Override
    public ResponseEntity<Void> download(UUID key, String contentType) {
        String downloadUrl = generatePresignedUrl(key.toString(), contentType);

        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create(downloadUrl))
            .build();
    }

    private String generatePresignedUrl(String key, String contentType) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .responseContentType(contentType)
                .build();

            PresignedGetObjectRequest presignedRequest =
                s3Presigner.presignGetObject(
                    GetObjectPresignRequest.builder()
                        .signatureDuration(
                            Duration.ofSeconds(presignedUrlExpiration)
                        )
                        .getObjectRequest(getObjectRequest)
                        .build()
                );

            return presignedRequest.url().toString();

        } catch (SdkException e) {
            log.error("S3 프리사인 URL 받아오기 실패: {}", key, e);

            throw new CustomException(
                CustomErrorCode.FILE_READ_FAILED,
                Map.of("storageKey", key, "storageType", "s3")
            );
        }
    }
}
