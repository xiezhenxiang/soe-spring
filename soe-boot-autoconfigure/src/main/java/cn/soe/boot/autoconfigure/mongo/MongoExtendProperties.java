package cn.soe.boot.autoconfigure.mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xiezhenxiang 2023/5/10
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class MongoExtendProperties {

    /**
     * 连接超时,默认5000ms
     **/
    private int connectTimeOut = 5000;
    /**
     * socket读取超时,默认0
     **/
    private int readTimeOut = 0;
}
