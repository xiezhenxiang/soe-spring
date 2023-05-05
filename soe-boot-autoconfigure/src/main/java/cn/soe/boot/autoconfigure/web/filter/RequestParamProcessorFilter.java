package cn.soe.boot.autoconfigure.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.function.Function;

/**
 * @author xiezhenxiang 2023/5/4
 **/
public class RequestParamProcessorFilter implements Filter {

    private final RequestMatcher requestMatcher;
    private final RequestFilterProperties filterProperties;
    private final Function<String, String> paramConvertor;

    public RequestParamProcessorFilter(RequestFilterProperties filterProperties, Function<String, String> paramConvertor) {
        this.filterProperties = filterProperties;
        this.paramConvertor = paramConvertor;
        this.requestMatcher = new RequestMatcher(filterProperties.getIncludeUrls(), filterProperties.getExcludeUrls());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (requestMatcher.matches(req)) {
            request = new RequestParamServletWrapper(req, filterProperties, paramConvertor);
        }
        filterChain.doFilter(request, response);
    }
}
