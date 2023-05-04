package cn.soe.boot.autoconfigure.web.filter.cors;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

/**
 * @author xiezhenxiang 2021/9/13
 */
@Getter
@Setter
@ConfigurationProperties("soe.cors")
public class CorsProperties {

    /**
     * 是否启用跨域,默认为true
     */
    private boolean enable = true;
    private String name = "defaultCorsFilter";
    private int order = Ordered.HIGHEST_PRECEDENCE;
    private String pattern = "/**";
    private Boolean allowCredentials;
    private String allowedOrigin = CorsConfiguration.ALL;
    private String allowedHeader = CorsConfiguration.ALL;
    private String allowedMethod = CorsConfiguration.ALL;
    private List<String> exposedHeaders;
    private Long maxAge = 3600L;
}
