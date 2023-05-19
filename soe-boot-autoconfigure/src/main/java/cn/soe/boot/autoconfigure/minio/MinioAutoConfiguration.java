package cn.soe.boot.autoconfigure.minio;

import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author xiezhenxiang 2023/5/9
 */
@AutoConfiguration
@ConditionalOnClass(MinioClient.class)
@EnableConfigurationProperties(MinioProperties.class)
public class MinioAutoConfiguration {

    @Bean
    public MinioSdk minioSdk(MinioProperties minioProperties) {
        MinioSdk minioSdk = new MinioSdk(minioProperties);
        if (!minioSdk.existBucket(minioProperties.getDefaultBucket())) {
            minioSdk.createBucket(minioProperties.getDefaultBucket());
        }
        return minioSdk;
    }
}
