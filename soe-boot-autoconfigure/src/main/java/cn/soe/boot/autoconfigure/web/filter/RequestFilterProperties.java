package cn.soe.boot.autoconfigure.web.filter;
import lombok.Getter;
import lombok.Setter;
import java.util.Collections;
import java.util.List;

/**
 * @author xiezhenxiang 2023/5/5
 */
@Getter
@Setter
public abstract class RequestFilterProperties {
    /**
     * 处理的url路径
     */
    private List<String> includeUrls = Collections.singletonList("/**");
    /**
     * 排除的url路径
     */
    private List<String> excludeUrls = Collections.emptyList();
    /**
     * 不处理排除的字段
     */
    private List<String> excludeFields = Collections.emptyList();
    /**
     * 是否处理header字段值
     */
    private boolean includeHeader = false;
    /**
     * 是否处理请求体
     */
    private boolean includeRequestBody = true;
}