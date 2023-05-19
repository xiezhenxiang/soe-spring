package cn.soe.util.database.minio;

import lombok.*;

import java.time.Duration;

/**
 * @author xiezhenxiang 2023/4/27
 **/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MinioConfig {

    /**
     * minio endpoint,默认为http://127.0.0.1:9000
     */
    private String url = "http://127.0.0.1:9000";

    /**
     * minio accessKey
     */
    private String username = "";

    /**
     * minio secretKey
     */
    private String password = "";

    /**
     * 连接超时时间,默认为5s
     */
    private Duration connectTimeout = Duration.ofSeconds(5);
    /**
     * 读取超时时间,默认为10m
     */
    private Duration readTimeout = Duration.ofMinutes(10);
    /**
     * 写入超时时间,默认为10m
     */
    private Duration writeTimeout = Duration.ofMinutes(10);

    /**
     * 预生成的url过期时间 默认24小时
     */
    private int urlExpire = (int) Duration.ofHours(24).getSeconds();

}
