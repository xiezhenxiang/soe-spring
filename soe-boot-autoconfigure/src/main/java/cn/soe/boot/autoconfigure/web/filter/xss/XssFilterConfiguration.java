package cn.soe.boot.autoconfigure.web.filter.xss;

import cn.soe.boot.autoconfigure.web.SoeWebConfiguration;
import cn.soe.boot.autoconfigure.web.filter.RequestParamProcessorFilter;
import cn.soe.boot.autoconfigure.web.filter.cors.CorsFilterConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * @author xiezhenxiang 2023/5/4
 **/
@SoeWebConfiguration
@ConditionalOnProperty(prefix = "soe.xss", name = "enable", havingValue = "true")
@EnableConfigurationProperties({XssFilterProperties.class})
public class XssFilterConfiguration {

    public static final int ORDER = CorsFilterConfiguration.ORDER + 1;
    private static final String FILTER_NAME = "soeXssFilter";

    private final XssFilterProperties xssProperties;

    public XssFilterConfiguration(XssFilterProperties xssProperties) {
        this.xssProperties = xssProperties;
    }

    @Bean
    public FilterRegistrationBean<RequestParamProcessorFilter> xssFilterRegistration(XssParamConvertor paramConvertor) {
        FilterRegistrationBean<RequestParamProcessorFilter> filterRegistrationBean = new FilterRegistrationBean<>(new RequestParamProcessorFilter(xssProperties, paramConvertor));
        filterRegistrationBean.setOrder(ORDER);
        filterRegistrationBean.setName(FILTER_NAME);
        return filterRegistrationBean;
    }

    @Bean
    public XssParamConvertor defaultXssProcessor(){
        return new XssParamConvertor(xssProperties);
    }
}
