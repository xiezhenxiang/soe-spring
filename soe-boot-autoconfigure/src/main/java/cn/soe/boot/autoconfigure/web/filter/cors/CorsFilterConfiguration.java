package cn.soe.boot.autoconfigure.web.filter.cors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author xiezhenxiang 2021/9/13
 */
@Configuration
@EnableConfigurationProperties(CorsFilterProperties.class)
@ConditionalOnProperty(prefix = "soe.cors", name = "enable", havingValue = "true")
public class CorsFilterConfiguration {

    public static final int ORDER = Ordered.HIGHEST_PRECEDENCE;
    private static final String FILTER_NAME = "soeCorsFilter";
    private final CorsFilterProperties corsProperties;

    public CorsFilterConfiguration(CorsFilterProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration(CorsConfigurationSource corsConfigurationSource) {
        FilterRegistrationBean<CorsFilter> filterRegistrationBean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource));
        filterRegistrationBean.setOrder(ORDER);
        filterRegistrationBean.setName(FILTER_NAME);
        return filterRegistrationBean;
    }

    @Bean
    @ConditionalOnMissingBean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(corsProperties.getAllowCredentials());
        corsConfiguration.addAllowedOrigin(corsProperties.getAllowedOrigin());
        corsConfiguration.addAllowedHeader(corsProperties.getAllowedHeader());
        corsConfiguration.addAllowedMethod(corsProperties.getAllowedMethod());
        corsConfiguration.setMaxAge(corsProperties.getMaxAge());
        corsConfiguration.setExposedHeaders(corsProperties.getExposedHeaders());
        source.registerCorsConfiguration(corsProperties.getPattern(), corsConfiguration);
        return source;
    }
}
