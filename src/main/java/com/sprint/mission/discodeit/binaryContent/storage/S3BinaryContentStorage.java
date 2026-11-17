package com.sprint.mission.discodeit.binaryContent.storage;

import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.common.exception.BinaryContentNotFoundException;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Slf4j
@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final S3Client s3Client;
    private final S3Presigner presigner;
    private final String bucket;
    private final Duration presignedUrlExpiration;

    public S3BinaryContentStorage(
        @Value("${discodeit.storage.s3.access-key}") String accessKey,
        @Value("${discodeit.storage.s3.secret-key}") String secretKey,
        @Value("${discodeit.storage.s3.region}") String region,
        @Value("${discodeit.storage.s3.bucket}") String bucket,
        @Value("${discodeit.storage.s3.presigned-url-expiration}") long presignedUrlExpirationSeconds) {

        StaticCredentialsProvider credentials = StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey));
        Region awsRegion = Region.of(region);

        this.s3Client = S3Client.builder()
            .region(awsRegion)
            .credentialsProvider(credentials)
            .build();
        this.presigner = S3Presigner.builder()
            .region(awsRegion)
            .credentialsProvider(credentials)
            .build();
        this.bucket = bucket;
        this.presignedUrlExpiration = Duration.ofSeconds(presignedUrlExpirationSeconds);
    }

    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) {
        String key = binaryContentId.toString();
        try {
            s3Client.putObject(
                PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build(),
                RequestBody.fromBytes(bytes));
        } catch (SdkException e) {
            log.error("S3 파일 업로드 실패 - id = {}", binaryContentId, e);
            throw e;
        }
        log.info("S3 파일 업로드 완료 - id = {}, size = {}", binaryContentId, bytes.length);
        return binaryContentId;
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        String key = binaryContentId.toString();
        try {
            return s3Client.getObject(
                GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
        } catch (NoSuchKeyException e) {
            log.warn("S3 파일 없음 - id = {}", binaryContentId);
            throw new BinaryContentNotFoundException(binaryContentId);
        } catch (SdkException e) {
            log.error("S3 파일 읽기 실패 - id = {}", binaryContentId, e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> download(BinaryContentDto dto) {
        String key = dto.id().toString();

        // 브라우저가 원래 파일 이름(한글 포함)으로 저장하도록 지정
        String contentDisposition = ContentDisposition.attachment()
            .filename(dto.fileName(), StandardCharsets.UTF_8)
            .build()
            .toString();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(presignedUrlExpiration)
            .getObjectRequest(r -> r
                .bucket(bucket)
                .key(key)
                .responseContentDisposition(contentDisposition)
                .responseContentType(dto.contentType()))
            .build();

        String presignedUrl = presigner.presignGetObject(presignRequest).url().toString();
        log.info("S3 파일 다운로드 - id = {}, fileName = {}", dto.id(), dto.fileName());

        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create(presignedUrl))
            .build();
    }
}