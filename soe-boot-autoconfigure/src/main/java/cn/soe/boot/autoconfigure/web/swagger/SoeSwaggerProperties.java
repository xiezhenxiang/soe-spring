package cn.soe.boot.autoconfigure.web.swagger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xiezhenxiang 2023/4/27
 **/
@Getter
@Setter
@ConfigurationProperties("swagger")
public class SoeSwaggerProperties {

    /**
     * 是否启用swagger,默认为true
     */
    private boolean enable = true;
}
