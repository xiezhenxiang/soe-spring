package cn.soe.boot.autoconfigure.web.filter.xss;

import cn.soe.boot.autoconfigure.web.filter.RequestFilterProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xiezhenxiang 2023/5/4
 **/
@Getter
@Setter
@ConfigurationProperties("soe.xss")
public class XssFilterProperties extends RequestFilterProperties {

    /**
     * 是否启用xss,默认为false
     */
    private boolean enable = false;
    /**
     * 存在xss直接抛异常
     */
    private boolean fastFail;
}
