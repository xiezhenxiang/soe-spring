package cn.soe.boot.autoconfigure.web.filter;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

/**
 * @author xiezhenxiang 2023/5/4
 **/
public class RequestParamServletWrapper extends HttpServletRequestWrapper {

    private final RequestFilterProperties filterProperties;
    private final Function<String, String> paramConvertor;

    public RequestParamServletWrapper(HttpServletRequest request, RequestFilterProperties filterProperties, Function<String, String> paramConvertor) {
        super(request);
        this.filterProperties = filterProperties;
        this.paramConvertor = paramConvertor;
    }

    @Override
    public String getParameter(String key) {
        String value = super.getParameter(key);
        return isExcludeField(key) ? value : convertValue(value);
    }

    @Override
    public String[] getParameterValues(String key) {
        String[] arr = super.getParameterValues(key);
        return isExcludeField(key) ? arr : Arrays.stream(arr).map(this::convertValue).toArray(String[]::new);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = super.getParameterMap();
        if (CollectionUtils.isEmpty(map)) {
            return map;
        }
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            if (!isExcludeField(entry.getKey())) {
                entry.setValue(Arrays.stream(entry.getValue()).map(this::convertValue).toArray(String[]::new));
            }
        }
        return map;
    }

    @Override
    public String getHeader(String key) {
        String value = super.getHeader(key);
        return filterProperties.isIncludeHeader() ? convertValue(value) : value;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if(filterProperties.isIncludeRequestBody()){
            String bodyStr = StreamUtils.copyToString(getRequest().getInputStream(), StandardCharsets.UTF_8);
            return ofServletInputStream(convertValue(bodyStr));
        }
        return super.getInputStream();
    }

    private ServletInputStream ofServletInputStream(String str) {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read() {
                return arrayInputStream.read();
            }
        };
    }

    private boolean isExcludeField(String field){
        if(CollectionUtils.isEmpty(filterProperties.getExcludeFields())){
            return false;
        }
        return filterProperties.getExcludeFields().contains(field);
    }

    private String convertValue(String value){
        return value == null ? value: paramConvertor.apply(value);
    }
}
