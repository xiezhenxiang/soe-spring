package cn.soe.boot.autoconfigure.web.filter;

import lombok.NoArgsConstructor;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * @author xiezhenxiang 2023/5/4
 **/
@NoArgsConstructor
public class RequestMatcher {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private List<String> includeUrls = Collections.singletonList("/**");
    private List<String> excludeUrls = Collections.emptyList();

    public RequestMatcher(List<String> includeUrls, List<String> excludeUrls) {
        Assert.notNull(includeUrls,"includeUrls must be not null");
        Assert.notNull(excludeUrls, "includeUrls must be not null");
        this.includeUrls = includeUrls;
        this.excludeUrls = excludeUrls;
    }

    public boolean matches(HttpServletRequest request){
        String url = request.getServletPath();
        if (doMatch(url,excludeUrls)) {
            return false;
        }
        return doMatch(url, includeUrls);
    }

    private boolean doMatch(String url, List<String> urls) {
        for (String pattern : urls) {
            if (antPathMatcher.match(pattern,url)) {
                return true;
            }
        }
        return false;
    }
}
