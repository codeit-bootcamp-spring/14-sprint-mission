package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.common.exception.binarycontent.FileError;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PreDestroy;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ContentDisposition;
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
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final S3Client s3Client;
    private final S3Presigner presigner;
    private final String bucket;
    private final Duration expiration;

    public S3BinaryContentStorage(
        @Value("${discodeit.storage.s3.access-key}") String accessKey,
        @Value("${discodeit.storage.s3.secret-key}") String secretKey,
        @Value("${discodeit.storage.s3.region}") String region,
        @Value("${discodeit.storage.s3.bucket}") String bucket,
        @Value("${discodeit.storage.s3.presigned-url-expiration}") long expirationSeconds) {
        var credentials = StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey));
        this.s3Client = S3Client.builder()
            .region(Region.of(region)).credentialsProvider(credentials).build();
        this.presigner = S3Presigner.builder()
            .region(Region.of(region)).credentialsProvider(credentials).build();
        this.bucket = bucket;
        this.expiration = Duration.ofSeconds(expirationSeconds);
    }

    @Override
    public UUID put(UUID id, byte[] bytes) {
        try {
            s3Client.putObject(
                PutObjectRequest.builder().bucket(bucket).key(id.toString()).build(),
                RequestBody.fromBytes(bytes));
            return id;
        } catch (SdkException e) {
            throw new FileError();
        }
    }

    @Override
    public InputStream get(UUID id) {
        try {
            return s3Client.getObject(
                GetObjectRequest.builder().bucket(bucket).key(id.toString()).build());
        } catch (SdkException e) {
            throw new FileError();
        }
    }

    @Override
    public ResponseEntity<?> download(BinaryContentDto dto) {
        String disposition = ContentDisposition.attachment()
            .filename(dto.fileName(), StandardCharsets.UTF_8).build().toString();
        var request = GetObjectRequest.builder()
            .bucket(bucket).key(dto.id().toString())
            .responseContentType(dto.contentType())
            .responseContentDisposition(disposition).build();
        var presigned = presigner.presignGetObject(GetObjectPresignRequest.builder()
            .signatureDuration(expiration).getObjectRequest(request).build());
        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create(presigned.url().toString())).build();
    }

    @PreDestroy
    public void close() {
        s3Client.close();
        presigner.close();
    }
}
