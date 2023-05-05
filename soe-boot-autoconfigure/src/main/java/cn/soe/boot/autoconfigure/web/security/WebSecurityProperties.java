package cn.soe.boot.autoconfigure.web.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xiezhenxiang 2023/4/27
 **/
@Getter
@Setter
@ConfigurationProperties("web.security")
public class WebSecurityProperties {

    /**
     * 排除拦截路径, 被排除的路径不经过安全过滤器链
     */
    private String[] excludePaths;
}
