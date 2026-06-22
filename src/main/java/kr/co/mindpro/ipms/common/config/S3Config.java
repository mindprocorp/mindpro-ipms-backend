package kr.co.mindpro.ipms.common.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.endpoint}")
    private String endpoint;

    @Bean
    public S3Client s3Client() {
        // 1. 네이버 클라우드 인증 정보 (Access Key, Secret Key)
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        // 2. S3Client 생성
        // NCP는 S3와 호환되지만 실제 서버 주소가 다르므로 endpointOverride가 필수입니다.
        return S3Client.builder()
                .region(Region.of(region)) // 보통 'kr-standard'
                .endpointOverride(URI.create(endpoint)) // https://kr.object.ncloudstorage.com
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}