package cn.soe.boot.autoconfigure.minio;

import cn.soe.boot.core.util.SoeUtils;
import cn.soe.util.database.minio.MinioConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xiezhenxiang 2023/4/27
 **/
@Getter
@Setter
@ConfigurationProperties("spring.data.minio")
public class MinioProperties extends MinioConfig {

    private static String defaultBucketName;

    /**
     * 默认bucket, 默认为default
     **/
    private String defaultBucket = "default";

    public static String defaultBucket() {
        if (defaultBucketName == null) {
            defaultBucketName = SoeUtils.getBean(MinioProperties.class).getDefaultBucket();
        }
        return defaultBucketName;
    }
}
