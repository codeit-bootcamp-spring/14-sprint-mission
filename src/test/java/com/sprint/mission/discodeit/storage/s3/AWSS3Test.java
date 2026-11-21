package com.sprint.mission.discodeit.storage.s3;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Tag("s3")
public class AWSS3Test {

    @DisplayName("s3에 하위 텍스트 업로드")
    @Test
    void upload() throws Exception {
        Properties env = new Properties();

        try (InputStream input = Files.newInputStream(Path.of(".env"))) {
            env.load(input);
        }

        S3Client s3 = S3Client.builder()
            .region(Region.of(env.getProperty("AWS_S3_REGION")))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                        env.getProperty("AWS_S3_ACCESS_KEY"),
                        env.getProperty("AWS_S3_SECRET_KEY")
                    )
                )
            )
            .build();

        s3.putObject(
            PutObjectRequest.builder()
                .bucket(env.getProperty("AWS_S3_BUCKET"))
                .key("test/hello.txt")
                .build(),
            RequestBody.fromString("hawe")
        );
    }
}
